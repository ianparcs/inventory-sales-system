package ph.parcs.rmhometiles.entity.invoice.manage;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;
import ph.parcs.rmhometiles.entity.invoice.ViewInvoiceController;
import ph.parcs.rmhometiles.session.SessionService;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.scene.SceneManager;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.DateUtility;
import ph.parcs.rmhometiles.util.ThreadUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @FXML
    private TableColumn<Invoice, String> tcInvoiceDate;
    @FXML
    private TableColumn<Invoice, Customer> tcCustomer;
    @FXML
    private TableColumn<Invoice, String> tcStatus;
    @FXML
    private JFXComboBox<String> cbDateRange;

    private InvoiceService invoiceService;
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

        spMain.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                    var currentUser = Optional.of(SessionService.getInstance().getLoggedInUser());
                    currentUser.ifPresent(user -> {
                        if (tcAction.getCellFactory() != null) {
                            switch (user.getRole()) {
                                case USER -> tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, this::onEditActionClick, this::onDeleteActionClick));
                                case ADMIN -> tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, AppConstant.ActionType.VIEW));
                            }
                        }
                    });
            }
        });
    }

    @SneakyThrows
    public Invoice onViewActionClick(BaseEntity item) {
        FXMLLoader fxmlLoader = sceneManager.create("/fxml/invoice/view-invoice.fxml");
        Invoice invoice = (Invoice) item;
        Parent parent = fxmlLoader.load();
        ViewInvoiceController viewInvoiceController = fxmlLoader.getController();
        viewInvoiceController.initData(invoice);
        spMain.getChildren().setAll(parent);
        return invoice;
    }

    @FXML
    public void onDateRangeSelect() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            LocalDateTime[] dateTimeRange = DateUtility.findDate(cbDateRange.getValue());
            List<Invoice> invoices = invoiceService.findAllInvoiceByDate(dateTimeRange);
            Platform.runLater(() -> {
                tvItem.getItems().setAll(invoices);
                tvItem.refresh();
            });
        });
        ThreadUtil.shutdownAndAwaitTermination(executorService);
    }

    @FXML
    public void onRefreshClicked() {
        super.updateItems();
        cbDateRange.getSelectionModel().selectFirst();
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        this.baseService = invoiceService;
    }

    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Autowired
    public void setEditItemController(EditItemController<Invoice> editItemController) {
        this.editItemController = editItemController;
    }

}
