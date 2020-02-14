package ph.parcs.rmhometiles.category;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.Category;
import ph.parcs.rmhometiles.item.ItemTableController;

@Controller
public class CategoryTableController extends ItemTableController<Category> {

    @FXML
    private void showAddItemDialog() {
        onItemEditAction(new Category());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.itemService = categoryService;
    }
}
