package ph.parcs.rmhometiles.item;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.custom.ActionTableCell;
import ph.parcs.rmhometiles.dialog.alert.SweetAlert;
import ph.parcs.rmhometiles.dialog.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Constant;

@Controller
public abstract class ItemTableController<T extends Item> {

    @FXML
    protected ComboBox<Integer> cbRowCount;
    @FXML
    protected JFXTextField tfSearchItem;
    @FXML
    protected Pagination pagination;
    @FXML
    protected Label lblPageEntries;
    @FXML
    protected TableView<T> tvItem;
    protected EditItemController<T> editItemController;
    protected ItemService<T> itemService;
    protected String searchValue = "";
    @FXML
    private TableColumn<T, HBox> tcAction;

    @FXML
    public void initialize() {
        updateItems();
        initItemPagination();
        initActionColumn();
        initSearchItem();
    }

    private void initItemPagination() {
        pagination.currentPageIndexProperty().addListener((observable) -> {
            updateItems();
        });
    }

    private void initSearchItem() {
        tfSearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            searchValue = newValue;
            updateItems();
        });
    }

    private void initActionColumn() {
        tcAction.setCellFactory(ActionTableCell.forColumn(
                this::showDeleteItemDialog,
                this::showEditItemDialog));
    }

    final protected void updateItems() {
        Page<T> items = itemService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
        tvItem.setItems(FXCollections.observableArrayList(items.toList()));
        tvItem.refresh();
        updatePageEntries(items);
        pagination.setPageCount(items.getTotalPages());
    }

    final protected void onSaveItem(T item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        T savedItem = itemService.saveItem(item);
        if (savedItem != null) {
            updateItems();
            String message = itemService.isItemEmpty(item) ? Constant.MSG.ADD : Constant.MSG.EDIT;
            SweetAlertFactory.successDialog(message).show(root);
        }
    }

    final protected T showDeleteItemDialog(T item) {
        final StackPane root = (StackPane) tvItem.getScene().getRoot();

        SweetAlert alert = SweetAlertFactory.deleteDialog();
        alert.setMessage("Are you sure you want to delete " + item.getName() + "?");
        alert.setConfirmListener(() -> {
            if (itemService.deleteItem(item)) {
                updateItems();
                SweetAlertFactory.successDialog(Constant.MSG.DELETE).show(root);
            }
        }).show(root);
        return item;
    }

    protected T showEditItemDialog(T item) {
        editItemController.onEditItem(this::onSaveItem, item);
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
        return item;
    }

    protected void updatePageEntries(Page<T> items) {
        long toEntry = items.getNumberOfElements() * (getCurrentPage() + 1);
        long fromEntry = (toEntry - getRowsPerPage()) + 1;

        if (items.isLast()) {
            toEntry = items.getTotalElements();
            fromEntry = toEntry - items.getNumberOfElements() + 1;
        }
        if (items.isFirst()) {
            fromEntry = 1;
            if (items.getTotalElements() == 0) fromEntry = 0;
        }
        lblPageEntries.setText("Showing " + fromEntry + " to "
                + toEntry + " of " + items.getTotalElements() + " entries");
    }

    @FXML
    private void onPageRowChanged() {
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
