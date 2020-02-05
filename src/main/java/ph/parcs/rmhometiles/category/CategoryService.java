package ph.parcs.rmhometiles.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public ObservableList<Category> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(categoryList, ArrayList::new));
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
