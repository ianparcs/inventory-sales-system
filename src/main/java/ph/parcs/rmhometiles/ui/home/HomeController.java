package ph.parcs.rmhometiles.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.ui.scene.SceneManager;

@Controller
public class HomeController {

    @FXML
    private StackPane apContent;
    private SceneManager sceneManager;

    @FXML
    protected void initialize() {
        Platform.runLater(() -> {
            if (apContent != null) maximizeHomeWindow();
        });
    }

    private void maximizeHomeWindow() {
        Stage stage = ((Stage) apContent.getScene().getWindow());
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        stage.setX(screen.getMinX());
        stage.setY(screen.getMinY());
        stage.setWidth(screen.getWidth());
        stage.setHeight(screen.getHeight());
    }

    public void setContent(State state) {
        Parent frontNode = sceneManager.getContent(state);
        if (!apContent.getChildren().contains(frontNode)) {
            apContent.getChildren().add(frontNode);
        }
        for (Node previousNode : apContent.getChildren()) {
            previousNode.setVisible(false);
        }
        frontNode.setVisible(true);
        frontNode.toFront();
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
