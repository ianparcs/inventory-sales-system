package ph.parcs.rmhometiles.ui.register;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.ui.login.LoginController;


@Controller
public class RegisterAccountController {

    private LoginController loginController;

    @FXML
    private FontAwesomeIconView icoKeyConfirmPassword;
    @FXML
    private FontAwesomeIconView icoKeyPassword;
    @FXML
    private FontAwesomeIconView icoFullName;
    @FXML
    private FontAwesomeIconView icoUser;

    @FXML
    private JFXPasswordField pfConfirmUserPassword;
    @FXML
    private JFXPasswordField pfUserPassword;
    @FXML
    private JFXTextField tfFullName;
    @FXML
    private JFXTextField tfUserName;

    private UserService userService;

    @FXML
    private void initialize() {
        setUserFieldStyle(icoKeyConfirmPassword, pfConfirmUserPassword);
        setUserFieldStyle(icoKeyPassword, pfUserPassword);
        setUserFieldStyle(icoFullName, tfFullName);
        setUserFieldStyle(icoUser, tfUserName);
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
    public void onRegisterSubmitButtonClicked() {
    }

    @FXML
    public void onRegisterBackButtonClicked() {
        loginController.showLoginScene();
    }

    @Autowired
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
