package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.invoice.manage.ManageInvoiceTableController;

@Controller
public class InvoiceTabController {

    @FXML
    private Tab tabManageInvoice;
    @FXML
    private JFXTabPane tpRoot;

    private ManageInvoiceTableController manageInvoiceTableController;

    @FXML
    private void initialize() {
        tpRoot.setDisableAnimation(true);
        tpRoot.visibleProperty().addListener((ov, oldTab, newTab) -> {
            if (tpRoot.getSelectionModel().getSelectedItem().equals(tabManageInvoice))
                manageInvoiceTableController.updateItems();
        });
    }

    @Autowired
    public void setManageInvoiceTableController(ManageInvoiceTableController manageInvoiceTableController) {
        this.manageInvoiceTableController = manageInvoiceTableController;
    }
}
