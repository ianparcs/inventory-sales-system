package ph.parcs.rmhometiles.entity.inventory.item;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

@Controller
public abstract class ItemTableController<T extends BaseEntity> {

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
    protected ItemService<T> itemService;
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
        tcAction.setCellFactory(ActionTableCell.forColumn(
                this::onItemDeleteAction, this::onItemEditAction));
    }

    public void updateItems() {
        Page<T> items = itemService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
        updatePageEntries(items);
        Platform.runLater(() -> {
            tvItem.setItems(FXCollections.observableArrayList(items.toList()));
            tvItem.refresh();
        });
    }

    protected T onItemDeleteAction(T item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        deleteAlert.setHeaderMessage("Delete " + item.getClass().getSimpleName());
        deleteAlert.setContentMessage("Are you sure you want to delete " + item.getName() + "?");
        deleteAlert.setConfirmListener(() -> {
            if (itemService.deleteItem(item)) {
                successAlert.setContentMessage(Global.MSG.DELETE).show(root);
                updateItems();
            }
        }).show(root);

        return item;
    }

    public T onItemEditAction(T editItem) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();

        editItemController.showDialog(root);
        editItemController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(T entity) {
                successAlert.setContentMessage(Global.MSG.SAVED).show(root);
                updateItems();
            }

            @Override
            public void onSaveFailed(T savedItem) {

            }
        }, editItem);
        return editItem;
    }

    private void updatePageEntries(Page<T> items) {
        ItemPageEntry itemPageEntry = itemService.getPageEntries(items);
        lblPageEntries.setText("Showing " + itemPageEntry.getFromEntry() + " to " + itemPageEntry.getToEntry() + " of " + items.getTotalElements() + " entries");
        pagination.setPageCount(items.getTotalPages());
    }

    @FXML
    private void onPageRowChanged() {
        updateItems();
    }

    @FXML
    private void searchItem() {
        searchValue = tfSearchItem.getText();
        updateItems();
    }

    protected int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    protected int getRowsPerPage() {
        return cbRowCount.getValue();
    }

    @Autowired
    public void setEditItemController(EditItemController<T> editItemController) {
        this.editItemController = editItemController;
    }
}
