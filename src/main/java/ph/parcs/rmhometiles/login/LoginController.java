package ph.parcs.rmhometiles.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.scene.SceneManager;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.user.User;
import ph.parcs.rmhometiles.user.UserService;


@Controller
public class LoginController {

    @FXML
    private JFXPasswordField pfUserPassword;
    @FXML
    private JFXTextField tfUserName;

    private UserService userService;
    private SceneManager sceneManager;

    @FXML
    private void initialize() {
        tfUserName.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                tfUserName.validate();
            }
        });
        pfUserPassword.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                pfUserPassword.validate();
            }
        });
    }

    public void login() {
        String username = tfUserName.getText();
        String password = pfUserPassword.getText();

        new Thread(() -> {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userService.isExist(userDetails)) {
                userService.login(userDetails, password);
            }

            Platform.runLater(() -> {
                if (userService.isAuthenticated()) {
                    sceneManager.changeScene(State.HOME);
                } else {
                    showErrorDialog();
                }
            });
        }).start();
    }

    private void showErrorDialog() {
        SweetAlert errorLogin = SweetAlertFactory.create(SweetAlert.Type.DANGER);
        errorLogin.show((StackPane) tfUserName.getScene().getRoot());
    }

    private User createUser() {
        User fakeUser = new User();
        fakeUser.setPassword("ian");
        fakeUser.setUsername("ian");
        fakeUser.setRole("admin");
        return fakeUser;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
