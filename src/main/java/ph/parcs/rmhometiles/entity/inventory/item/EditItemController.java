package ph.parcs.rmhometiles.entity.inventory.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.ItemListener;

@Controller
public abstract class EditItemController<T extends BaseEntity> {

    @FXML
    protected JFXDialog editDialog;
    @FXML
    protected JFXButton btnSave;
    @FXML
    protected Label lblTitle;

    protected BaseService<T> baseService;

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

    protected void validateNumberField(JFXTextField tf) {
        validateField(tf);
        RegexValidator valid = new RegexValidator();
        valid.setRegexPattern("^[1-9]\\d*(\\.\\d{1,2})?$");
        tf.setValidators(valid);
        tf.getValidators().get(0).setMessage("No special characters. Maximum of two decimal digits");
    }

    public void onEditItem(ItemListener<T> itemListener, final T item) {
        setDialogTitle(item);
        bindFields(item);

        btnSave.setOnAction(actionEvent -> {
            closeDialog();
            T savedItem = baseService.saveEntity(createEntity(item.getId()));
            if (savedItem != null) {
                itemListener.onSavedSuccess(savedItem);
            } else {
                itemListener.onSaveFailed(null);
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

    protected void setDialogTitle(T item) {
        String title = item.getId() > 0 ? "Edit" : "Add";
        lblTitle.setText(title + " " + item.getClass().getSimpleName());
    }

    protected abstract T createEntity(Integer id);

    protected abstract void bindFields(T item);

    protected abstract void clearFields();
}

