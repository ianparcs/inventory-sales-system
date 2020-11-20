package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.inventory.stock.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StockUnitService extends BaseService<StockUnit> {

    private StockUnitRepository stockUnitRepository;
    private StockRepository stockRepository;

    public ObservableList<StockUnit> getStockUnits() {
        List<StockUnit> stockUnits = stockUnitRepository.findAll();
        //   stockUnits.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(stockUnits, ArrayList::new));
    }

    public Optional<StockUnit> findStockUnitByProduct(ObservableList<StockUnit> items, Product product) {
        Optional<StockUnit> search = Optional.empty();
        if (product.getStock() != null && product.getStock().getStockUnit() != null) {
            search = items.stream()
                    .filter(item -> item.getId().equals(product.getStock().getStockUnit().getId()))
                    .findAny();
        }
        return search;
    }

    @Override
    public boolean deleteEntity(StockUnit stockUnit) {
        List<Stock> stocks = stockRepository.findAllByStockUnit(stockUnit);
        for (Stock stock : stocks) {
            stock.setStockUnit(null);
            stockRepository.save(stock);
        }
        stockUnit.setStocks(stocks);
        stockUnitRepository.delete(stockUnit);
        Optional<StockUnit> search = stockUnitRepository.findById(stockUnit.getId());
        return search.isEmpty();
    }

    public StockUnit createDefault() {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setName("");
        stockUnit.setId(0);
        return stockUnit;
    }

    @Autowired
    public void setCategoryRepository(StockUnitRepository stockUnitRepository) {
        this.entityRepository = stockUnitRepository;
        this.stockUnitRepository = (StockUnitRepository) entityRepository;
    }

    @Autowired
    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}

