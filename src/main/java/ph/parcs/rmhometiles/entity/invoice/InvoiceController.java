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
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class InvoiceController {

    @FXML
    private JFXComboBox<BaseEntity> cbCustomer;
    @FXML
    private JFXComboBox<BaseEntity> cbProducts;
    @FXML
    private TableView<Invoice> tvInvoice;
    @FXML
    private JFXTextField tfAddress;
    @FXML
    private JFXTextField tfContact;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private StackPane spMain;

    private EditItemController<Customer> customerEditController;
    private InvoiceProductController invoiceProductController;
    private CustomerService customerService;
    private ProductService productService;
    private InvoiceService invoiceService;
    private SweetAlert successAlert;

    private boolean isCustomerAdded;
    @FXML
    public void initialize() {
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
        configureCustomerCombobox();
        configureProductCombobox();
    }

    private void configureCustomerCombobox() {
        setComboboxConverter(cbCustomer);
        cbCustomer.getEditor().textProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(() -> {
            if(!StringUtils.isEmpty(newVal) && !isCustomerAdded){
                cbCustomer.show();
                searchCustomer(newVal);
            }
        }));

        cbCustomer.focusedProperty().addListener((observableValue, outOfFocus, focus) -> Platform.runLater(() -> {
            String editorTxt = cbCustomer.getEditor().getText();
            if (focus) {
                isCustomerAdded = false;
                if (editorTxt.isEmpty()) {
                    searchCustomer(Global.STRING_EMPTY);
                }
            }
            if (outOfFocus) {
                Customer customer = (Customer) cbCustomer.getValue();
                if (customer != null && !customer.getName().equals(editorTxt)) {
                    cbCustomer.getEditor().setText(customer.getName());
                } else if (customer == null) {
                    clearFields();
                }
            }
        }));
    }

    private void configureProductCombobox() {
        setComboboxConverter(cbProducts);

        cbProducts.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> Platform.runLater(() -> {
            cbProducts.show();
            searchProduct(keyTyped);
        }));

        cbProducts.focusedProperty().addListener((observableValue, outOfFocus, focus) -> Platform.runLater(() -> {
            String editorTxt = cbCustomer.getEditor().getText();
            if (focus && editorTxt.isEmpty()) {
                searchCustomer(Global.STRING_EMPTY);
            }
        }));
    }

    private void setComboboxConverter(JFXComboBox<BaseEntity> cb) {
        cb.setConverter(new StringConverter<>() {
            @Override
            public String toString(BaseEntity baseEntity) {
                if (baseEntity == null) return Global.STRING_EMPTY;
                return baseEntity.getName();
            }

            @Override
            public BaseEntity fromString(String s) {
                return cb.getValue();
            }
        });
    }

    private void searchCustomer(String query) {
        Set<Customer> customers = customerService.findItems(query);
        cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers));
    }

    private void searchProduct(String query) {
        Set<Product> products = productService.findItems(query);
        cbProducts.getItems().setAll(FXCollections.observableArrayList(products));
    }

    @FXML
    private void selectCustomer() {
        Customer customer = (Customer) cbCustomer.getValue();
        if (customer != null) {
            tfAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
            tfContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
        }
    }

    @FXML
    private void selectProducts() {
        invoiceProductController.show(spMain);
    }

    @FXML
    private void clearFields() {
        cbCustomer.setValue(null);
        cbCustomer.hide();
        tfContact.clear();
        tfAddress.clear();
    }

    @FXML
    private void showAddCustomer() {
        customerEditController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(Customer customer) {
                if (customer != null) {
                    cbCustomer.setValue(customer);
                    tfAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
                    tfContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
                }
                isCustomerAdded = true;
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
    public void setInvoiceProductController(InvoiceProductController invoiceProductController) {
        this.invoiceProductController = invoiceProductController;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
}
