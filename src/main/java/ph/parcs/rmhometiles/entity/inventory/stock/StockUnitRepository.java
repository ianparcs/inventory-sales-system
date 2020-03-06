package ph.parcs.rmhometiles.entity.inventory.stock;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.ItemRepository;

import java.util.List;
import java.util.Set;

@Repository
public interface StockUnitRepository extends ItemRepository<StockUnit, Integer> {

    @Override
    List<StockUnit> findAll();

    Set<StockUnit> findStockUnitByNameContains(String query);

}
