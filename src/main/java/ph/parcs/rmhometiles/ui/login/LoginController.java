package ph.parcs.rmhometiles.ui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.State;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.entity.user.UserRepository;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.session.SessionService;
import ph.parcs.rmhometiles.ui.scene.SceneManager;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;

import java.util.ArrayList;


@Slf4j
@Controller
public class LoginController {

    @FXML
    private JFXPasswordField pfUserPassword;
    @FXML
    private FontAwesomeIconView icoUser;
    @FXML
    private FontAwesomeIconView icoKey;

    @FXML
    private StackPane spLoginAccount;
    @FXML
    private JFXTextField tfUserName;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private StackPane spRoot;

    @FXML
    private Parent vbRegister;
    @FXML
    private Parent vbLogin;

    private UserService userService;
    private SceneManager sceneManager;

    private UserRepository userRepository;

    @FXML
    private void initialize() {
        setUserFieldStyle(icoKey, pfUserPassword);
        setUserFieldStyle(icoUser, tfUserName);
        //       btnLogin.fire();
    }

    private void setUserFieldStyle(FontAwesomeIconView icon, TextField textField) {
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                icon.setGlyphStyle("-fx-fill: primary; -glyph-size: 22px;");
            } else {
                icon.setGlyphStyle("-fx-fill: #5a5c69; -glyph-size: 22px;");
            }
        });
    }

    @FXML
    public void onLoginButtonClicked() {
        final String username = tfUserName.getText();
        final String password = pfUserPassword.getText();

        new Thread(() -> {
            User user = userRepository.findByUsername(tfUserName.getText());
            userService.authenticate(username, password);

            if (userService.isAuthenticated()) {
                login(user);
            } else {
                showLoginErrorDialog();
            }
        }).start();
    }

    @FXML
    public void onRegisterButtonClicked() {
        showRegisterAccountScene();
    }

    private void login(User user) {
        SessionService.getInstance().setLoggedInUser(user);
        Platform.runLater(() -> {
            sceneManager.load();
            sceneManager.changeScene(State.HOME);
        });
    }

    public void showLoginScene() {
        switchTo(vbLogin);
    }

    public void showRegisterAccountScene() {
        switchTo(vbRegister);
    }

    private void switchTo(Node target) {
        for (Node node : new ArrayList<>(spLoginAccount.getChildren())) {
            node.setVisible(node == target);
            if (node == target) {
                node.toFront();
            }
        }
    }


    private void showLoginErrorDialog() {
        String errorMessage = """
                The account '%s' does not exist, or the password is incorrect
                """.formatted(tfUserName.getText());

        SweetAlert errorLogin = SweetAlertFactory.create(SweetAlert.Type.DANGER);
        errorLogin.setHeaderMessage("Bad Credentials!");
        errorLogin.setContentMessage(errorMessage);
        errorLogin.show(spRoot);
    }

    private User createUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(AppConstant.Role.USER);
        return admin;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
