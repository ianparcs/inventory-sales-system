package ph.parcs.rmhometiles.entity.inventory.category;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;

@Controller
public class CategoryTableController extends EntityTableController<Category> {

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Category());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.baseService = categoryService;
    }
}
