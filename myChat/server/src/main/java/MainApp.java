import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class MainApp {

    public static void main(String[] args) {
         new Server(8189);
//        Msg uMsg=new Msg("mmm!PRIVATE!user1");
//        System.out.println("args = " + Arrays.deepToString(args));
    }
}
