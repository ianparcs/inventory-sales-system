package ph.parcs.rmhometiles.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.dialog.UserNotFoundDialogController;
import ph.parcs.rmhometiles.scene.SceneManager;
import ph.parcs.rmhometiles.user.UserService;

import java.net.URL;
import java.util.ResourceBundle;


@Controller
public class LoginController implements Initializable {

    @FXML
    private JFXPasswordField pfUserPassword;
    @FXML
    private JFXTextField tfUserName;

    private UserNotFoundDialogController userNotFoundDialogController;
    private UserService userService;
    private SceneManager sceneManager;

    public void login() {
        String username = tfUserName.getText();
        String password = pfUserPassword.getText();

        new Thread(() -> {
            userService.login(username, password);
            Platform.runLater(() -> {
                if (userService.isAuthenticated()) {
                    sceneManager.changeScene(State.HOME);
                }

            });
        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
  /*      User fakeUser = new User();
        fakeUser.setPassword("ian");
        fakeUser.setUsername("ian");
        fakeUser.setRole("admin");
        userService.saveUser(fakeUser);*/

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

    public void showUserNotFoundDialog(String username) {
        String message = "User " + username + " not found! \n Please try another username";
        userNotFoundDialogController.setMessage(message);
        userNotFoundDialogController.showDialog();
    }

    @Autowired
    public void setUserNotFoundDialogController(UserNotFoundDialogController userNotFoundDialogController) {
        this.userNotFoundDialogController = userNotFoundDialogController;
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
