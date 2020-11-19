package ph.parcs.rmhometiles.entity.inventory.category;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends EntityRepository<Category, Integer> {

    @Override
    List<Category> findAll();

    Set<Category> findCategoriesByProducts(Product product);

}
