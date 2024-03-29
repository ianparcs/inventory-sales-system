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
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerService;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.entity.user.UserRepository;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.ui.scene.SceneManager;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;


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

    private UserRepository userRepository;
    private CustomerService customerService;

    @FXML
    private void initialize() {
        User user = userRepository.findByUsername(tfUserName.getText());
        if (user == null) {
            userService.saveUser(createUser());
        }

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
            Platform.runLater(() -> {
                sceneManager.load();
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
        User admin  = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole("admin");
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

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
