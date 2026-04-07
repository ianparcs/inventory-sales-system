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
import java.util.concurrent.Callable;
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
    private Label lblAmount;
    @FXML
    private Label lblAmountDueBalance;
    @FXML
    private Label lblTotalBeforeTax;
    @FXML
    private Label lblDiscountAmount;
    @FXML
    private StackPane spMain;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblAmountDue;
    @FXML
    private Label lblTax;

    @FXML
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
        askSaveAlert.setContentMessage("Checkout new order?");
        askSaveAlert.setHeaderMessage("Checkout");
        askSaveAlert.setConfirmButton("Yes");

        initNumberInputFormatter(tfDeliveryAmount);
        initNumberInputFormatter(tfCashPay);
        initColumnCellValueFactory();
        initInvoiceLabelProperties();
        initMoneyColumn(tcAmount);
        initMoneyColumn(tcPrice);
        initInvoiceProperties();
        initProductSearchBox();
        initDate();

        refreshItems();

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

    private void initInvoiceProperties() {
        tvOrders.getItems().forEach(item -> {
            item.amountProperty().addListener((obs, oldVal, newVal) -> {
                updateInvoiceDiscount();
            });
        });

        tvOrders.getItems().addListener((ListChangeListener<OrderItem>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (OrderItem item : change.getAddedSubList()) {
                        item.amountProperty().addListener((obs, oldVal, newVal) -> {
                            updateInvoiceDiscount();
                        });

                    }
                }
                if (change.wasRemoved()) {
                    updateInvoiceDiscount();
                }
            }
        });
    }

    private void updateInvoiceDiscount() {
        var totalOrderDiscountStream = tvOrders.getItems()
                .stream()
                .map(item -> item.getDiscount() != null ? item.getDiscount() : Money.parse("PHP 0.00"));

        var totalOrderAmountStream = tvOrders.getItems().stream()
                .map(item -> item.getAmount() != null ? item.getAmount() : Money.parse("PHP 0.00"));

        var totalOrderDiscountAmount = moneyService.computeTotalMoney(totalOrderDiscountStream);
        var totalOrderAmount = moneyService.computeTotalMoney(totalOrderAmountStream);

        invoice.setDiscountAmount(totalOrderDiscountAmount);
        invoice.setAmount(totalOrderAmount);
    }


    private void initNumberInputFormatter(JFXTextField textField) {
        textField.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches(AppConstant.Regex.DECIMAL)) {
                return change;
            }
            return null;
        }));
    }

    private void refreshItems() {
        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) {
                invoiceService.updateLineItems(tvOrders.getItems());
                Platform.runLater(() -> tvOrders.refresh());
            }
        });
    }

    private void initColumnCellValueFactory() {
        tcQty.setCellFactory(TextFieldTableCell.forTableColumn(new NumberConverter()));
        tcDiscountPercent.setCellFactory(TextFieldTableCell.forTableColumn(new FloatConverter()));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onItemDeleteAction, AppConstant.ActionType.DELETE));
        tcCode.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "code"));
        tcPrice.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "price"));
        tcStock.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().productProperty(), "stock", "stocks"));
    }

    private void initInvoiceLabelProperties() {
        btnSales.textProperty().bind(Bindings.createObjectBinding(this::changeCashPayTextButton, tfCashPay.textProperty()));

        lblTax.textProperty().bind(invoice.taxAmountProperty().asString());
        lblAmount.textProperty().bind(invoice.amountProperty().asString());
        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        lblDiscountAmount.textProperty().bind(invoice.discountAmountProperty().asString());
        lblAmountDue.textProperty().bind(invoice.changeProperty().asString());
        lblAmountDueBalance.textProperty().bind(Bindings.createObjectBinding(this::changeAmountDueLabel, lblAmountDue.textProperty()));
        lblTotalBeforeTax.textProperty().bind(Bindings.createObjectBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Money totalAmount = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : Money.parse("PHP 0.00");
                Money totalDiscount = invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : Money.parse("PHP 0.00");
                Money totalBeforeTax = totalAmount.plus(totalDiscount);

                return MoneyUtil.print(totalBeforeTax);
            }
        }, invoice.amountProperty(), invoice.discountAmountProperty()));
    }

    private String changeCashPayTextButton() {
        Money cashPay = moneyService.parseMoney(tfCashPay.getText());

        if (invoice.getTotalAmount() != null && cashPay.isLessThan(invoice.getTotalAmount())) {
            return "Add Payment";
        }
        return "Complete Sales";
    }

    private String changeAmountDueLabel() {
        if (invoice.changeProperty().get() != null) {
            if (invoice.changeProperty().get().isPositive()) {
                return "CHANGE";
            }
        }
        return "BALANCE";
    }

    private void initDate() {
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
                showError("Cannot have duplicate item");
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
            showError(validateMsg);
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
        OrderItem lineItem = event.getRowValue();
        int stocks = lineItem.getProduct().getStock().getStocks();
        int quantity = event.getNewValue();
        if (quantity > stocks) {
            showError(AppConstant.Message.QUANTITY_EXCEED);
            lineItem.setQuantity(event.getOldValue());
        } else {
            lineItem.setQuantity(event.getNewValue());
        }
        Platform.runLater(() -> {
            int index = tvOrders.getItems().indexOf(lineItem);
            tvOrders.getItems().remove(index);
            tvOrders.getItems().add(index, lineItem);
            tvOrders.refresh();
        });
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
