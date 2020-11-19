package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.List;
import java.util.Set;

@Repository
public interface StockUnitRepository extends EntityRepository<StockUnit, Integer> {

    @Override
    List<StockUnit> findAll();

    Set<StockUnit> findStockUnitByNameContains(String query);

}
