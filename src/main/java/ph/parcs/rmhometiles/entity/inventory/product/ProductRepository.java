package ph.parcs.rmhometiles.entity.inventory.product;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.Category;
import ph.parcs.rmhometiles.entity.Supplier;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.Set;

@Repository
public interface ProductRepository extends ItemRepository<Product, Integer> {

    Set<Product> findProductsByCategory(Category category);

    Set<Product> findProductsBySupplier(Supplier supplier);
}
