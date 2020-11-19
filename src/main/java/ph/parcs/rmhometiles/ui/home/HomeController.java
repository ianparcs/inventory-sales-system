package ph.parcs.rmhometiles.ui.home;

import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @FXML
    private StackPane apContent;

    public void setContent(Parent content) {
        apContent.getChildren().setAll(content);
        apContent.setCache(true);
        apContent.setCacheHint(CacheHint.SPEED);
    }
}
