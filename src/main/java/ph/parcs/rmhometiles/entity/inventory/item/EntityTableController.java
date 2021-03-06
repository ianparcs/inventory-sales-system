package ph.parcs.rmhometiles.entity.inventory.item;

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
import ph.parcs.rmhometiles.util.PageUtil;

@Controller
public abstract class EntityTableController<T extends BaseEntity> implements EntityActions<T> {

    @FXML
    protected ComboBox<String> cbRowCount;
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
    private String searchValue = "";

    private SweetAlert deleteAlert;
    private SweetAlert successAlert;

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
        Page<T> items = baseService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
        tvItem.setItems(FXCollections.observableArrayList(items.toList()));
        tvItem.refresh();
        updatePageEntries(items);
    }

    @FXML
    private void onPageRowChanged() {
        updateItems();
    }

    public T onDeleteActionClick(T item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        deleteAlert.setHeaderMessage("Delete " + item.getClass().getSimpleName());
        deleteAlert.setContentMessage("Are you sure you want to delete " + item.getName() + "?");
        deleteAlert.setConfirmListener(() -> {
            if (baseService.deleteEntity(item)) {
                successAlert.setContentMessage(Global.Message.DELETE).show(root);
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
                successAlert.setContentMessage(Global.Message.SAVED).show(root);
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
    private void searchItem() {
        searchValue = tfSearchItem.getText();
        updateItems();
    }

    private int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    private int getRowsPerPage() {
        if (cbRowCount.getValue().equalsIgnoreCase("all")) return Integer.MAX_VALUE;
        return Integer.parseInt(cbRowCount.getValue());
    }

    @Autowired
    public void setEditItemController(EditItemController<T> editItemController) {
        this.editItemController = editItemController;
    }
}
