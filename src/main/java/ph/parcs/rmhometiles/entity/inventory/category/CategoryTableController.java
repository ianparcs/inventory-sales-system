package ph.parcs.rmhometiles.entity.inventory.category;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.user.User;

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

    @Autowired
    public void setEditItemController(EditItemController<Category> editItemController) {
        this.editItemController = editItemController;
    }

    @Override
    protected void hideUIBasedOnUserRole(User user) {

    }
}
