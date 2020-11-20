package ph.parcs.rmhometiles.entity.inventory.stock;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnit;

import java.util.List;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

        List<Stock> findAllByStockUnit(StockUnit stockUnit);
}
