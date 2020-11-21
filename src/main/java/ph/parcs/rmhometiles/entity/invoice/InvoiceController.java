package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
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
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItemEditController;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class InvoiceController {

    public TableColumn<InvoiceLineItem, Integer> tcQty;
    @FXML
    private JFXComboBox<BaseEntity> cbCustomer;
    @FXML
    private JFXComboBox<BaseEntity> cbProducts;
    @FXML
    private TableView<InvoiceLineItem> tvInvoice;
    @FXML
    private JFXButton btnClearCustomer;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXButton btnAddUser;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblName;
    @FXML
    private StackPane spMain;
    @FXML
    private TableColumn<InvoiceLineItem, HBox> tcAction;

    private EditItemController<Customer> customerEditController;
    private EditItemController<InvoiceLineItem> lineItemEditController;

    private CustomerService customerService;
    private ProductService productService;
    private SweetAlert successAlert;
    private boolean isCustomerAdded;

    @FXML
    public void initialize() {
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
        configureCustomerCombobox();
        configureProductCombobox();
        initDate();

        tvInvoice.setEditable(true);
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction, this::onItemEditAction));
    }

    private void initDate() {
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }

    private void configureCustomerCombobox() {
        setComboboxConverter(cbCustomer);

        cbCustomer.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> {
            List<Customer> customers = customerService.findEntities(keyTyped);
            cbCustomer.show();
            Platform.runLater(() -> cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers)));
        });

        cbCustomer.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) {
                List<Customer> customers = customerService.findEntities(Global.STRING_EMPTY);
                cbCustomer.show();
                Platform.runLater(() -> {
                    cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers));
                });
            }
        });
    }

    private void configureProductCombobox() {
        cbProducts.setConverter(new StringConverter<>() {
            @Override
            public String toString(BaseEntity baseEntity) {
                Product product = (Product) baseEntity;
                if (baseEntity == null) return Global.STRING_EMPTY;
                return product.getCodeProperty();
            }

            @Override
            public Product fromString(String s) {
                return (Product) cbProducts.getValue();
            }
        });


        cbProducts.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> {
            List<Product> products = productService.findEntities(keyTyped);
            cbProducts.show();
            Platform.runLater(() -> cbProducts.getItems().setAll(FXCollections.observableArrayList(products)));
        });

        cbProducts.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) {
                List<Product> products = productService.findEntities(Global.STRING_EMPTY);
                cbProducts.show();
                Platform.runLater(() -> cbProducts.getItems().setAll(FXCollections.observableArrayList(products)));
            }
        });
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

    @FXML
    private void fillUpCustomerDetails() {
        Customer customer = (Customer) cbCustomer.getValue();
        if (customer != null) {
            lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
            lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
            lblName.setText(StringUtils.isEmpty(customer.getName()) ? "n/a" : customer.getName());
            btnClearCustomer.setVisible(true);
            btnAddUser.setVisible(false);
        }
    }

    @FXML
    private void addInvoiceLineItem() {
        Product product = (Product) cbProducts.getValue();
        if (product == null) return;
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setProduct(product);
        invoiceLineItem.setCode(product.getCodeProperty());
        //   invoiceLineItem.setPrice(product.getPrice());
        invoiceLineItem.setQuantity((int) (Math.random() * (10 - 1)));

        tvInvoice.getItems().add(invoiceLineItem);

        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvInvoice.refresh();
        });
    }

    private InvoiceLineItem onItemEditAction(InvoiceLineItem item) {
        lineItemEditController.showDialog((StackPane) tvInvoice.getScene().getRoot());
        lineItemEditController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(InvoiceLineItem entity) {

            }

            @Override
            public void onSaveFailed(InvoiceLineItem savedItem) {

            }
        }, item);
        return item;
    }

    private InvoiceLineItem onItemDeleteAction(InvoiceLineItem s) {
        return new InvoiceLineItem();
    }

    @FXML
    private void clearFields() {
        cbCustomer.setValue(null);
        cbCustomer.hide();

        lblContact.setText("");
        lblAddress.setText("");
        lblName.setText("");
        btnClearCustomer.setVisible(false);
        btnAddUser.setVisible(true);
    }

    @FXML
    private void showAddCustomer() {
        customerEditController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(Customer customer) {
                if (customer != null) {
                    cbCustomer.setValue(customer);
                    lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
                    lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
                }
                isCustomerAdded = true;
                successAlert.setContentMessage(Global.MSG.SAVED).show(spMain);
                cbCustomer.hide();
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
    public void setLineItemEditController(InvoiceLineItemEditController lineItemEditController) {
        this.lineItemEditController = lineItemEditController;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

}
