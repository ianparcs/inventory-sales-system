package ph.parcs.rmhometiles.ui.navigation;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.session.SessionService;
import ph.parcs.rmhometiles.ui.home.HomeController;
import ph.parcs.rmhometiles.ui.scene.SceneManager;
import ph.parcs.rmhometiles.util.AppConstant;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NavigationController {

    @FXML
    private JFXButton btnDashboard;
    @FXML
    private JFXButton btnInventory;
    @FXML
    private JFXButton btnSupplier;
    @FXML
    private JFXButton btnCustomer;
    @FXML
    private JFXButton btnInvoice;
    @FXML
    private JFXButton btnSales;
    @FXML
    private JFXButton btnLog;
    @FXML
    private VBox vbContainer;

    private HomeController homeController;
    private SceneManager sceneManager;

    @FXML
    private void initialize() {
        Map<State, JFXButton> states = new HashMap<>();
        states.put(State.INVENTORY, btnInventory);
        states.put(State.DASHBOARD, btnDashboard);
        states.put(State.SUPPLIER, btnSupplier);
        states.put(State.SALE_REPORT, btnSales);
        states.put(State.CUSTOMER, btnCustomer);
        states.put(State.INVOICE, btnInvoice);
        states.put(State.LOG, btnLog);


        Platform.runLater(() -> {
            User currentUser = SessionService.getInstance().getLoggedInUser();
            if (currentUser != null) {
                var userRole = currentUser.getRole();
                vbContainer.getChildren().remove(btnDashboard);

                if (userRole.equals(AppConstant.Role.USER)) {
                    vbContainer.getChildren().remove(btnDashboard);
                }
            }
        });

        Platform.runLater(() -> states.forEach((key, value) -> value.setOnAction(actionEvent -> {
            Parent content = sceneManager.getContent(key);
            content.setCache(true);
            content.setCacheHint(CacheHint.QUALITY);
            homeController.setContent(content);
        })));
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
