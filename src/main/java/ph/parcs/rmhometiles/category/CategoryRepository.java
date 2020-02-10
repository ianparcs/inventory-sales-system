package ph.parcs.rmhometiles.category;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.item.ItemRepository;

import java.util.List;

@Repository
public interface CategoryRepository extends ItemRepository<Category, Integer> {

    @Override
    List<Category> findAll();
}
