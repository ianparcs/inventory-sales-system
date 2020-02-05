package ph.parcs.rmhometiles.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.item.ItemTableController;

@Controller
public class CategoryTableController extends ItemTableController<Category> {

    @Override
    public void initialize() {
        // updateItems();
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.itemService = categoryService;
    }


}
