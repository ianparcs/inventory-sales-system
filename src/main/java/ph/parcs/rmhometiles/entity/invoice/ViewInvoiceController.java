package ph.parcs.rmhometiles.entity.invoice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Controller
public class ViewInvoiceController {

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

    private void displayDetails(Invoice invoice) {
        if (invoice != null) {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");
            String formattedDate = invoice.getCreatedAt().format(myFormatObj);

            lblCustomer.setText("Customer: " + invoice.getCustomer().getName());
            lblTotalAmount.setText("₱" + invoice.getTotalAmount().getAmount());
            lblInvoiceDate.setText("Invoice Date: " + formattedDate);
            lblBalance.setText("₱" + invoice.getTotalAmountDue().getAmount());
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
                    Node node = fxmlLoader.load();
                    test.getChildren().setAll(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
    private StackPane test;

    public void setPreviousPage(StackPane test) {
        this.test = test;
    }
}
