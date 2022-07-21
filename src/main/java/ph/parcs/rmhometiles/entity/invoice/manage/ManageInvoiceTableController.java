package ph.parcs.rmhometiles.entity.invoice.manage;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.StageInitializer;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;
import ph.parcs.rmhometiles.entity.invoice.ViewInvoiceController;
import ph.parcs.rmhometiles.ui.ActionTableCell;

import java.io.IOException;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @FXML
    private TableColumn<Invoice, Customer> tcCustomer;
    @Autowired
    private StageInitializer stageInitializer;

    @FXML
    public void initialize() {
        super.initialize();
        tcCustomer.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().getCustomer(), "name"));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, this::onEditActionClick, this::onDeleteActionClick));
    }

    public Invoice onViewActionClick(Invoice item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/invoice/view-invoice.fxml"));
        fxmlLoader.setControllerFactory(aClass -> stageInitializer.getApplicationContext().getBean(aClass));

        try {
            Parent parent = fxmlLoader.load();
            ViewInvoiceController viewInvoiceController = fxmlLoader.getController();
            viewInvoiceController.initData(item);
            spMain.getChildren().setAll(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
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
