package ph.parcs.rmhometiles.navigation;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.home.HomeController;
import ph.parcs.rmhometiles.scene.SceneManager;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NavigationController {

    @FXML
    private JFXButton btnInvoice;
    @FXML
    private JFXButton btnCategory;
    @FXML
    private JFXButton btnInventory;
    @FXML
    private JFXButton btnDashboard;
    @FXML
    private JFXButton btnSupplier;
    @FXML
    private JFXButton btnSales;
    @FXML
    private JFXButton btnTitle;
    @FXML
    private JFXButton btnLog;

    private SceneManager sceneManager;
    private HomeController homeController;
    private Map<State, JFXButton> states;

    @FXML
    private void initialize() {
        states = new HashMap<>();
        states.put(State.PRODUCT, btnInventory);
        states.put(State.DASHBOARD, btnDashboard);
        states.put(State.CATEGORY, btnCategory);
        states.put(State.SUPPLIER, btnSupplier);
        states.put(State.SALE_REPORT, btnSales);
        states.put(State.INVOICE, btnInvoice);
        states.put(State.LOG, btnLog);

        states.forEach((key, value) -> value.setOnAction(actionEvent -> {
            Parent content = sceneManager.getContent(key);
            homeController.setContent(content);
        }));
    }

    public void hideText() {
        states.forEach((key, value) -> value.setContentDisplay(ContentDisplay.GRAPHIC_ONLY));
        btnTitle.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public void showText() {
        btnTitle.setContentDisplay(ContentDisplay.LEFT);
        states.forEach((key, value) -> value.setContentDisplay(ContentDisplay.LEFT));
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Autowired
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }
}
