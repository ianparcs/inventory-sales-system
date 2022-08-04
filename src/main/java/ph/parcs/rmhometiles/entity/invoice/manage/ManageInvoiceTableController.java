package ph.parcs.rmhometiles.entity.invoice.manage;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;
import ph.parcs.rmhometiles.entity.invoice.ViewInvoiceController;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.scene.SceneManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @FXML
    private TableColumn<Invoice, String> tcInvoiceDate;
    @FXML
    private TableColumn<Invoice, Customer> tcCustomer;
    @FXML
    private TableColumn<Invoice, String> tcStatus;

    private SceneManager sceneManager;

    @FXML
    public void initialize() {
        super.initialize();
        tcStatus.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Invoice, String> call(TableColumn<Invoice, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            this.getStyleClass().add(item.equalsIgnoreCase("PAID") ? "item-paid" : "item-unpaid");
                        }
                    }
                };
            }
        });

        tcInvoiceDate.setCellValueFactory(cellData -> {
            LocalDateTime createdAt = cellData.getValue().getCreatedAt();
            return Bindings.createObjectBinding(() -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd | hh:mm a");
                if (createdAt != null) {
                    return createdAt.format(formatter);
                }
                return "";
            });
        });

        tcCustomer.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().getCustomer(), "name"));
        tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, this::onEditActionClick, this::onDeleteActionClick));
    }

    public Invoice onViewActionClick(Invoice item) {
        FXMLLoader fxmlLoader = sceneManager.create("/fxml/invoice/view-invoice.fxml");

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
    public void onRefreshClicked() {
        super.updateItems();
    }

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Invoice());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.baseService = invoiceService;
    }

}
