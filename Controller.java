package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controller {
    @FXML
    TextArea mainArea;

    @FXML
    TextField msgField;


    public void sendMsgAction(ActionEvent actionEvent) {
        if (msgField.getText().trim().length() > 0) {
            mainArea.appendText(msgField.getText() + "\n");
            msgField.clear();
            msgField.requestFocus();
        }
    }
}
