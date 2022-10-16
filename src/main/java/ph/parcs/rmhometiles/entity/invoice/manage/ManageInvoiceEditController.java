package ph.parcs.rmhometiles.entity.invoice.manage;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;

@Controller
public class ManageInvoiceEditController extends EditItemController<Invoice> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected Invoice createEntity(Invoice invoice) {
        invoice.setName(tfName.getText());
        return invoice;
    }

    @Override
    protected void bindFields(Invoice category) {
        if (!StringUtils.isEmpty(category.getName()))
            tfName.setText(category.getName());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.baseService = invoiceService;
    }


}
