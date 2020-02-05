package ph.parcs.rmhometiles.menu;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.navigation.NavigationController;
import ph.parcs.rmhometiles.util.WindowHelper;

@Controller
public class MenuTitleController {

    @FXML
    private Parent apTitleBar;

    @FXML
    private JFXHamburger hbgNavigation;

    private NavigationController navigationController;

    @FXML
    public void initialize() {
        HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hbgNavigation);
        burgerTask.setRate(-1);

        hbgNavigation.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();

            if (navigationController.isDrawerOpened()) {
                navigationController.closeDrawer();
            } else {
                navigationController.openDrawer();
            }
        });

        WindowHelper.setDraggable(apTitleBar);
    }


    @FXML
    public void closeWindow() {
        stage().close();
    }

    @FXML
    public void maximizeWindow() {
        stage().setMaximized(!stage().isMaximized());
    }

    @FXML
    public void minimizeWindow() {
        stage().setIconified(!stage().isIconified());
    }

    private Stage stage() {
        return ((Stage) apTitleBar.getScene().getWindow());
    }

    @Autowired
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }
}
