package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
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
import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.MoneyService;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.converter.DateConverter;
import ph.parcs.rmhometiles.util.converter.NumberConverter;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class InvoiceController {

    @FXML
    private TableColumn<OrderItem, Integer> tcStock;
    @FXML
    private TableColumn<OrderItem, HBox> tcAction;
    @FXML
    private TableColumn<OrderItem, Integer> tcQty;
    @FXML
    private TableColumn<OrderItem, String> tcCode;
    @FXML
    private TableColumn<OrderItem, Money> tcPrice;
    @FXML
    private TableView<OrderItem> tvInvoice;
    @FXML
    private JFXComboBox<Product> cbProducts;
    @FXML
    private JFXTextField tfDiscountPercent;
    @FXML
    private JFXTextField tfDeliveryAmount;
    @FXML
    private JFXTextField tfCashPay;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private Label lblAmount;
    @FXML
    private StackPane spMain;
    @FXML
    private Label lblTotalBeforeTax;
    @FXML
    private Label lblDiscountAmount;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblAmountDue;
    @FXML
    private Label lblTax;

    private CustomerController customerController;
    private ProductService productService;
    private InvoiceService invoiceService;
    private MoneyService moneyService;
    private Invoice invoice;

    @FXML
    public void initialize() {
        invoice = new Invoice();
        invoice.setOrderItems(tvInvoice.getItems());

        initNumberInputFormatter(tfDeliveryAmount);
        initNumberInputFormatter(tfCashPay);
        initDiscountNumberFormatter();
        initColumnCellValueFactory();
        initProductSearchBox();
        initFieldValidation();
        initInvoiceProperty();
        initInvoiceProperties();
        initDate();

        refreshItems();

        customerController.setCustomer(null);
        customerController.setSpMain(spMain);
        tfCashPay.clear();
    }

    private void initInvoiceProperty() {
        invoice.amountProperty().bind(Bindings.createObjectBinding(this::showItemLineAmounts, tvInvoice.getItems()));
        invoice.discountProperty().bind(Bindings.createObjectBinding(this::showDiscountAmount, tfDiscountPercent.textProperty(), invoice.amountProperty()));
        invoice.taxAmountProperty().bind(Bindings.createObjectBinding(this::showTaxAmount, tfDiscountPercent.textProperty(), invoice.amountProperty()));
        invoice.totalAmountDueProperty().bind(Bindings.createObjectBinding(this::showTotalAmountDue, invoice.amountProperty(), tfCashPay.textProperty()));
        invoice.totalAmountProperty().bind(Bindings.createObjectBinding(this::showTotalAmount, invoice.amountProperty(), tfDeliveryAmount.textProperty(), tfDiscountPercent.textProperty()));
    }

    private void initDiscountNumberFormatter() {
        tfDiscountPercent.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches(Global.Regex.DECIMAL_PERCENT)) {
                return change;
            }
            return null;
        }));
        setDiscountPercentTextBehavior();
    }

    private void initNumberInputFormatter(JFXTextField textField) {
        textField.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches(Global.Regex.DECIMAL)) {
                return change;
            }
            return null;
        }));

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

    private Money showItemLineAmounts() {
        return tvInvoice.getItems().stream()
                .map(OrderItem::getAmount)
                .reduce(Money.parse("PHP 0.00"), Money::plus);
    }

    @SneakyThrows
    private Money showDiscountAmount() {
        Money amount = invoice.getAmount();
        if (StringUtils.isEmpty(tfDiscountPercent.getText())) return Money.parse("PHP 0.00");
        return moneyService.computeDiscount(amount, tfDiscountPercent.getText());
    }

    private Money showTotalBeforeTax() {
        if (invoice.getAmount() == null) return Money.parse("PHP 0.00");
        return invoice.getAmount().minus(showDiscountAmount());
    }

    @SneakyThrows
    private Money showTaxAmount() {
        Money totalBeforeTax = showTotalBeforeTax();
        return moneyService.computeDiscount(totalBeforeTax, Global.TAX);
    }

    private Money showTotalAmount() {
        Money currentTotal = showTotalBeforeTax();
        Money taxAmount = moneyService.computeDiscount(currentTotal, Global.TAX);
        Money deliveryRate = Money.parse(Global.Unit.CURRENCY + " " + (tfDeliveryAmount.getText().isEmpty() ? "0.00" : tfDeliveryAmount.getText()));
        return moneyService.computeTotalAmount(currentTotal, taxAmount, deliveryRate);
    }

    private Money showTotalAmountDue() {
        String cashPayText = tfCashPay.getText();
        Money cashPaid = Money.parse(cashPayText.isEmpty() ? "PHP 0.00" : Global.Unit.CURRENCY + " " + cashPayText);
        return moneyService.computeTotalAmountDue(invoice.getTotalAmount(), cashPaid);
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

    private void initInvoiceProperties() {
        lblTax.textProperty().bind(invoice.taxAmountProperty().asString());
        lblAmount.textProperty().bind(invoice.amountProperty().asString());
        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        lblDiscountAmount.textProperty().bind(invoice.discountProperty().asString());
        lblAmountDue.textProperty().bind(invoice.totalAmountDueProperty().asString());
        lblTotalBeforeTax.textProperty().bind(Bindings.createObjectBinding(this::showTotalBeforeTax, invoice.amountProperty(), tfDiscountPercent.textProperty()).asString());
    }

    private void initFieldValidation() {
   /*     tfCashPay.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                tfCashPay.validate();
                if (tfCashPay.getActiveValidator() != null && tfCashPay.getActiveValidator().getHasErrors()) {
                    tfCashPay.requestFocus();
                    String errorMessage = getAmountValidatorMessage(tfCashPay.getActiveValidator());
                    showError(errorMessage);
                }
            }
        });*/
    }

    private String getAmountValidatorMessage(ValidatorBase activeValidator) {
        String validatorMessage = "";
        if (activeValidator instanceof NumberValidator) {
            validatorMessage = "Please enter numerical value only";
        } else if (activeValidator instanceof RequiredFieldValidator) {
            validatorMessage = "Please enter an amount";
        } else if (activeValidator instanceof RegexValidator) {
            validatorMessage = "Please input two decimal digits only";
        }
        return validatorMessage;
    }

    private void initDate() {
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new DateConverter());
    }

    private void initProductSearchBox() {
        cbProducts.setConverter(new ProductConverter(cbProducts.getValue()));
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
    private void onProductItemClick() {
        Product product = cbProducts.getValue();
        if (product == null) return;

        for (OrderItem item : tvInvoice.getItems()) {
            if (item.getProduct().getCode().equalsIgnoreCase(product.getCode())){
                showError("Cannot have duplicate item");
                return;
            }
        }

        tvInvoice.getItems().add(new OrderItem(product));
        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvInvoice.refresh();
        });
    }

    @FXML
    private void onCheckout() {
        if (tfCashPay.getText().isEmpty()) {
            tfCashPay.getValidators().add(new RequiredFieldValidator());
            tfCashPay.requestFocus();
            showError("Please enter an amount");
            return;
        }
        Customer customer = customerController.getCustomer();
        if (customer == null) {
            SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.DANGER);
            successAlert.setContentMessage(Global.Message.ENTER_CUSTOMER).show(spMain);
            return;
        }

        invoice.setCustomer(customer);
        invoice.setOrderItems(tvInvoice.getItems());
        invoice.setName("INV-" + dpDate.getValue() + "-ID" + 1);
        invoice.setCreatedAt(dpDate.getValue().atTime(LocalTime.now()));

        Invoice savedInvoice = invoiceService.saveEntity(invoice);
        if (savedInvoice != null) {
            SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
            successAlert.setContentMessage(Global.Message.SAVED).show(spMain);
        }
    }

    @FXML
    public void onQuantityEditCommit(TableColumn.CellEditEvent<OrderItem, Integer> event) {
        OrderItem lineItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
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

    private OrderItem onItemDeleteAction(OrderItem item) {
        tvInvoice.getItems().remove(item);
        tvInvoice.refresh();
        return item;
    }

    private void showError(String message) {
     SweetAlert sweetAlert = SweetAlertFactory.create(SweetAlert.Type.DANGER, message);
     sweetAlert.show(spMain);
    }

    public void clearAllInvoice() {
        clearCustomer();
        clearInvoiceSummary();

        initDate();

        invoice = new Invoice();
        tvInvoice.getItems().clear();

        initInvoiceProperty();
    }

    private void clearInvoiceSummary() {
        tfDiscountPercent.clear();
        tfDeliveryAmount.clear();
        tfCashPay.clear();
    }

    private void clearCustomer() {
        customerController.setCustomer(null);
        customerController.clearCustomerDetails();
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
    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

}
