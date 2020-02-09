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
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;

@Controller
public abstract class ItemTableController<T extends Item> {

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

    protected EditItemController<T> editItemController;
    protected ItemService<T> itemService;
    protected String searchValue = "";

    private SweetAlert deleteAlert;
    private SweetAlert successAlert;

    @FXML
    protected void initialize() {
        deleteAlert = SweetAlertFactory.create(SweetAlert.Type.WARNING);
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);

        updateItems();
        initItemPagination();
        initActionColumn();
        initSearchItem();
    }

    private void initItemPagination() {
        pagination.currentPageIndexProperty().addListener((observable) -> updateItems());
    }

    private void initSearchItem() {
        tfSearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            searchValue = newValue;
            updateItems();
        });
    }

    private void initActionColumn() {
        tcAction.setCellFactory(ActionTableCell.forColumn(
                this::onItemDeleteAction, this::onItemEditAction));
    }

    final protected void updateItems() {
        Page<T> items = itemService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
        tvItem.setItems(FXCollections.observableArrayList(items.toList()));
        tvItem.refresh();
        updatePageEntries(items);
    }

    protected T onItemDeleteAction(T item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        deleteAlert.setMessage("Are you sure you want to delete " + item.getName() + "?");
        deleteAlert.setConfirmListener(() -> {
            if (itemService.deleteItem(item)) {
                successAlert.setMessage(Global.MSG.DELETE).show(root);
                updateItems();
            }
        }).show(root);

        return item;
    }

    final protected T onItemEditAction(T editItem) {
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
        editItemController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(T entity) {
                successAlert.setMessage("Item saved!").show((StackPane) tvItem.getScene().getRoot());
                updateItems();
            }

            @Override
            public void onSaveFailed(T savedItem) {
            }
        }, editItem);
        return editItem;
    }

    final protected void updatePageEntries(Page<T> items) {
        ItemPageEntry itemPageEntry = itemService.getPageEntries(items);
        lblPageEntries.setText("Showing " + itemPageEntry.getFromEntry() + " to " + itemPageEntry.getToEntry() + " of " + items.getTotalElements() + " entries");
        pagination.setPageCount(items.getTotalPages());
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
