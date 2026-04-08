package ph.parcs.rmhometiles.ui.register;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.user.UserData;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.exception.AppException;
import ph.parcs.rmhometiles.exception.ErrorCode;
import ph.parcs.rmhometiles.ui.login.LoginController;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;


@Controller
public class RegisterAccountController {

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
    @FXML
    private StackPane spRegisterForm;

    private LoginController loginController;
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
        new Thread(() -> {
            try {
                String password = pfUserPassword.getText();
                String confirmPassword = pfConfirmUserPassword.getText();

                if (!password.equals(confirmPassword)) throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

                userService.createUser(retrieveUserInput());
                SweetAlertFactory.create(SweetAlert.Type.SUCCESS)
                        .setConfirmListener(() -> loginController.showLoginScene())
                        .setContentMessage(AppConstant.Message.USER_REGISTERED)
                        .show(spRegisterForm);

            } catch (AppException e) {
                SweetAlertFactory.create(SweetAlert.Type.DANGER, e.getMessage()).show(spRegisterForm);
            } catch (DataIntegrityViolationException e) {
                SweetAlertFactory.create(SweetAlert.Type.DANGER, ErrorCode.USER_EXIST.getTypeValue()).show(spRegisterForm);
            }
        }).start();
    }

    private UserData retrieveUserInput() {
        return UserData.builder()
                .role(AppConstant.Role.USER)
                .fullName(tfFullName.getText())
                .password(pfUserPassword.getText())
                .username(tfUserName.getText())
                .build();
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
