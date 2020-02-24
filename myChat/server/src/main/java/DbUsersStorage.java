import java.sql.*;

public class DbUsersStorage implements AuthManager {
    private Connection connection;
    private Statement stmt;
    private PreparedStatement ps;
    private PreparedStatement upd_ps;

    public DbUsersStorage() {
        try {
            connect();
            createTableEx();
            prepareStatements();
            fillUserList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:dbusers.db");
        stmt = connection.createStatement();
    }

    private void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableEx() throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS userslist (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT, nickname TEXT);");
    }

    private void prepareStatements() throws SQLException {
        ps = connection.prepareStatement("INSERT INTO userslist (login, password, nickname) VALUES (?, ?, ?);");
        //upd_ps=connection.prepareStatement("UPDATE userslist SET nickname = ? WHERE nickname= ? ;");
    }

    private void fillUserList() throws SQLException {
        connection.setAutoCommit(false);
        addUser("login1", "pass1", "user1");
        addUser("login2", "pass2", "user2");
        addUser("login3", "pass3", "user3");
        connection.commit();
    }

    private void addUser(String login, String password, String nickname) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT count(*) as count FROM userslist WHERE login='" + login + "';");
        rs.next();
        int count=rs.getInt("count");
        if (count > 0) return;
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, nickname);
        ps.executeUpdate();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
           ResultSet rs = stmt.executeQuery("SELECT login, password, nickname FROM userslist WHERE login='" + login + "';");
                if (rs.getString("password").equals(password) &&
                        rs.getString("login").equals(login)
                ) return rs.getString("nickname");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeNickName(String oldNickname, String newNickName) {
        try {
            stmt.executeUpdate("UPDATE userslist SET nickname = '"+newNickName+"' WHERE nickname= '"+oldNickname+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
