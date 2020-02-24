import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileHistoryManager implements HistoryManager {
    private static String chatLog = "chatLog.cht";
    private static int historyMsgSize = 100;
    private OutputStreamWriter out;


    @Override
    public List<String> getLastMsg() {
        return lastMsg;
    }

    private List<String> lastMsg;

    public FileHistoryManager() {
        try {
            lastMsg = loadLastMsg();
            out = new OutputStreamWriter(new FileOutputStream(chatLog, true), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadLastMsg() {
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(chatLog), StandardCharsets.UTF_8);
            int x;
            List<String> msgList = new ArrayList<>();
            Deque<String> queue = new ArrayDeque<>(historyMsgSize);
            StringBuilder sb = new StringBuilder();
            while ((x = in.read()) != -1) {
                if ((char) x == '\n') {
                    if (queue.size() >= historyMsgSize)
                        queue.removeFirst();
                    queue.add(sb.toString());
                    sb.setLength(0);
                } else if ((char) x != '\r')
                    sb.append((char) x);
            }
            while (!queue.isEmpty())
                msgList.add(queue.remove());
            in.close();
            return msgList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void appendText(String msg) {
        try {
            out.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



