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
                        textArea.appendText(msg + "\n");
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Соединение с серверов разорвано", ButtonType.OK);
                        alert.showAndWait();
                    });
                } finally {
                    network.close();
                    Platform.exit();
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            throw new RuntimeException("Невозможно подключиться к серверу");
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
        usrListComboBox.getSelectionModel().selectFirst();
        // Почему то не срабатывает установка ComboBox значения первого элемента
        //usrListComboBox.setValue("All"); тоже не помог
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
            network.sendMsg("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось отправить сообщение, проверьте сетевое подключение", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
