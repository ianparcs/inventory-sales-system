package ph.parcs.rmhometiles.product;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.item.ItemRepository;
import ph.parcs.rmhometiles.supplier.Supplier;

import java.util.Set;

@Repository
public interface ProductRepository extends ItemRepository<Product, Integer> {

    Set<Product> findProductsByCategory(Category category);

    Set<Product> findProductsBySupplier(Supplier supplier);
}
