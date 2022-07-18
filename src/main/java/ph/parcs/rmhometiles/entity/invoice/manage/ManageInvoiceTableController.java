package ph.parcs.rmhometiles.entity.invoice.manage;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @FXML
    private TableColumn<Invoice, Customer> tcCustomer;

    @FXML
    public void initialize() {
        super.initialize();
        tcCustomer.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().getCustomer(), "name"));
    }

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Invoice());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.baseService = invoiceService;
    }

}
