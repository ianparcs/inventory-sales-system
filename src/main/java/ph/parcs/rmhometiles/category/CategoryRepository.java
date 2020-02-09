package ph.parcs.rmhometiles.category;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.item.ItemRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends ItemRepository<Category, Integer> {

    @Override
    List<Category> findAll();

    @Override
    Optional<Category> findById(Integer integer);
}
