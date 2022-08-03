package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.StageInitializer;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.payment.Payment;
import ph.parcs.rmhometiles.entity.payment.PaymentService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ViewInvoiceController {

    @FXML
    private TableColumn<Payment, String> tcPaymentPaidDate;
    @FXML
    private TableColumn<OrderItem, Money> tcItemPrice;
    @FXML
    private TableColumn<OrderItem, String> tcItemName;
    @FXML
    private TableView<OrderItem> tvOrderItems;
    @FXML
    private JFXComboBox<String> cbPaymentType;
    @FXML
    private TableView<Payment> tvPayments;
    @FXML
    private JFXTextField tfCashPay;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblInvoiceDate;
    @FXML
    private Label lblCustomer;
    @FXML
    private Label lblBalance;
    @FXML
    private StackPane spMain;

    private PaymentService paymentService;
    private Invoice invoice;

    private StageInitializer stageInitializer;


    private void displayDetails(Invoice invoice) {
        if (invoice != null) {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");

            tcItemName.setCellValueFactory(cellData -> {
                Product orderProduct = cellData.getValue().getProduct();
                return Bindings.createObjectBinding(orderProduct::getCode);
            });

            tcItemPrice.setCellValueFactory(cellData -> {
                Product orderProduct = cellData.getValue().getProduct();
                return Bindings.createObjectBinding(orderProduct::getPrice);
            });

            tcPaymentPaidDate.setCellValueFactory(cellData -> {
                LocalDateTime paidDate = cellData.getValue().getCreatedAt();
                return Bindings.createObjectBinding(()-> paidDate.format(myFormatObj));
            });

            lblBalance.setText("₱" + invoice.getTotalAmountDue().getAmount());
            lblTotalAmount.setText("₱" + invoice.getTotalAmount().getAmount());
            lblCustomer.setText("Customer: " + invoice.getCustomer().getName());
            lblInvoiceDate.setText("Invoice Date: " +  invoice.getCreatedAt().format(myFormatObj));

            tvPayments.setItems(FXCollections.observableArrayList(invoice.getPayments()));
            tvOrderItems.setItems(FXCollections.observableArrayList(invoice.getOrderItems()));
        }
    }

    public void initData(Invoice invoice) {
        this.invoice = invoice;
        displayDetails(invoice);
    }

    @FXML
    public void backToInvoice() {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoice/manage-invoice.fxml"));
                fxmlLoader.setControllerFactory(aClass -> stageInitializer.getApplicationContext().getBean(aClass));
                Node node = fxmlLoader.load();
                spMain.getChildren().setAll(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void onClickedAddPayment() {
        String amount = tfCashPay.getText();
        String paymentType = cbPaymentType.getValue();

        Payment payment = paymentService.createPayment(invoice,amount,paymentType);

        tvPayments.getItems().add(payment);
        tvPayments.refresh();
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    public void setStageInitializer(StageInitializer stageInitializer) {
        this.stageInitializer = stageInitializer;
    }
}
