package ph.parcs.rmhometiles.ui.menu;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ui.navigation.DrawerController;
import ph.parcs.rmhometiles.util.WindowHelper;

@Controller
public class TitleBarController {

    @FXML
    private Parent apTitleBar;
    @FXML
    private JFXHamburger hbgNavigation;

    private DrawerController drawerController;

    @FXML
    public void initialize() {
        HamburgerNextArrowBasicTransition burgerTask = new HamburgerNextArrowBasicTransition(hbgNavigation);
        burgerTask.setRate(-1);

        hbgNavigation.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();

            if (drawerController.isDrawerOpened()) {
                drawerController.closeDrawer();
            } else {
                drawerController.openDrawer();
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
    public void setDrawerController(DrawerController drawerController) {
        this.drawerController = drawerController;
    }
}
