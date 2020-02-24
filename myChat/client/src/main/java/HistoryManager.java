import java.util.List;

public interface HistoryManager {
    public void appendText(String msg);
    public void close();
    public List<String> getLastMsg();
}
