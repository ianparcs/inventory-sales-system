package ph.parcs.rmhometiles.ui.home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.ui.scene.SceneManager;

@Controller
public class HomeController {

    @FXML
    private StackPane apContent;
    private SceneManager sceneManager;

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
