package ph.parcs.rmhometiles.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.item.ItemService;
import ph.parcs.rmhometiles.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService extends ItemService<Category> {

    private CategoryRepository categoryRepository;

    public ObservableList<Category> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        categoryList.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(categoryList, ArrayList::new));
    }

    @Override
    public Page<Category> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return categoryRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    @Transactional
    public boolean deleteItem(Category category) {
        Optional<Category> search = categoryRepository.findById(category.getId());
        if (search.isPresent()) {
            categoryRepository.delete(search.get());
            return true;
        }
        return false;
    }

    @Override
    public Category saveItem(Category item) {
        return categoryRepository.save(item);
    }

    public Category createDefault() {
        Category category = new Category();
        category.setName("");
        category.setId(0);
        return category;
    }

    @Override
    public boolean isNew(Category item) {
        return categoryRepository.findById(item.getId()).isEmpty();
    }

    public Optional<Category> findCategoryByProduct(ObservableList<Category> items, Product product) {
        Optional<Category> search = Optional.empty();
        if (product.getCategory() != null) {
            search = items.stream()
                    .filter(item -> item.getId().equals(product.getCategory().getId()))
                    .findAny();
        }
        return search;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
