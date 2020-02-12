package ph.parcs.rmhometiles.category;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.item.EditItemController;

@Controller
public class EditCategoryController extends EditItemController<Category> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected Category unbindFields(Integer id) {
        Category category = new Category();
        category.setName(tfName.getText());
        category.setId(id);
        return category;
    }

    @Override
    protected void bindFields(Category category) {
        tfName.setText(category.getName());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.itemService = categoryService;
    }

}
