package ph.parcs.rmhometiles.entity.invoice.manage;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerService;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.baseService = invoiceService;
    }

}
