package ph.parcs.rmhometiles.home;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @FXML
    private StackPane apContent;

    public void setContent(Parent content) {
        apContent.getChildren().setAll(content);
    }

}
