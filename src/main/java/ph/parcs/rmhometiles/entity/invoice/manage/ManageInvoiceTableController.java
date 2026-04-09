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
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.invoice.InvoiceService;
import ph.parcs.rmhometiles.entity.invoice.ViewInvoiceController;
import ph.parcs.rmhometiles.entity.payment.Payment;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.scene.SceneManager;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.TableColumnUtil;
import ph.parcs.rmhometiles.util.ThreadUtil;
import ph.parcs.rmhometiles.util.date.DateRangeType;
import ph.parcs.rmhometiles.util.date.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ManageInvoiceTableController extends EntityTableController<Invoice> {

    @FXML
    private TableColumn<Invoice, Payment.Status> tcPaymentStatus;
    @FXML
    private TableColumn<Invoice, Customer> tcCustomer;
    @FXML
    private TableColumn<Invoice, Money> tcTotalAmount;
    @FXML
    private TableColumn<Invoice, Money> tcBalance;
    @FXML
    private JFXComboBox<String> cbDateRange;
    @FXML
    public JFXComboBox<String> cbInvoices;

    private InvoiceService invoiceService;
    private SceneManager sceneManager;

    @FXML
    public void initialize() {
        super.initialize();

        TableColumnUtil.configureMoneyColumn(tcBalance);
        TableColumnUtil.configureMoneyColumn(tcTotalAmount);

        tcCustomer.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().getCustomer(), "name"));
        tcPaymentStatus.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Invoice, Payment.Status> call(TableColumn<Invoice, Payment.Status> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Payment.Status status, boolean empty) {
                        super.updateItem(status, empty);
                        if (status != null) {
                            setText(status.name());
                            this.getStyleClass().add(status.name().equalsIgnoreCase("PAID") ? "item-paid" : "item-unpaid");
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void hideUIBasedOnUserRole(User user) {
        switch (user.getRole()) {
            case ADMIN ->
                    tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, this::onEditActionClick, this::onDeleteActionClick));
            case USER ->
                    tcAction.setCellFactory(ActionTableCell.forActions(this::onViewActionClick, AppConstant.ActionType.VIEW));
        }
    }

    @FXML
    public void onStatusFilterClicked() {
        new Thread(() -> {
            var filterInvoice = invoiceService.filterByStatus(cbInvoices.getValue().toUpperCase());
            Platform.runLater(() -> {
                tvItem.getItems().setAll(filterInvoice);
                tvItem.refresh();
            });
        }).start();
    }

    @SneakyThrows
    @Override
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
            DateRangeType dateRangeType = DateRangeType.fromValue(cbDateRange.getValue());
            LocalDateTime[] dateTimeRange = DateUtil.find(dateRangeType);
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
