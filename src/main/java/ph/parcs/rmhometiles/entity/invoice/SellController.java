package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerEditController;
import ph.parcs.rmhometiles.entity.customer.CustomerService;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;

import java.util.List;

@Controller
public class SellController {

    @FXML
    private TableView<Customer> tvItem;
    @FXML
    private JFXTextField tfAddress;
    @FXML
    private JFXTextField tfContact;
    @FXML
    private JFXComboBox<Customer> cbCustomer;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private StackPane spMain;

    private EditItemController<Customer> customerEditController;
    private SweetAlert successAlert;

    private CustomerService customerService;

    @FXML
    public void initialize() {
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
        cbCustomer.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer customer) {
                if (customer == null) return "";
                return customer.getName();
            }

            @Override
            public Customer fromString(String s) {
                return cbCustomer.getValue();
            }
        });

        cbCustomer.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                List<Customer> customers = customerService.findCustomer(newValue);
                cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers));
            });
        });

        cbCustomer.focusedProperty().addListener((observableValue, aBoolean, showing) -> {
            Platform.runLater(() -> {
                if (showing) {
                    if (cbCustomer.getEditor().getText().isEmpty()) {
                        List<Customer> customers = customerService.findCustomer("");
                        cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers));
                        clearFields();
                    }
                    cbCustomer.hide();
                    cbCustomer.setVisibleRowCount(5);
                    cbCustomer.show();
                }
                if (cbCustomer.getEditor().getText().isEmpty()) {
                    tfContact.clear();
                    tfAddress.clear();
                }
            });
        });
    }

    @FXML
    private void selectCustomer() {
        Customer customer = cbCustomer.getValue();
        if (customer != null) {
            tfAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
            tfContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
        } else {
            clearFields();
        }

        cbCustomer.getEditor().setText("");
        spMain.requestFocus();
    }

    private void clearFields() {
        cbCustomer.getSelectionModel().clearSelection();
        cbCustomer.hide();
        tfContact.clear();
        tfAddress.clear();
    }

    @FXML
    private void showAddCustomer() {
        customerEditController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(Customer entity) {
                successAlert.setContentMessage(Global.MSG.SAVED).show(spMain);
            }

            @Override
            public void onSaveFailed(Customer savedItem) {

            }
        }, new Customer());
        customerEditController.showDialog(spMain);
    }

    @Autowired
    public void setCustomerEditController(CustomerEditController customerEditController) {
        this.customerEditController = customerEditController;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

}
