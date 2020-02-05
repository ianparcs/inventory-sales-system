package ph.parcs.rmhometiles.category;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.SaveListener;
import ph.parcs.rmhometiles.item.EditItemController;

@Controller
public class EditCategoryController extends EditItemController<Category> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected Category unbindFields(Category category) {
        return null;
    }

    @Override
    protected void bindFields(Category category) {

    }

    public void onSaveItem(SaveListener<Category> saveListener) {
        btnSave.setOnAction(actionEvent -> {
            Category category = unbindFields(new Category());
            saveListener.onSaveData(category);
            closeDialog();
        });
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

}
