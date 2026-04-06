package ph.parcs.rmhometiles.entity.inventory.item;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.user.User;
import ph.parcs.rmhometiles.exception.AppException;
import ph.parcs.rmhometiles.session.SessionService;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.pagination.PaginationController;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.date.DateUtil;

import java.time.LocalDateTime;

@Slf4j
@Controller
public abstract class EntityTableController<T extends BaseEntity> extends PaginationController<T> implements EntityActions<BaseEntity> {

    @FXML
    protected TableColumn<BaseEntity, LocalDateTime> tcCreatedAt;
    @FXML
    protected TableColumn<BaseEntity, User> tcCreatedBy;
    @FXML
    protected TableColumn<BaseEntity, HBox> tcAction;
    @FXML
    protected JFXTextField tfSearchItem;
    @FXML
    public HBox hbSearchContainer;

    @FXML
    protected StackPane spMain;

    protected EditItemController<T> editItemController;

    private SweetAlert deleteAlert;
    private SweetAlert successAlert;
    private SweetAlert errorAlert;

    @FXML
    protected void initialize() {
        super.initialize();
        super.updateItems();

        successAlert = SweetAlertFactory.create(SweetAlert.Type.SUCCESS);
        deleteAlert = SweetAlertFactory.create(SweetAlert.Type.WARNING);
        errorAlert = SweetAlertFactory.create(SweetAlert.Type.DANGER);

        tcCreatedAt.setCellFactory(new Callback<>() {
            @Override
            public TableCell<BaseEntity, LocalDateTime> call(TableColumn<BaseEntity, LocalDateTime> param) {
                return new TableCell<>() {
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.format(DateUtil.FORMAT));
                        }
                    }
                };
            }
        });

        tcCreatedBy.setCellFactory(new Callback<>() {
            @Override
            public TableCell<BaseEntity, User> call(TableColumn<BaseEntity, User> param) {
                return new TableCell<>() {
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            setText(user.getName());
                        }
                    }
                };
            }
        });

        var loggedInUser = SessionService.getInstance().getLoggedInUser();
        hideUIBasedOnUserRole(loggedInUser);
    }

    protected void hideUIBasedOnUserRole(User user) {
        switch (user.getRole()) {
            case ADMIN ->
                    tcAction.setCellFactory(ActionTableCell.forActions(this::onEditActionClick, this::onDeleteActionClick));
            case USER -> {
                hbSearchContainer.getChildren().remove(0);
                tcAction.setVisible(false);
            }
        }
    }

    @SneakyThrows
    @Override
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
            } catch (AppException itemLockedException) {
                errorAlert.setContentMessage(itemLockedException.getMessage()).show(root);
            }

        });
        Platform.runLater(() -> deleteAlert.show(root));
        return item;
    }

    @Override
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
            public void onSaveFailed(Exception e) {
                errorAlert.setContentMessage(e.getMessage());
                errorAlert.show(root);
            }

        }, (T) entity);
        return entity;
    }

    @Override
    public BaseEntity onViewActionClick(BaseEntity entity) {
        return entity;
    }

    @FXML
    private void searchItem() {
        searchValue = tfSearchItem.getText();
        updateItems();
    }
}
