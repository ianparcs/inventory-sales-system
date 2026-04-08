package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.inventory.stock.StockService;
import ph.parcs.rmhometiles.entity.money.MoneyService;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.order.OrderItemService;
import ph.parcs.rmhometiles.entity.payment.PaymentService;
import ph.parcs.rmhometiles.exception.AppException;
import ph.parcs.rmhometiles.exception.ErrorCode;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.MoneyUtil;
import ph.parcs.rmhometiles.util.ThreadUtil;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.converter.DateConverter;
import ph.parcs.rmhometiles.util.converter.FloatConverter;
import ph.parcs.rmhometiles.util.converter.NumberConverter;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
public class InvoiceController {

    @FXML
    private TableColumn<OrderItem, Money> tcOrderDiscountAmount;
    @FXML
    private TableColumn<OrderItem, Float> tcDiscountPercent;
    @FXML
    private TableColumn<OrderItem, Integer> tcStock;
    @FXML
    private TableColumn<OrderItem, HBox> tcAction;
    @FXML
    private TableColumn<OrderItem, Integer> tcQty;
    @FXML
    private TableColumn<OrderItem, Money> tcAmount;
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
    private Label lblSubTotal;
    @FXML
    private Label lblBalanceText;
    @FXML
    private Label lblTotalBeforeTax;
    @FXML
    private Label lblDiscountAmount;
    @FXML
    private StackPane spMain;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblBalance;
    @FXML
    private Label lblTax;
    @FXML
    private CustomerController customerController;

    private OrderItemService orderItemService;
    private ProductService productService;
    private PaymentService paymentService;
    private InvoiceService invoiceService;
    private MoneyService moneyService;
    private StockService stockService;
    private Invoice invoice;

    private SweetAlert checkoutAlert;

    @FXML
    public void initialize() {
        invoice = invoiceService.createDefault();
        invoice.setOrderItems(new HashSet<>(tvOrders.getItems()));

        checkoutAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);
        checkoutAlert.setContentMessage("Checkout new order?");
        checkoutAlert.setHeaderMessage("Checkout");
        checkoutAlert.setConfirmButton("Yes");

        initMoneyColumn(tcOrderDiscountAmount);
        initMoneyColumn(tcAmount);
        initMoneyColumn(tcPrice);

        bindInvoiceLabelProperties();
        initColumnCellValueFactory();
        initProductSearchBox();
        initTableViewOrder();
        initDateUI();

