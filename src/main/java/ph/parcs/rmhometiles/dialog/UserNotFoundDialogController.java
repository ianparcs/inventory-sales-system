package ph.parcs.rmhometiles.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class UserNotFoundDialogController {

    @FXML
    private JFXDialog warningDialog;

    @FXML
    private StackPane stackpane;

    @FXML
    private Label lblContent;

    public void setMessage(String msg) {
        lblContent.setText(msg);
    }

    public void showDialog() {
        warningDialog.show(stackpane);
    }

    public void close() {
        warningDialog.close();
    }
}
