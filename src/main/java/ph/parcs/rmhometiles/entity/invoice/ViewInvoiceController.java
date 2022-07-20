package ph.parcs.rmhometiles.entity.invoice;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.StageInitializer;

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
    private StackPane test;
    @Autowired
    private StageInitializer stageInitializer;

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
                fxmlLoader.setControllerFactory(aClass -> stageInitializer.getApplicationContext().getBean(aClass));
                Node node = fxmlLoader.load();
                spMain.getChildren().setAll(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