        customerController.setCustomer(null);
        customerController.setSpMain(spMain);
        tfCashPay.clear();
    }

    private void initMoneyColumn(TableColumn<OrderItem, Money> tableCell) {
        tableCell.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Money item, boolean empty) {
                if (!empty) {
                    setText(MoneyUtil.print(item));
                }
            }
        });
    }

    private void initDateUI() {
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new DateConverter());
    }

    private void initProductSearchBox() {
        cbProducts.setConverter(new ProductConverter(cbProducts.getValue()));
        cbProducts.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> showProduct(keyTyped));
        cbProducts.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) showProduct(AppConstant.STRING_EMPTY);
        });
    }

    private void initTableViewOrder() {
        tvOrders.getItems().forEach(item -> {
            item.amountProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) computeInvoiceTotals();
            });
            item.discountProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) computeInvoiceTotals();
            });
        });

        tvOrders.getItems().addListener((ListChangeListener<OrderItem>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (OrderItem item : change.getAddedSubList()) {
                        item.amountProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal != null) computeInvoiceTotals();
                        });
                        item.discountProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal != null) computeInvoiceTotals();
                        });
                    }
                }
                if (change.wasRemoved()) {
                    computeInvoiceTotals();
                }
            }
        });

        tfCashPay.textProperty().addListener((observable, oldValue, newValue) -> {
            computeInvoiceTotals();
        });

        tfCashPay.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));

    }

    private void initColumnCellValueFactory() {
        tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberConverter()));
        tcDiscountPercent.setCellFactory(TextFieldTableCell.forTableColumn(new FloatConverter()));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction, AppConstant.ActionType.DELETE));
        tcCode.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "code"));
        tcPrice.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "price"));
        tcStock.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "stock", "stocks"));
    }

    private void bindInvoiceLabelProperties() {
        lblTotalBeforeTax.textProperty().bind(Bindings.createObjectBinding(() -> {
            Money totalAmount = invoice.getSubTotalAmount() != null ? invoice.getSubTotalAmount() : Money.parse("PHP 0.00");
            Money totalDiscount = invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : Money.parse("PHP 0.00");
            Money totalBeforeTax = totalAmount.plus(totalDiscount);
            return MoneyUtil.print(totalBeforeTax);
        }, invoice.subTotalAmountProperty(), invoice.discountAmountProperty()));

        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        btnSales.textProperty().bind(Bindings.createObjectBinding(this::changeCashPayTextButton, tfCashPay.textProperty()));
        lblTax.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getTaxAmount()), invoice.taxAmountProperty()));
        lblBalanceText.textProperty().bind(Bindings.createStringBinding(this::changeBalanceLabel, invoice.balanceProperty(), tfCashPay.textProperty()));
        lblSubTotal.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getSubTotalAmount()), invoice.subTotalAmountProperty()));
        lblDiscountAmount.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getDiscountAmount()), invoice.discountAmountProperty()));
        lblBalance.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getBalance()), invoice.balanceProperty(), tfCashPay.textProperty()));
    }

    private void unBindInvoiceLabelProperties() {
        lblTotalBeforeTax.textProperty().unbind();
        lblDiscountAmount.textProperty().unbind();
        lblBalanceText.textProperty().unbind();
        lblTotalAmount.textProperty().unbind();
        lblBalance.textProperty().unbind();
        lblSubTotal.textProperty().unbind();
        btnSales.textProperty().unbind();
        lblTax.textProperty().unbind();
    }

    private void computeInvoiceTotals() {
        var totalOrderDiscountStream = tvOrders.getItems().stream().map(item -> item.getDiscount() != null ? item.getDiscount() : Money.parse("PHP 0.00"));
        var totalOrderAmountStream = tvOrders.getItems().stream().map(item -> item.getAmount() != null ? item.getAmount() : Money.parse("PHP 0.00"));

        var cashPaid = moneyService.parseMoney(tfCashPay.getText());
        var deliveryRate = moneyService.parseMoney(tfDeliveryAmount.getText());

        var subTotal = moneyService.computeTotalMoney(totalOrderAmountStream);
        var discount = moneyService.computeTotalMoney(totalOrderDiscountStream);

        var tax = moneyService.computeTax(subTotal);
        var total = moneyService.computeTotalAmount(subTotal, tax, discount, deliveryRate);

        var balance = moneyService.computeBalance(total, cashPaid);

        invoice.setDiscountAmount(discount);
        invoice.setSubTotalAmount(subTotal);
        invoice.setTotalAmount(total);
        invoice.setBalance(balance);
        invoice.setTaxAmount(tax);
    }

    private String changeCashPayTextButton() {
        Money cashPay = moneyService.parseMoney(tfCashPay.getText());

        if (invoice.getTotalAmount() != null && cashPay.isLessThan(invoice.getTotalAmount())) {
            return "Add Payment";
        }
        return "Complete Sales";
    }

    private String changeBalanceLabel() {
        var balance = invoice.getBalance();
        if (balance != null && balance.isPositive()) return "CHANGE";
        return "BALANCE";
    }

    private void showProduct(String query) {
        List<Product> products = productService.findEntities(query);
        cbProducts.show();
        Platform.runLater(() -> cbProducts.getItems().setAll(FXCollections.observableArrayList(products)));
    }

    @FXML
    private void onProductItemClicked() {
        Product product = cbProducts.getValue();
        if (product == null) return;

        try {
            var isOrderDuplicate = orderItemService.isOrderDuplicate(tvOrders.getItems(), product.getCode());
            if (isOrderDuplicate) throw new AppException(ErrorCode.ORDER_DUPLICATE);

            tvOrders.getItems().add(new OrderItem(product));
            Platform.runLater(() -> {
                cbProducts.valueProperty().set(null);
                cbProducts.hide();
                spMain.requestFocus();
                tvOrders.refresh();
            });

        } catch (AppException e) {
            showInvoiceError(e.getMessage());
        }
    }

    @FXML
    private void onCheckout() {
        try {
            validateInvoiceCheckout();
        } catch (AppException e) {
            showInvoiceError(e.getMessage());
            return;
        }

        checkoutAlert.setConfirmListener(() -> {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(() -> {
                productService.saveInvoiceProduct(tvOrders.getItems());
                invoiceService.saveOrderItem(invoice, tvOrders.getItems());

                var payment = paymentService.createPayment(rbCashType.isSelected(), invoice);

                invoice.setCreatedAt(LocalDateTime.now());
                invoice.setOrderItems(new HashSet<>(tvOrders.getItems()));
                invoice.setName("INV-" + dpDate.getValue() + "-ID" + 1);
                invoice.setCustomer(customerController.getCustomer());
                invoice.setRemarks(txaRemarks.getText());
                invoice.addPayments(payment);

                Invoice savedInvoice = invoiceService.saveEntity(invoice);
                Platform.runLater(() -> {
                    if (savedInvoice != null) {
                        SweetAlert successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
                        successAlert.setContentMessage(AppConstant.Message.SAVED).show(spMain);
                        clearAllInvoice();
                    }
                });
            });
            ThreadUtil.shutdownAndAwaitTermination(executorService);
        }).show(spMain);
    }

    private void validateInvoiceCheckout() throws AppException {
        if (tfCashPay.getText().isEmpty()) throw new AppException(ErrorCode.AMOUNT_IS_REQUIRED);
        if (customerController.getCustomer() == null) throw new AppException(ErrorCode.CUSTOMER_IS_REQUIRED);

        var isOrderQuantityValid = orderItemService.isOrderQuantityValid(tvOrders.getItems());
        if (tvOrders.getItems().isEmpty()) throw new AppException(ErrorCode.ORDER_IS_REQUIRED);
        if (!isOrderQuantityValid) throw new AppException(ErrorCode.ORDER_QUANTITY_IS_REQUIRED);
    }

    @FXML
    public void onEditInvoiceQuantity(TableColumn.CellEditEvent<OrderItem, Integer> event) {
        OrderItem item = event.getRowValue();
        int newQty = event.getNewValue();
        int oldQty = event.getOldValue();

        boolean outOfStock = !stockService.hasStock(item.getProduct(), newQty);

        if (outOfStock) {
            item.setQuantity(oldQty);
            showInvoiceError(AppConstant.Message.QUANTITY_EXCEED);
        } else {
            item.setQuantity(newQty);
        }
        Platform.runLater(tvOrders::refresh);
    }

    @FXML
    public void onDiscountPercentEditCommit(TableColumn.CellEditEvent<OrderItem, Float> event) {
        OrderItem lineItem = event.getRowValue();
        if (lineItem != null) {
            BigDecimal discPercent = new BigDecimal(event.getNewValue());
            BigDecimal multiplier = discPercent.divide(BigDecimal.valueOf(100));

            Money totalAmount = lineItem.getProduct().getPrice().multipliedBy(lineItem.getQuantity());
            Money discountAmount = totalAmount.multipliedBy(multiplier, RoundingMode.UNNECESSARY);

            lineItem.setDiscount(discountAmount);
            lineItem.setDiscountPercent(event.getNewValue());
        }
    }

    @FXML
    public void onCashTypeClicked() {
        rbGCashType.setSelected(false);
    }

    @FXML
    public void onGCashTypeClicked() {
        rbCashType.setSelected(false);
    }

    @FXML
    private OrderItem onItemDeleteAction(OrderItem item) {
        if (item != null && tvOrders.getItems().contains(item)) {
            tvOrders.getItems().remove(item);
            tvOrders.refresh();
        }
        return item;
    }

    @FXML
    public void clearAllInvoice() {
        clearInvoiceBillingDetails();
        clearCustomer();
        initDateUI();

        unBindInvoiceLabelProperties();
        invoice = invoiceService.createDefault();
        bindInvoiceLabelProperties();

        tvOrders.getItems().clear();
    }

    private void showInvoiceError(String message) {
        SweetAlert sweetAlert = SweetAlertFactory.create(SweetAlert.Type.DANGER, message);
        sweetAlert.show(spMain);
    }

    private void clearInvoiceBillingDetails() {
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

    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    @Autowired
    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
