public interface AuthManager {
    String getNicknameByLoginAndPassword(String login, String password);

    void changeNickName(String oldNickname, String newNickName);
}
