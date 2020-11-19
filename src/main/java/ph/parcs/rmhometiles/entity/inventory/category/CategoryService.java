package ph.parcs.rmhometiles.entity.inventory.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductRepository;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryService extends BaseService<Category> {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public ObservableList<Category> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(categoryList, ArrayList::new));
    }

    @Override
    public boolean deleteEntity(Category category) {
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

    public Category createDefault() {
        Category category = new Category();
        category.setName("");
        category.setId(0);
        return category;
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
        this.entityRepository = categoryRepository;
        this.categoryRepository = (CategoryRepository) entityRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
