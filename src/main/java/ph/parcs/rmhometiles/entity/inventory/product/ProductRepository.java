package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.StringProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Set<Product> findAllByCodeContains(String code);

    Page<Product> findAllByCodeContains(Pageable pageable, String name);

}
