import javafx.scene.chart.ScatterChart;

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
        // использование пула потоков в чате позволит:
        // 1) ограничить количество одновременных соединений с клиентами в случае нехватки серверных мощностей
        // 2) одновременное управление потоками например disconnect со всеми соеденениями методом service.shutdown();
        // 3) задавать таймаут после которого закрываем соединение с клиентом (просим переавторизоваться)
      //   Future<Boolean> future = server.service.submit(() -> {
            server.service.execute(() -> {
     //   new Thread(() -> {
                    try {
                        while (true) { // цикл аутентификации
                            String msg = in.readUTF();
                            System.out.print("Сообщение от клиента: " + msg + "\n");
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
                            System.out.print("Сообщение от клиента: " + msg + "\n");
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
                        e.printStackTrace();
                    } finally {
                       close();
                    }
                  //   }).start();
                   //  return this.connected;
                }
             );

/*        try {
            future.get(Server.TIMEOUTSEC, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            //e.printStackTrace();
            this.connected=false;
            sendMsg("/end_confirm"); // после истечениии таймаута выбиваем пользователя из чата
        }*/
    }


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
