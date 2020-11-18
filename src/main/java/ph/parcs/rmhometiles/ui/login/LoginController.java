package ph.parcs.rmhometiles.ui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.ui.scene.SceneManager;


@Controller
public class LoginController {

    @FXML
    private JFXPasswordField pfUserPassword;
    @FXML
    private FontAwesomeIconView icoKey;
    @FXML
    private FontAwesomeIconView icoUser;
    @FXML
    private JFXTextField tfUserName;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private StackPane spRoot;

    private UserService userService;
    private SceneManager sceneManager;

    @FXML
    private void initialize() {
      //  userService.saveUser(createUser());
        setUserFieldStyle(pfUserPassword, icoKey);
        setUserFieldStyle(tfUserName, icoUser);
        btnLogin.fire();
    }

    private void setUserFieldStyle(TextField textField, FontAwesomeIconView icon) {
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                icon.setGlyphStyle("-fx-fill: primary; -glyph-size: 20px;");
            } else {
                icon.setGlyphStyle("-fx-fill: #5a5c69; -glyph-size: 20px;");
            }
        });
    }

    @FXML
    public void login() {
        final String username = tfUserName.getText();
        final String password = pfUserPassword.getText();

        new Thread(() -> {
            userService.authenticate(username, password);
            sceneManager.load();
            Platform.runLater(() -> {
                if (userService.isAuthenticated()) {
                    gotoHomeScene();
                } else {
                    showErrorDialog();
                }
            });
        }).start();
    }

    private void gotoHomeScene() {
        sceneManager.changeScene(State.HOME);
    }

    private void showErrorDialog() {
        SweetAlert errorLogin = SweetAlertFactory.create(SweetAlert.Type.DANGER);
        errorLogin.setHeaderMessage("Bad Credentials!");
        errorLogin.setContentMessage("Account " + tfUserName.getText() + " doesn't exist. Password or username doesn't match");
        errorLogin.show(spRoot);
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
