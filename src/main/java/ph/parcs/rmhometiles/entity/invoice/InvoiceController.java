package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.DateConverter;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerEditController;
import ph.parcs.rmhometiles.entity.customer.CustomerService;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.entity.user.UserService;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.NameConverter;
import ph.parcs.rmhometiles.util.SnackbarLayoutFactory;

import java.time.LocalDate;
import java.util.List;

@Controller
public class InvoiceController {

    @FXML
    private TableColumn<InvoiceLineItem, Integer> tcStock;
    @FXML
    private TableColumn<InvoiceLineItem, HBox> tcAction;
    @FXML
    private TableColumn<InvoiceLineItem, Integer> tcQty;
    @FXML
    private TableColumn<InvoiceLineItem, String> tcCode;
    @FXML
    private TableColumn<InvoiceLineItem, Money> tcPrice;
    @FXML
    private TableColumn<InvoiceLineItem, Money> tcTotal;
    @FXML
    private TableView<InvoiceLineItem> tvInvoice;
    @FXML
    private JFXComboBox<BaseEntity> cbCustomer;
    @FXML
    private JFXComboBox<BaseEntity> cbProducts;
    @FXML
    private JFXTextField lblDiscountSales;
    @FXML
    private JFXButton lblCreateInvoice;
    @FXML
    private JFXButton btnClearCustomer;
    @FXML
    private JFXTextField tfAmount;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXButton btnAddUser;
    @FXML
    private StackPane spMain;
    @FXML
    private Label lblSalesPerson;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblName;
    @FXML
    private Label lblTax;


    private EditItemController<Customer> customerEditController;
    private CustomerService customerService;
    private ProductService productService;
    private InvoiceService invoiceService;
    private UserService userService;

    @FXML
    public void initialize() {
        initColumnCellValueFactory();
        configureCustomerCombobox();
        configureProductCombobox();
        initInvoiceSummaryValue();
        initFieldValidation();
        initDate();


        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) {
                for (InvoiceLineItem item : tvInvoice.getItems()) {
                    Product result = productService.findEntityById(item.getProduct().getId());
                    item.setProduct(result);
                }
                tvInvoice.refresh();
            }
        });

        lblCreateInvoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }

    private void initColumnCellValueFactory() {
        tcCode.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "code"));
        tcPrice.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "price"));
        tcStock.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "stock", "stocks"));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction));

        setQuantityCellFactory();
        setTotalCellFactory();
    }

    private void setTotalCellFactory() {
        tcTotal.setCellValueFactory(cellData -> {
            InvoiceLineItem lineItem = cellData.getValue();
            return Bindings.createObjectBinding(() -> lineItem.getProduct().getPrice().multipliedBy(lineItem.getQuantity()));
        });
    }

    private void setQuantityCellFactory() {
        tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String s) {
                if (org.apache.commons.lang3.StringUtils.isNumeric(s)) {
                    return super.fromString(s);
                }
                return quantityOldValue;
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }));
    }

    private void initInvoiceSummaryValue() {
        User user = userService.getCurrentUser();
        lblSalesPerson.setText(user.getUsername());
    }

    private void initFieldValidation() {
        tfAmount.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                tfAmount.validate();
                if (tfAmount.getActiveValidator() != null && tfAmount.getActiveValidator().getHasErrors()) {
                    tfAmount.requestFocus();
                    String errorMessage = invoiceService.getAmountValidatorMessage(tfAmount.getActiveValidator());
                    showError(errorMessage);
                }
            }
        });
    }

    private void initDate() {
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new DateConverter());
    }

    private void configureCustomerCombobox() {
        cbCustomer.setConverter(new NameConverter(cbCustomer.getValue()));
        setCustomerComboboxFocusProperty();
        setCustomerComboboxValues();
    }

    private void setCustomerComboboxValues() {
        cbCustomer.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> {
            List<Customer> customers = customerService.findEntities(keyTyped);
            cbCustomer.show();
            Platform.runLater(() -> cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers)));
        });
    }

    private void setCustomerComboboxFocusProperty() {
        cbCustomer.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) {
                List<Customer> customers = customerService.findEntities(Global.STRING_EMPTY);
                cbCustomer.show();
                Platform.runLater(() -> cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers)));
            }
        });
    }

    private void configureProductCombobox() {
        cbProducts.setConverter(new StringConverter<>() {
            @Override
            public String toString(BaseEntity baseEntity) {
                Product product = (Product) baseEntity;
                if (baseEntity == null) return Global.STRING_EMPTY;
                return product.getCode();
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
        tvInvoice.getItems().add(new InvoiceLineItem(product));

        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvInvoice.refresh();
        });
    }

    private int quantityOldValue;

    @FXML
    public void changeQuantity(TableColumn.CellEditEvent<InvoiceLineItem, Integer> event) {
        this.quantityOldValue = event.getOldValue();

        InvoiceLineItem lineItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
        int stocks = lineItem.getProduct().getStock().getStocks();
        int quantity = event.getNewValue();
        if (quantity > stocks) {
            showError("Quantity must not exceed stocks");
            lineItem.setQuantity(event.getOldValue());
        } else {
            lineItem.setQuantity(event.getNewValue());
        }
        tvInvoice.refresh();
    }

    private void showError(String message) {
        JFXSnackbar snackbar = new JFXSnackbar(spMain);
        JFXSnackbarLayout snackbarLayout = SnackbarLayoutFactory.create(message);

        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                snackbarLayout, Duration.millis(3000)));
    }

    private InvoiceLineItem onItemDeleteAction(InvoiceLineItem item) {
        tvInvoice.getItems().remove(item);
        tvInvoice.refresh();
        return item;
    }

    @FXML
    private void clearFields() {
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
                    lblAddress.setText(StringUtils.isEmpty(customer.getAddress()) ? "n/a" : customer.getAddress());
                    lblContact.setText(StringUtils.isEmpty(customer.getContact()) ? "n/a" : customer.getContact());
                }

                SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
