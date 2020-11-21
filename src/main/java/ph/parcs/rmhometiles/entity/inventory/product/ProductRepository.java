package ph.parcs.rmhometiles.entity.inventory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;
import ph.parcs.rmhometiles.entity.supplier.Supplier;

import java.util.List;

@Repository
public interface ProductRepository extends EntityRepository<Product, Integer> {

    List<Product> findProductsByCategory(Category category);

    List<Product> findProductsBySupplier(Supplier supplier);

    List<Product> findAllByCodeContains(String code);

    Page<Product> findAllByCodeContains(Pageable pageable, String name);

}
