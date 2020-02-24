import javafx.scene.chart.ScatterChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;
    private Boolean connected=false;
    private final Logger LOGGER = (Logger) LogManager.getLogger(ClientHandler.class);

    public Boolean getConnected() {
        return connected;
    }

    public String getNickname() {
        return nickname;
    }

    private void setNickname(String msg) throws IOException {
        server.nickChange(this.nickname, msg.split(" ")[1]);
        this.nickname=msg.split(" ")[1];
        server.broadcastMsg("/userlistupdate!" + server.getUserList());
    }

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
                    try {
                        while (true) { // цикл аутентификации
                            String msg = in.readUTF();
                        //    System.out.print("Сообщение от клиента: " + msg + "\n");
                             LOGGER.debug("Сообщение от клиента: " + msg + "\n");

                            if (msg.startsWith("/auth ")) { // /auth login1 pass1
                                String[] tokens = msg.split(" ", 3);
                                String nickFromAuthManager = server.getAuthManager().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
                                if (nickFromAuthManager != null) {
                                    if (server.isNickBusy(nickFromAuthManager)) {
                                        sendMsg("Данный пользователь уже в чате");
                                        continue;
                                    }
                                    nickname = nickFromAuthManager;
                                    server.subscribe(this);
                                    sendMsg("/authok " + nickname);
                                    server.broadcastMsg("Пользователь " + getNickname() + " присоединился к чату");
                                    server.broadcastMsg("/userlistupdate!" + server.getUserList());
                                    this.connected=true;
                                    break;
                                } else {
                                    sendMsg("Указан неверный логин/пароль");
                                }
                            }
                        }
                        while (true) { // цикл общения с сервером (обмен текстовыми сообщениями и командами)
                            String msg = in.readUTF();
                         //   System.out.print("Сообщение от клиента: " + msg + "\n");
                            LOGGER.debug("Сообщение от клиента: " + msg + "\n");
                            if (msg.startsWith("/")) {
                                if (msg.equals("/end")) {
                                    sendMsg("/end_confirm");
                                    break;
                                }
                                if (msg.startsWith("/change_nick")) {
                                    setNickname(msg);
                                    continue;
                                }
                            } else {
                                Msg uMsg = new Msg(msg);
                                if (uMsg.getMsgType() == MsgType.PUBLIC)
                                    server.broadcastMsg(nickname + ": " + msg);
                                else
                                    server.broadcastMsg(nickname + "(приватное): " + uMsg.getMsg(), uMsg.getUserName());
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(e);
                    } finally {
                       close();
                    }
                }).start();
    }


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
            LOGGER.info(msg);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void close() {
        server.broadcastMsg("Пользователь " + getNickname() + " покинул чат");
        server.unsubscribe(this);
        server.broadcastMsg("/userlistupdate!" + server.getUserList());
        nickname = null;
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
