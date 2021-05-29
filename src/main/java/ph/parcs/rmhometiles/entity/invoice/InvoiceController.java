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
import ph.parcs.rmhometiles.entity.MoneyService;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.SnackbarLayoutFactory;
import ph.parcs.rmhometiles.util.converter.DateConverter;
import ph.parcs.rmhometiles.util.converter.NumberConverter;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private CustomerController invoiceCustomerController;
    private ProductService productService;
    private InvoiceService invoiceService;
    private MoneyService moneyService;
    private Invoice invoice;

    @FXML
    public void initialize() {
        invoice = new Invoice();
        invoice.setInvoiceLineItems(tvInvoice.getItems());

        setNumberInputFormatter(tfDeliveryAmount);
        setNumberInputFormatter(tfCashPay);

        setDiscountPercentInputValidation();
        initColumnCellValueFactory();
        configureProductCombobox();
        showInvoiceSummary();

        initFieldValidation();
        initDate();

        refreshItems();
        bindInvoiceFields();

        invoiceCustomerController.setSpMain(spMain);
    }

    private void bindInvoiceFields() {
        invoice.amountProperty().bind(Bindings.createObjectBinding(this::showItemLineAmounts, tvInvoice.getItems()));
        invoice.discountProperty().bind(Bindings.createObjectBinding(this::showDiscountAmount, tfDiscountPercent.textProperty(), invoice.amountProperty()));
        invoice.taxAmountProperty().bind(Bindings.createObjectBinding(this::showTaxAmount, tfDiscountPercent.textProperty(), invoice.amountProperty()));
        invoice.totalAmountDueProperty().bind(Bindings.createObjectBinding(this::showTotalAmountDue, invoice.amountProperty(), tfCashPay.textProperty()));
        invoice.totalAmountProperty().bind(Bindings.createObjectBinding(this::showTotalAmount, invoice.amountProperty(), tfDeliveryAmount.textProperty()));
    }

    private void setDiscountPercentInputValidation() {
        tfDiscountPercent.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.matches(Global.Regex.DECIMAL_PERCENT)) {
                return change;
            }
            return null;
        }));
        setDiscountPercentTextBehavior();
    }

    private void setNumberInputFormatter(JFXTextField textField) {
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
                .map(InvoiceLineItem::getAmount)
                .reduce(Money.parse("PHP 0.00"), Money::plus);
    }

    @SneakyThrows
    private Money showDiscountAmount() {
        Money amount = invoice.getAmount();
        if (StringUtils.isEmpty(tfDiscountPercent.getText())) return Money.parse("PHP 0.00");
        return moneyService.computePercentage(amount, tfDiscountPercent.getText());
    }

    private Money showTotalBeforeTax() {
        if (invoice.getAmount() == null) return Money.parse("PHP 0.00");
        return invoice.getAmount().minus(showDiscountAmount());
    }

    @SneakyThrows
    private Money showTaxAmount() {
        Money totalBeforeTax = showTotalBeforeTax();
        return moneyService.computePercentage(totalBeforeTax, Global.TAX);
    }

    private Money showTotalAmount() {
        Money currentTotal = showTotalBeforeTax();
        Money taxAmount = moneyService.computePercentage(currentTotal, Global.TAX);
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

    private void showInvoiceSummary() {
        lblTax.textProperty().bind(invoice.taxAmountProperty().asString());
        lblAmount.textProperty().bind(invoice.amountProperty().asString());
        lblTotalAmount.textProperty().bind(invoice.totalAmountProperty().asString());
        lblDiscountAmount.textProperty().bind(invoice.discountProperty().asString());
        lblAmountDue.textProperty().bind(invoice.totalAmountDueProperty().asString());
        lblTotalBeforeTax.textProperty().bind(Bindings.createObjectBinding(this::showTotalBeforeTax, invoice.amountProperty(), tfDiscountPercent.textProperty()).asString());
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

    private String getAmountValidatorMessage(ValidatorBase activeValidator) {
        String validatorMessage = "Please input two decimal digits only";
        if (activeValidator instanceof NumberValidator) {
            validatorMessage = "Please enter numerical value only";
        } else if (activeValidator instanceof RequiredFieldValidator) {
            validatorMessage = "Please enter an amount";
        }
        return validatorMessage;
    }


    private void initDate() {
        LocalDate todayDate = LocalDate.now();
        System.out.println(todayDate);
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new DateConverter());
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
    private void onProductItemClick() {
        Product product = cbProducts.getValue();
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
    private void onCreateInvoiceClick() {
        invoice.setCreatedAt(dpDate.getValue().atTime(LocalTime.now()));
        invoice.setInvoiceLineItems(tvInvoice.getItems());
        Invoice savedInvoice = invoiceService.saveEntity(invoice);
        savedInvoice.setName("INV-" + dpDate.getValue() + "-ID" + savedInvoice.getId());
        invoiceService.saveEntity(savedInvoice);
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
    public void setCustomerController(CustomerController invoiceCustomerController) {
        this.invoiceCustomerController = invoiceCustomerController;
    }
}
