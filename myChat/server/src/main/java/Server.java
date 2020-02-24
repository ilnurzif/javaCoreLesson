import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private AuthManager authManager;
    private List<ClientHandler> clients;
    private int clientsMax=10;
    public ExecutorService service;
    private final Logger LOGGER = (Logger) LogManager.getLogger(Server.class);

    public AuthManager getAuthManager() {
        return authManager;
    }

    public Server(int port) {
        clients = new ArrayList<>();
       //  authManager = new BasicAuthManager();
         authManager = new DbUsersStorage();
        service= Executors.newFixedThreadPool(clientsMax);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
       //     System.out.println("Сервер запущен. Ожидаем подключения клиентов...");
            LOGGER.debug("Сервер запущен. Ожидаем подключения клиентов... logger" );
            while (true) {
                Socket socket = serverSocket.accept();
               //  System.out.println("Клиент подключился");
              LOGGER.debug("Клиент подключился" );
                ClientHandler o = new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void close() {
        service.shutdown();
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
         LOGGER.debug(msg);
    }

    // Личные сообщения
    public void broadcastMsg(String msg, String nickName) {
         for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickName))
                o.sendMsg(msg);
        }
        LOGGER.debug(msg);
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public String getUserList() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler o : clients) {
            sb.append(o.getNickname() + "!");
        }
         return sb.toString();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void nickChange(String oldNickname, String newNickName) {
        authManager.changeNickName(oldNickname, newNickName);
    }
}
