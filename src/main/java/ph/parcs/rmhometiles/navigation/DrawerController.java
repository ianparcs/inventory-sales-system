package ph.parcs.rmhometiles.navigation;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.scene.SceneManager;

@Controller
public class DrawerController {

    @FXML
    private JFXDrawer drawer;
    private SceneManager sceneManager;

    @FXML
    private void initialize() {
        VBox drawerContent = (VBox) sceneManager.loadUI(State.NAVIGATION);
        drawer.setSidePane(drawerContent);
        drawer.open();
    }

    public boolean isDrawerOpened() {
        return drawer.isOpened();
    }

    public void closeDrawer() {
        drawer.close();

    }

    public void openDrawer() {
        drawer.open();
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
