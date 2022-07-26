package ph.parcs.rmhometiles.entity.invoice;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.StageInitializer;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.payment.Payment;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Controller
public class ViewInvoiceController {

    @FXML
    private TableColumn<OrderItem, String> tcItemName;
    @FXML
    private TableView<OrderItem> tvOrderItems;
    @FXML
    private TableView<Payment> tvPayments;
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

    private Invoice invoice;

    @Autowired
    private StageInitializer stageInitializer;

    private void displayDetails(Invoice invoice) {
        if (invoice != null) {
            tcItemName.setCellValueFactory(cellData -> {
                Product orderProduct = cellData.getValue().getProduct();
                return Bindings.createObjectBinding(orderProduct::getCode);
            });
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");
            String formattedDate = invoice.getCreatedAt().format(myFormatObj);

            lblCustomer.setText("Customer: " + invoice.getCustomer().getName());
            lblTotalAmount.setText("₱" + invoice.getTotalAmount().getAmount());
            lblInvoiceDate.setText("Invoice Date: " + formattedDate);
            lblBalance.setText("₱" + invoice.getTotalAmountDue().getAmount());

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
}
