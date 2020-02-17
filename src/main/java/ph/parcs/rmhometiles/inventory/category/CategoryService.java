package ph.parcs.rmhometiles.inventory.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.Category;
import ph.parcs.rmhometiles.entity.Product;
import ph.parcs.rmhometiles.inventory.ProductRepository;
import ph.parcs.rmhometiles.item.ItemService;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryService extends ItemService<Category> {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

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
    public boolean deleteItem(Category category) {
        Category clearProd = removeProductsOfCategory(category);
        categoryRepository.delete(clearProd);
        Optional<Category> search = categoryRepository.findById(category.getId());
        return search.isEmpty();
    }

    private Category removeProductsOfCategory(Category category) {
        Set<Product> productSet = productRepository.findProductsByCategory(category);
        if (productSet != null) {
            for (Product product : productSet) {
                product.setCategory(null);
            }
        }
        category.setProducts(null);
        return category;
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
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
