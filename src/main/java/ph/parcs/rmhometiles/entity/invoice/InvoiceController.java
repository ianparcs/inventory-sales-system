package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
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
import ph.parcs.rmhometiles.util.SnackbarLayoutFactory;
import ph.parcs.rmhometiles.util.converter.NameConverter;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

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
    private JFXButton btnCreateInvoice;
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

    private final ReadOnlyObjectWrapper<Integer> total = new ReadOnlyObjectWrapper<>();

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

        refreshItems();
        ObjectBinding<Money> sum = new ObjectBinding<>() {

            @Override
            protected Money computeValue() {
                Money money = Money.parse("PHP 0.00");
                for (InvoiceLineItem item : tvInvoice.getItems()) {
                    money.plus(item.getAmount());
                }
                System.out.println("test");
                return money;
            }
        };
        total.bind(Bindings.createObjectBinding(() ->
                tvInvoice.getItems().stream().mapToInt(InvoiceLineItem::getQuantity).sum(), tvInvoice.getItems()));
        lblTax.textProperty().bind(total.asString());
    }


    private void refreshItems() {
        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) {
                for (InvoiceLineItem item : tvInvoice.getItems()) {
                    Product result = productService.findEntityById(item.getProduct().getId());
                    item.setProduct(result);
                }

                tvInvoice.refresh();
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
            return Bindings.createObjectBinding(() -> {
                Money subTotal = lineItem.getProduct().getPrice().multipliedBy(lineItem.getQuantity());
                lineItem.setAmount(subTotal);
                return subTotal;
            });
        });
    }

    private void setQuantityCellFactory() {
  /*      tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String s) {
                if (org.apache.commons.lang3.StringUtils.isNumeric(s)) {
                    return super.fromString(s);
                }
                return 0;
            }
        }));*/
        tcQty.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
        Platform.runLater(() -> cbCustomer.getItems().setAll(FXCollections.observableArrayList(customers)));
    }

    private void configureProductCombobox() {
        cbProducts.setConverter(new ProductConverter(cbProducts.getValue()));
        fillProductComboboxValues();
    }

    private void fillProductComboboxValues() {
        cbProducts.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> showProduct(keyTyped));
        cbProducts.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) showProduct(Global.STRING_EMPTY);
        });
    }

    private void showProduct(String query) {
        List<Product> products = productService.findEntities(query);
        cbProducts.show();
        Platform.runLater(() -> cbProducts.getItems().setAll(FXCollections.observableArrayList(products)));
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


    @FXML
    public void changeQuantity(TableColumn.CellEditEvent<InvoiceLineItem, Integer> event) {
        InvoiceLineItem lineItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
        int stocks = lineItem.getProduct().getStock().getStocks();
        int quantity = event.getNewValue();
        if (quantity > stocks) {
            showError("Quantity must not exceed stocks");
            lineItem.setQuantity(event.getOldValue());
            lineItem.quantityProperty().setValue(event.getOldValue());
        } else {
            lineItem.setQuantity(event.getNewValue());
            lineItem.quantityProperty().setValue(event.getNewValue());
        }
        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvInvoice.refresh();
        });
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
    private void clearCustomerDetails() {
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
