package ph.parcs.rmhometiles.entity.inventory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;
import ph.parcs.rmhometiles.entity.supplier.Supplier;

import java.util.Set;

@Repository
public interface ProductRepository extends EntityRepository<Product, Integer> {

    Set<Product> findProductsByCategory(Category category);

    Set<Product> findProductsBySupplier(Supplier supplier);

    Set<Product> findAllByCodePropertyContains(String code);

    Page<Product> findAllByCodePropertyContains(Pageable pageable, String name);

}
