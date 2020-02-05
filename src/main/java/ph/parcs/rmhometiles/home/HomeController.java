package ph.parcs.rmhometiles.home;

import com.jfoenix.controls.JFXSpinner;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @FXML
    private StackPane apContent;

    public void setContent(Parent content) {
        apContent.getChildren().setAll(content);
    }

}
