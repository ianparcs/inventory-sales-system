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
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.inventory.stock.StockService;
import ph.parcs.rmhometiles.entity.money.MoneyService;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.payment.Payment;
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
    private Label lblChangeDue;
    @FXML
    private Label lblTax;
    @FXML
    private CustomerController customerController;

    private ProductService productService;
    private InvoiceService invoiceService;
    private MoneyService moneyService;
    private StockService stockService;
    private Invoice invoice;

    private SweetAlert askSaveAlert;

    @FXML
    public void initialize() {
        invoice = invoiceService.createDefault();
        invoice.setOrderItems(new HashSet<>(tvOrders.getItems()));

        askSaveAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);
        askSaveAlert.setContentMessage("Checkout new order?");
        askSaveAlert.setHeaderMessage("Checkout");
        askSaveAlert.setConfirmButton("Yes");

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

    protected void initMoneyColumn(TableColumn<OrderItem, Money> tableCell) {
        tableCell.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Money item, boolean empty) {
                if (!empty) {
                    setText(MoneyUtil.print(item));
                }
            }
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

        tfCashPay.textProperty().addListener((observable, oldValue, newValue) -> computeInvoiceTotals());
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

        var changeDue = moneyService.computeMoneyChange(total, cashPaid);
        var balance = moneyService.computeBalance(total, cashPaid);

        invoice.setDiscountAmount(discount);
        invoice.setSubTotalAmount(subTotal);
        invoice.setTotalAmount(total);
        invoice.setTaxAmount(tax);
        invoice.setChangeDue(changeDue);
        invoice.setBalance(balance);
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
        lblBalanceText.textProperty().bind(Bindings.createStringBinding(this::changeBalanceLabel, tfCashPay.textProperty()));
        lblTax.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getTaxAmount()), invoice.taxAmountProperty()));
        lblChangeDue.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getChangeDue()), tfCashPay.textProperty()));
        lblSubTotal.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getSubTotalAmount()), invoice.subTotalAmountProperty()));
        lblDiscountAmount.textProperty().bind(Bindings.createStringBinding(() -> MoneyUtil.print(invoice.getDiscountAmount()), invoice.discountAmountProperty()));
    }

    private void unBindInvoiceLabelProperties() {
        lblTotalBeforeTax.textProperty().unbind();
        lblDiscountAmount.textProperty().unbind();
        lblBalanceText.textProperty().unbind();
        lblTotalAmount.textProperty().unbind();
        lblChangeDue.textProperty().unbind();
        lblSubTotal.textProperty().unbind();
        btnSales.textProperty().unbind();
        lblTax.textProperty().unbind();
    }

    private String changeCashPayTextButton() {
        Money cashPay = moneyService.parseMoney(tfCashPay.getText());

        if (invoice.getTotalAmount() != null && cashPay.isLessThan(invoice.getTotalAmount())) {
            return "Add Payment";
        }
        return "Complete Sales";
    }

    private String changeBalanceLabel() {
        var changeDue = invoice.getChangeDue();
        if (changeDue != null && changeDue.isPositive()) return "CHANGE";
        return "BALANCE";
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
                showInvoiceError("Cannot have duplicate item");
                return;
            }
        }

        OrderItem orderItem = new OrderItem(product);

        tvOrders.getItems().add(orderItem);
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
            showInvoiceError(validateMsg);
            return;
        }

        askSaveAlert.setConfirmListener(() -> {

            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(() -> {
                productService.saveInvoiceProduct(tvOrders.getItems());
                invoiceService.saveOrderItem(invoice, tvOrders.getItems());

                String paymentType = invoiceService.getPaymentType(rbCashType.isSelected(), rbGCashType.isSelected());
                LocalDateTime createdAt = dpDate.getValue().atTime(LocalTime.now());
                Payment payment = new Payment();
                payment.setInvoice(invoice);
                payment.setCreatedAt(createdAt);
                payment.setPaymentType(paymentType);
                payment.setPaymentAmount(moneyService.parseMoney(tfCashPay.getText()));

                invoice.setCreatedAt(createdAt);
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

    private String validateCheckout() {
        if (tfCashPay.getText().isEmpty()) {
            return "Please enter an amount";
        }

        Customer customer = customerController.getCustomer();

        if (customer == null) {
            return AppConstant.Message.ENTER_CUSTOMER;
        }

        OrderItem item = productService.checkQuantity(tvOrders.getItems());

        if (item != null) return "Please enter quantity for item " + item.getProduct().getCode();
        if (tvOrders.getItems().isEmpty()) return "Please put an order(s)";

        return "";
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

}
