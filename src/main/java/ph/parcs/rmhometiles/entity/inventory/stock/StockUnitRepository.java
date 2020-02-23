package ph.parcs.rmhometiles.entity.inventory.stock;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.List;

@Repository
public interface StockUnitRepository extends ItemRepository<StockUnit, Integer> {

    @Override
    List<StockUnit> findAll();


}
