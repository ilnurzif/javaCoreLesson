import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField;

    @FXML
    PasswordField passField;

    @FXML
    HBox loginBox;

    @FXML
    ComboBox usrListComboBox;

    private Network network;
    private boolean authenticated;
    private String nickname;
    private static String All = "All";
    private HistoryManager historyManager;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        loginBox.setVisible(!authenticated);
        loginBox.setManaged(!authenticated);
        msgField.setVisible(authenticated);
        msgField.setManaged(authenticated);
        usrListComboBox.setVisible(authenticated);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
    }

    public void connect() {
        try {
            setAuthenticated(false);
            network = new Network(8189);
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String msg = network.readMsg();
                        if (msg.startsWith("/authok ")) { // /authok nick1
                            nickname = msg.split(" ")[1];
                            setAuthenticated(true);
                             historyManager = new FileHistoryManager();
                            List<String> lastMsgList=historyManager.getLastMsg();
                            for (String str:
                                    lastMsgList) {
                                textArea.appendText(str + "\n");
                            }
                            break;
                        }
                        textArea.appendText(msg + "\n");
                    }
                    while (true) {
                        String msg = network.readMsg();
                        if (msg.equals("/end_confirm")) {
                            textArea.appendText("Завершено общение с сервером");
                            break;
                        }
                        if (msg.startsWith("/userlistupdate!")) {
                            userListReload(msg);
                            continue;
                        }
                        textArea.appendText(msg + System.lineSeparator());
                        historyManager.appendText(msg+ System.lineSeparator());
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Соединение с серверов разорвано", ButtonType.OK);
                        alert.showAndWait();
                    });
                } finally {
                    historyManager.close();
                    network.close();
                    Platform.exit();
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Невозможно подключиться к серверу", ButtonType.OK);
            alert.showAndWait();
        }
    }


    private void userListReload(String msg) {
        String[] subStr;
        subStr = msg.split("!");
        if (subStr.length < 2) return;
        subStr[0] = "All";
        usrListComboBox.getItems().clear();
        ObservableList<String> users = FXCollections.observableArrayList(subStr);
        usrListComboBox.setItems(users);
       // usrListComboBox.getSelectionModel().selectFirst();
    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            String msg = usrListComboBox.getValue().equals(All) ? msgField.getText()
                    : msgField.getText() + "!PRIVATE!" + usrListComboBox.getValue();
            network.sendMsg(msg);
            msgField.clear();
            msgField.requestFocus();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось отправить сообщение, проверьте сетевое подключение", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        try {
            if (network!=null && network.isConnected())
              return;
            connect();
            network.sendMsg("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось отправить сообщение, проверьте сетевое подключение", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
