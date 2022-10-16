package ph.parcs.rmhometiles.entity.inventory.category;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;

import java.time.LocalDateTime;

@Controller
public class CategoryEditController extends EditItemController<Category> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected Category createEntity(Integer id) {
        Category category = new Category();
        category.setName(tfName.getText());
        category.setId(id);

        if (id == 0) category.setCreatedAt(LocalDateTime.now());
        else category.setUpdatedAt(LocalDateTime.now());

        return category;
    }

    @Override
    protected void bindFields(Category category) {
        if (!StringUtils.isEmpty(category.getName()))
            tfName.setText(category.getName());
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.baseService = categoryService;
    }

}
