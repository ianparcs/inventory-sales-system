package ph.parcs.rmhometiles.entity.inventory.item;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.concurrent.CountDownLatch;

@Controller
public abstract class EntityTableController<T extends BaseEntity> implements EntityActions<T> {

    @FXML
    protected ComboBox<Integer> cbRowCount;
    @FXML
    protected TableColumn<T, HBox> tcAction;
    @FXML
    protected JFXTextField tfSearchItem;
    @FXML
    protected Pagination pagination;
    @FXML
    protected Label lblPageEntries;
    @FXML
    protected TableView<T> tvItem;
    @FXML
    protected StackPane spMain;

    protected EditItemController<T> editItemController;
    protected BaseService<T> baseService;
    protected String searchValue = "";

    protected SweetAlert deleteAlert;
    protected SweetAlert successAlert;

    @FXML
    protected void initialize() {
        deleteAlert = SweetAlertFactory.create(SweetAlert.Type.WARNING);
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);

        initItemPagination();
        initActionColumn();

        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) updateItems();
        });
    }

    private void initItemPagination() {
        pagination.currentPageIndexProperty().addListener((observable) -> updateItems());
    }

    private void initActionColumn() {
        tcAction.setCellFactory(ActionTableCell.forActions(
                this::onDeleteActionClick, this::onEditActionClick));
    }

    public void updateItems() {
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        Page<T> items = baseService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            try {
                                updatePageEntries(items);
                                tvItem.setItems(FXCollections.observableArrayList(items.toList()));
                                tvItem.refresh();
                            } finally {
                                latch.countDown();
                            }
                        });
                        latch.await();
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public T onDeleteActionClick(T item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        deleteAlert.setHeaderMessage("Delete " + item.getClass().getSimpleName());
        deleteAlert.setContentMessage("Are you sure you want to delete " + item.getName() + "?");
        deleteAlert.setConfirmListener(() -> {
            if (baseService.deleteEntity(item)) {
                successAlert.setContentMessage(Global.MSG.DELETE).show(root);
                updateItems();
            }
        }).show(root);

        return item;
    }

    public T onEditActionClick(T entity) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();

        editItemController.showDialog(root);
        editItemController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(T entity) {
                successAlert.setContentMessage(Global.MSG.SAVED).show(root);
                updateItems();
            }

            @Override
            public void onSaveFailed(T entity) {

            }
        }, entity);
        return entity;
    }

    private void updatePageEntries(Page<T> items) {
        ItemPageEntry itemPageEntry = PageUtil.getPageEntries((Page<BaseEntity>) items);
        lblPageEntries.setText("Showing " + itemPageEntry.getFromEntry() + " to " + itemPageEntry.getToEntry() + " of " + items.getTotalElements() + " entries");
        pagination.setPageCount(items.getTotalPages());
    }

    @FXML
    private void onPageRowChanged() {
        Page<T> items = baseService.findPages(0, getRowsPerPage(), searchValue);
        updatePageEntries(items);
        tvItem.setItems(FXCollections.observableArrayList(items.toList()));
        tvItem.refresh();
    }

    @FXML
    private void searchItem() {
        searchValue = tfSearchItem.getText();
        updateItems();
    }

    private int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    private int getRowsPerPage() {
        return cbRowCount.getValue();
    }

    @Autowired
    public void setEditItemController(EditItemController<T> editItemController) {
        this.editItemController = editItemController;
    }
}
