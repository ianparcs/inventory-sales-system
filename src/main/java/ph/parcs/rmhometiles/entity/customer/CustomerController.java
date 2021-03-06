package ph.parcs.rmhometiles.entity.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.converter.CustomerConverter;

import java.util.List;

@Controller
@Scope("prototype")
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
        configureCustomerCombobox();
    }

    @FXML
    private void fillUpCustomerDetails() {
        if (customer != null) {
            lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
            lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
            lblName.setText(StringUtils.isEmpty(customer.getName()) ? "n/a" : customer.getName());
            btnClearCustomer.setVisible(true);
            btnAddUser.setVisible(false);
        }
    }

    private void configureCustomerCombobox() {
        cbCustomer.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Customer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            customer = item;
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        cbCustomer.setConverter(new CustomerConverter(cbCustomer.getValue()));
        fillCustomerComboboxValues();
    }

    private void fillCustomerComboboxValues() {
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
            cbCustomer.setVisibleRowCount(customers.size());
        });
    }

    @FXML
    private void clearCustomerDetails() {
        this.customer = null;
        cbCustomer.setValue(null);
        cbCustomer.hide();

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
