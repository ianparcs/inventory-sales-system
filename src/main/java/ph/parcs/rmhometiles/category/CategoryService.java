package ph.parcs.rmhometiles.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.item.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService extends ItemService<Category> {

    private CategoryRepository categoryRepository;

    public ObservableList<Category> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(categoryList, ArrayList::new));
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findPages(int page, int itemPerPage, String name) {
        return null;
    }

    @Override
    public boolean deleteItem(Category item) {
        return false;
    }

    @Override
    public Category saveItem(Category item) {
        return null;
    }
}
