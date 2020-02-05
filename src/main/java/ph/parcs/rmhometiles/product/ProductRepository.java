package ph.parcs.rmhometiles.product;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.item.ItemRepository;

@Repository
public interface ProductRepository extends ItemRepository<Product, Integer> {
}
