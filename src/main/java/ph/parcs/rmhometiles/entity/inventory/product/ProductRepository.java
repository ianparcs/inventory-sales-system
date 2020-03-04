package ph.parcs.rmhometiles.entity.inventory.product;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;
import ph.parcs.rmhometiles.entity.inventory.stock.StockUnit;
import ph.parcs.rmhometiles.entity.supplier.Supplier;

import java.util.Set;

@Repository
public interface ProductRepository extends ItemRepository<Product, Integer> {

    Set<Product> findProductsByCategory(Category category);

    Set<Product> findProductsBySupplier(Supplier supplier);

    Set<Product> findProductsByStockUnit(StockUnit stockUnit);

}
