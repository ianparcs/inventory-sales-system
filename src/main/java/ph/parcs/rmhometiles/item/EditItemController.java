package ph.parcs.rmhometiles.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.SaveListener;

@Controller
public abstract class EditItemController<T extends Item> {

    @FXML
    protected JFXDialog saveDialog;
    @FXML
    protected JFXButton btnSave;

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

    final public void onEditItem(SaveListener<T> saveListener, final T item) {
        bindFields(item);
        btnSave.setOnAction(actionEvent -> {
            closeDialog();
            unbindFields(item);
            saveListener.onSaveData(item);
        });
    }

    final public void onSaveItem(SaveListener<T> saveListener, T newItem) {
        btnSave.setOnAction(actionEvent -> {
            T unbindItem = unbindFields(newItem);
            saveListener.onSaveData(unbindItem);
            closeDialog();
        });
    }

    @FXML
    protected void closeDialog() {
        saveDialog.close();
    }

    public void showDialog(StackPane stackPane) {
        saveDialog.show(stackPane);
    }

    protected abstract T unbindFields(T item);

    protected abstract void bindFields(T item);

    protected abstract void clearFields();
}

