package ph.parcs.rmhometiles.entity.inventory.item;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.exception.ItemLockedException;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.pagination.PaginationController;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;

@Controller
public abstract class EntityTableController<T extends BaseEntity> extends PaginationController<T> implements EntityActions<BaseEntity> {

    @FXML
    protected TableColumn<BaseEntity, HBox> tcAction;
    @FXML
    protected JFXTextField tfSearchItem;
    @FXML
    protected StackPane spMain;

    protected EditItemController<T> editItemController;

    private SweetAlert deleteAlert;
    private SweetAlert successAlert;
    private SweetAlert errorAlert;

    @FXML
    protected void initialize() {
        super.initialize();
        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
        deleteAlert = SweetAlertFactory.create(SweetAlert.Type.WARNING);
        errorAlert = SweetAlertFactory.create(SweetAlert.Type.DANGER);

        initActionColumn();

        spMain.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene != null) updateItems();
        });
    }

    private void initActionColumn() {
        tcAction.setCellFactory(ActionTableCell.forActions(
                this::onEditActionClick, this::onDeleteActionClick));
    }

    @FXML
    private void onPageRowChanged() {
        updateItems();
    }

    @SneakyThrows
    public BaseEntity onDeleteActionClick(BaseEntity item) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();
        deleteAlert.setHeaderMessage("Delete " + item.getClass().getSimpleName());
        deleteAlert.setContentMessage("Are you sure you want to delete " + item.getName() + "?");
        deleteAlert.setConfirmListener(() -> {
            try {
                if (baseService.deleteEntity((T) item)) {
                    successAlert.setContentMessage(AppConstant.Message.DELETE).show(root);
                    updateItems();
                }
            } catch (ItemLockedException itemLockedException) {
                errorAlert.setContentMessage(itemLockedException.getMessage()).show(root);
            }

        });
        Platform.runLater(() -> deleteAlert.show(root));
        return item;
    }

    public BaseEntity onEditActionClick(BaseEntity entity) {
        StackPane root = (StackPane) tvItem.getScene().getRoot();

        editItemController.showDialog(root);
        editItemController.onEditItem(new ItemListener<>() {
            @Override
            public void onSavedSuccess(BaseEntity entity) {
                successAlert.setContentMessage(AppConstant.Message.SAVED).show(root);
                updateItems();
            }

            @Override
            public void onSaveFailed(BaseEntity entity) {

            }
        }, (T)entity);
        return entity;
    }

    @FXML
    private void searchItem() {
        searchValue = tfSearchItem.getText();
        updateItems();
    }

}
