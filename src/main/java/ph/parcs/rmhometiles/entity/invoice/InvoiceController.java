package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.MoneyService;
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
import ph.parcs.rmhometiles.util.converter.DateConverter;
import ph.parcs.rmhometiles.util.converter.NameConverter;
import ph.parcs.rmhometiles.util.converter.NumberConverter;
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
    private TableView<InvoiceLineItem> tvInvoice;
    @FXML
    private JFXComboBox<BaseEntity> cbCustomer;
    @FXML
    private JFXComboBox<BaseEntity> cbProducts;
    @FXML
    private JFXTextField tfDiscountPercent;
    @FXML
    private JFXTextField tfDeliveryAmount;
    @FXML
    private JFXButton btnCreateInvoice;
    @FXML
    private JFXButton btnClearCustomer;
    @FXML
    private JFXTextField tfCashPay;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private JFXButton btnAddUser;
    @FXML
    private Label lblAmount;
    @FXML
    private StackPane spMain;
    @FXML
    private Label lblDiscountAmount;
    @FXML
    private Label lblSalesPerson;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblAmountDue;
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
    private MoneyService moneyService;
    private UserService userService;

    private Invoice invoice;

    @FXML
    public void initialize() {
        invoice = new Invoice();
        invoice.setInvoiceLineItems(tvInvoice.getItems());

        setDiscountPercentFormatter();
        initColumnCellValueFactory();
        configureCustomerCombobox();
        configureProductCombobox();
        showInvoiceSummary();
        initFieldValidation();
        initDate();

        refreshItems();

        invoice.amountProperty().bind(Bindings.createObjectBinding(this::showItemLineAmounts, tvInvoice.getItems()));
        invoice.discountProperty().bind(Bindings.createObjectBinding(this::showDiscountAmount, tfDiscountPercent.textProperty(), invoice.amountProperty()));
        invoice.totalAmountDueProperty().bind(Bindings.createObjectBinding(this::showTotalAmountDue, invoice.amountProperty(), tfCashPay.textProperty()));

    }

    private void setDiscountPercentFormatter() {
        tfDiscountPercent.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches(Global.Regex.DECIMAL_PERCENT)) {
                return change;
            }
            return null;
        }));
        setDiscountPercentTextBehavior();
    }

    private void setDiscountPercentTextBehavior() {
        tfDiscountPercent.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (outOfFocus) {
                String discountText = tfDiscountPercent.getText();
                if (!discountText.endsWith(Global.Unit.PERCENT)) {
                    tfDiscountPercent.setText(discountText + Global.Unit.PERCENT);
                }
            }
        });
    }

    @SneakyThrows
    private Money showDiscountAmount() {
        Money amount = invoice.getAmount();
        Number discountPercent = moneyService.getDiscountPercent(tfDiscountPercent.getText());
        return moneyService.computeDiscountAmount(amount, discountPercent);
    }

    private Money showTotalAmountDue() {
        String text = tfCashPay.getText();
        String value = "0.00";
        if (!StringUtils.isEmpty(text) && org.apache.commons.lang3.StringUtils.isNumeric(text)) {
            value = tfCashPay.getText();
        }

        return invoice.amountProperty().get().minus(Money.parse("PHP" + " " + value));
    }

    private Money showItemLineAmounts() {
        return tvInvoice.getItems().stream()
                .map(InvoiceLineItem::getAmount)
                .reduce(Money.parse("PHP 0.00"), Money::plus);
    }

    private void refreshItems() {
        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) {
                invoiceService.updateLineItems(tvInvoice.getItems());
                tvInvoice.refresh();
            }
        });
    }

    private void initColumnCellValueFactory() {
        tcCode.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "code"));
        tcPrice.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "price"));
        tcStock.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "stock", "stocks"));
        tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberConverter()));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction));

    }

    private void showInvoiceSummary() {
        User user = userService.getCurrentUser();
        lblSalesPerson.setText(user.getUsername());
        lblAmount.textProperty().bind(invoice.amountProperty().asString());
        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        lblDiscountAmount.textProperty().bind(invoice.discountProperty().asString());
        lblAmountDue.textProperty().bind(invoice.totalAmountDueProperty().asString());
    }

    private void initFieldValidation() {
        tfCashPay.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                tfCashPay.validate();
                if (tfCashPay.getActiveValidator() != null && tfCashPay.getActiveValidator().getHasErrors()) {
                    tfCashPay.requestFocus();
                    String errorMessage = getAmountValidatorMessage(tfCashPay.getActiveValidator());
                    showError(errorMessage);
                }
            }
        });
    }

    public String getAmountValidatorMessage(ValidatorBase activeValidator) {
        String validatorMessage = "Please input two decimal digits only";
        if (activeValidator instanceof NumberValidator) {
            validatorMessage = "Please enter numerical value only";
        } else if (activeValidator instanceof RequiredFieldValidator) {
            validatorMessage = "Please enter an amount";
        }
        return validatorMessage;
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
    private void onProductItemClick() {
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
    public void onQuantityEditCommit(TableColumn.CellEditEvent<InvoiceLineItem, Integer> event) {
        InvoiceLineItem lineItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
        int stocks = lineItem.getProduct().getStock().getStocks();
        int quantity = event.getNewValue();
        if (quantity > stocks) {
            showError(Global.Message.QUANTITY_EXCEED);
            lineItem.setQuantity(event.getOldValue());
        } else {
            lineItem.setQuantity(event.getNewValue());
        }
        if (tvInvoice.getItems().contains(lineItem)) {
            int index = tvInvoice.getItems().indexOf(lineItem);
            tvInvoice.getItems().remove(lineItem);
            tvInvoice.getItems().add(index, lineItem);
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
                successAlert.setContentMessage(Global.Message.SAVED).show(spMain);
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
    public void setMoneyService(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
