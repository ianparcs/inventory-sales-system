package ph.parcs.rmhometiles.entity.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.converter.CustomerConverter;

import java.util.List;

@Controller
@Scope("singleton")
public class CustomerController {

    @FXML
    private JFXComboBox<Customer> cbCustomer;
    @FXML
    private JFXButton btnClearCustomer;
    @FXML
    private JFXButton btnAddUser;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblName;

    private CustomerEditController customerEditController;
    private CustomerService customerService;
    private Customer customer;
    private StackPane spMain;

    @FXML
    public void initialize() {
        fillCustomerComboboxValues();
    }

    @FXML
    private void selectCostumer() {
        if (cbCustomer != null && cbCustomer.getValue() != null) customer = cbCustomer.getValue();
        if (customer != null) {
            lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
            lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
            lblName.setText(StringUtils.isEmpty(customer.getName()) ? "n/a" : customer.getName());
            btnClearCustomer.setVisible(true);
            btnAddUser.setVisible(false);
        }

        Platform.runLater(() -> {
            //  cbCustomer.valueProperty().set(null);
            cbCustomer.hide();
            spMain.requestFocus();
        });
    }

    private void fillCustomerComboboxValues() {
        cbCustomer.setConverter(new CustomerConverter(cbCustomer.getValue()));
        cbCustomer.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> showCustomer(keyTyped));
        cbCustomer.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) showCustomer(Global.STRING_EMPTY);
        });
    }

    private void showCustomer(String query) {
        List<Customer> customers = customerService.findEntities(query);
        cbCustomer.show();
        Platform.runLater(() -> {
            cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers));
        });
    }

    @FXML
    public void clearCustomerDetails() {
        this.customer = null;
        cbCustomer.getSelectionModel().clearSelection();
        //  cbCustomer.hide();

        btnClearCustomer.setVisible(false);
        btnAddUser.setVisible(true);
        lblContact.setText("");
        lblAddress.setText("");
        lblName.setText("");
    }

    @FXML
    private void showAddCustomer() {
        customerEditController.onEditItem(new ItemListener<>() {

            @Override
            public void onSavedSuccess(Customer customer) {
                if (customer != null) {
                    cbCustomer.setValue(customer);
                    lblName.setText(StringUtils.isEmpty(customer.getName()) ? "n/a" : customer.getName());
                    lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
                    lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
                }

                SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
                successAlert.setContentMessage(Global.Message.SAVED).show(spMain);
                cbCustomer.hide();
            }

            @Override
            public void onSaveFailed(Customer savedItem) {

            }
        }, new Customer());
        customerEditController.showDialog(spMain);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setSpMain(StackPane spMain) {
        this.spMain = spMain;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCustomerEditController(CustomerEditController customerEditController) {
        this.customerEditController = customerEditController;
    }

}
