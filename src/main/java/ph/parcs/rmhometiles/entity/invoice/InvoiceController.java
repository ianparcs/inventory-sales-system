package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import ph.parcs.rmhometiles.entity.payment.Payment;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.converter.DateConverter;
import ph.parcs.rmhometiles.util.converter.NumberConverter;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
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
    private JFXComboBox<String> cbPaymentType;
    @FXML
    private JFXComboBox<Product> cbProducts;
    @FXML
    private TableView<OrderItem> tvOrders;
    @FXML
    private JFXTextField tfDiscountPercent;
    @FXML
    private JFXTextField tfDeliveryAmount;
    @FXML
    private JFXRadioButton rbGCashType;
    @FXML
    private JFXRadioButton rbCashType;
    @FXML
    private JFXTextField tfCashPay;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private TextArea txaRemarks;
    @FXML
    private JFXButton btnSales;
    @FXML
    private Label lblAmount;
    @FXML
    private Label lblAmountDueBalance;
    @FXML
    private Label lblTotalBeforeTax;
    @FXML
    private Label lblDiscountAmount;
    @FXML
    private Label lblSalesPerson;
    @FXML
    private StackPane spMain;
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

    private SweetAlert askSaveAlert;

    @FXML
    public void initialize() {
        invoice = new Invoice();
        invoice.setOrderItems(new HashSet<>(tvOrders.getItems()));

        askSaveAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);
        askSaveAlert.setContentMessage("Create new invoice?");
        askSaveAlert.setHeaderMessage("Checkout");
        askSaveAlert.setConfirmButton("Yes");

        initNumberInputFormatter(tfDeliveryAmount);
        initNumberInputFormatter(tfCashPay);
        initDiscountNumberFormatter();
        initColumnCellValueFactory();
        initInvoiceLabelProperties();
        initProductSearchBox();
        initInvoiceProperties();
        initDate();

        refreshItems();

        customerController.setCustomer(null);
        customerController.setSpMain(spMain);
        tfCashPay.clear();
    }

    private void initInvoiceProperties() {
        invoice.statusProperty().bind(Bindings.createStringBinding(() -> invoiceService.setInvoiceStatus(invoice.totalAmountDueProperty().get()), invoice.totalAmountDueProperty().asString()));

        invoice.amountProperty().bind(Bindings.createObjectBinding(this::showItemLineAmounts, tvOrders.getItems()));
        invoice.discountProperty().bind(Bindings.createObjectBinding(this::showDiscountAmount, tfDiscountPercent.textProperty()));
        invoice.taxAmountProperty().bind(Bindings.createObjectBinding(this::showTaxAmount, invoice.amountProperty()));
        invoice.totalAmountDueProperty().bind(Bindings.createObjectBinding(this::showTotalAmountDue, invoice.totalAmountProperty(), tfCashPay.textProperty()));
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
        final int MAX_LENGTH = 6;
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (textField.getText().length() >= MAX_LENGTH) {
                    textField.setText(textField.getText().substring(0, MAX_LENGTH));
                }
            }
        });
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
        return tvOrders.getItems().stream()
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
        Money totalAmount = moneyService.computeTotalAmount(currentTotal, taxAmount, deliveryRate);
        tfCashPay.setText(totalAmount.getAmount().toString());
        return totalAmount;
    }

    private Money showTotalAmountDue() {
        String cashPayText = tfCashPay.getText();
        Money cashPaid = Money.parse(cashPayText.isEmpty() ? "PHP 0.00" : Global.Unit.CURRENCY + " " + cashPayText);
        return moneyService.computeTotalAmountDue(invoice.getTotalAmount(), cashPaid);
    }

    private void refreshItems() {
        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) {
                invoiceService.updateLineItems(tvOrders.getItems());
                tvOrders.refresh();
            }
        });
    }

    private void initColumnCellValueFactory() {
        tcCode.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "code"));
        tcPrice.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "price"));
        tcStock.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "stock", "stocks"));
        tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberConverter()));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction,"DELETE"));

    }

    private void initInvoiceLabelProperties() {
        btnSales.textProperty().bind(Bindings.createObjectBinding(this::changeCashPayTextButton, tfCashPay.textProperty()));

        lblTax.textProperty().bind(invoice.taxAmountProperty().asString());
        lblAmount.textProperty().bind(invoice.amountProperty().asString());
        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        lblDiscountAmount.textProperty().bind(invoice.discountProperty().asString());
        lblAmountDue.textProperty().bind(invoice.totalAmountDueProperty().asString());
        lblAmountDueBalance.textProperty().bind(Bindings.createObjectBinding(this::changeAmountDueLabel, lblAmountDue.textProperty()));
        lblTotalBeforeTax.textProperty().bind(Bindings.createObjectBinding(this::showTotalBeforeTax, invoice.amountProperty(), tfDiscountPercent.textProperty()).asString());
    }

    private String changeCashPayTextButton() {
        Money cashPay = moneyService.parseMoney(tfCashPay.getText());

        if(invoice.getTotalAmount() != null && cashPay.isLessThan(invoice.getTotalAmount())){
            return "Add Payment";
        }
        return "Complete Sales";
    }

    private String changeAmountDueLabel() {
        if (invoice.totalAmountDueProperty().get() != null) {
            if (invoice.totalAmountDueProperty().get().isPositive()) {
                return "Change";
            }
        }
        return "Balance";
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

        for (OrderItem item : tvOrders.getItems()) {
            if (item.getProduct().getCode().equalsIgnoreCase(product.getCode())) {
                showError("Cannot have duplicate item");
                return;
            }
        }

        tvOrders.getItems().add(new OrderItem(product));
        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvOrders.refresh();
        });
    }

    @FXML
    private void onCheckout() {
        String validateMsg = validateCheckout();
        if (!validateMsg.isEmpty()) {
            showError(validateMsg);
            return;
        }

        askSaveAlert.setConfirmListener(() -> {
            for (OrderItem item : tvOrders.getItems()) {
                productService.saveEntity(item.getProduct());
            }

            invoiceService.saveOrderItem(invoice, tvOrders.getItems());

            String paymentType = invoiceService.getPaymentType(rbCashType.isSelected(),rbGCashType.isSelected());

            LocalDateTime createdAt = dpDate.getValue().atTime(LocalTime.now());
            Payment payment = new Payment();
            payment.setInvoice(invoice);
            payment.setCreatedAt(createdAt);
            payment.setPaymentType(paymentType);
            payment.setPaymentAmount(invoice.getAmount());

            invoice.setCreatedAt(createdAt);
            invoice.setOrderItems(new HashSet<>(tvOrders.getItems()));
            invoice.setName("INV-" + dpDate.getValue() + "-ID" + 1);
            invoice.setCustomer(customerController.getCustomer());
            invoice.setRemarks(txaRemarks.getText());
            invoice.addPayments(payment);

            Invoice savedInvoice = invoiceService.saveEntity(invoice);

            if (savedInvoice != null) {
                SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
                successAlert.setContentMessage(Global.Message.SAVED).show(spMain);
                clearAllInvoice();
            }
        }).show(spMain);
    }

    private String validateCheckout() {
        if (tfCashPay.getText().isEmpty()) {
            tfCashPay.getValidators().add(new RequiredFieldValidator());
            tfCashPay.requestFocus();
            return "Please enter an amount";
        }

        Customer customer = customerController.getCustomer();
        if (customer == null) {
            return Global.Message.ENTER_CUSTOMER;
        }

        OrderItem item = productService.checkQuantity(tvOrders.getItems());

        if (item != null) return "Please enter quantity for item " + item.getProduct().getCode();
        if (tvOrders.getItems().isEmpty()) return "Please put an order(s)";

        return "";
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
        if (tvOrders.getItems().contains(lineItem)) {
            int index = tvOrders.getItems().indexOf(lineItem);
            tvOrders.getItems().remove(lineItem);
            tvOrders.getItems().add(index, lineItem);
        }
        tvOrders.refresh();
    }

    @FXML
    public void onCashTypeClicked() {
        rbGCashType.setSelected(false);
    }

    @FXML
    public void onGCashTypeClicked() {
        rbCashType.setSelected(false);
    }

    private OrderItem onItemDeleteAction(OrderItem item) {
        tvOrders.getItems().remove(item);
        tvOrders.refresh();
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
        tvOrders.getItems().clear();

        initInvoiceProperties();
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
