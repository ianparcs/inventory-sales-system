package ph.parcs.rmhometiles.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.BaseEntity;

@Controller
public abstract class EditItemController<T extends BaseEntity> {

    @FXML
    protected JFXDialog editDialog;
    @FXML
    protected JFXButton btnSave;
    @FXML
    protected Label lblTitle;

    protected ItemService<T> itemService;

    @FXML
    public void initialize() {
        editDialog.setOnDialogClosed(event -> clearFields());
    }

    protected void validateField(JFXTextField tf) {
        tf.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                tf.validate();
                if (tf.getActiveValidator() != null && tf.getActiveValidator().getHasErrors()) {
                    tf.requestFocus();
                }
            }
        });
    }

    final void onEditItem(ItemListener<T> itemListener, final T item) {
        setDialogTitle(item);
        bindFields(item);

        btnSave.setOnAction(actionEvent -> {
            closeDialog();
            T savedItem = itemService.saveItem(unbindFields(item.getId()));
            if (!itemService.isEmpty(savedItem)) {
                itemListener.onSavedSuccess(savedItem);
            } else {
                itemListener.onSaveFailed(savedItem);
            }
        });
    }

    public void showDialog(StackPane stackPane) {
        editDialog.show(stackPane);
    }

    @FXML
    protected void closeDialog() {
        editDialog.close();
    }

    private void setDialogTitle(T item) {
        String title = item.getId() > 0 ? "Edit" : "Add";
        lblTitle.setText(title + " " + item.getClass().getSimpleName());
    }

    protected abstract T unbindFields(Integer id);

    protected abstract void bindFields(T item);

    protected abstract void clearFields();
}

