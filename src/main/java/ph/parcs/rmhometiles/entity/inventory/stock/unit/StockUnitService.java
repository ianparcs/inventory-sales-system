package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StockUnitService extends BaseService<StockUnit> {

    private StockUnitRepository stockUnitRepository;

    public ObservableList<StockUnit> getStockUnits() {
        List<StockUnit> stockUnits = stockUnitRepository.findAll();
        //   stockUnits.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(stockUnits, ArrayList::new));
    }

    @Override
    public Page<StockUnit> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return stockUnitRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public Set<StockUnit> findEntities(String query) {
        return stockUnitRepository.findStockUnitByNameContains(query);
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
        stockUnitRepository.delete(stockUnit);
        Optional<StockUnit> search = stockUnitRepository.findById(stockUnit.getId());
        return search.isEmpty();
    }

    @Override
    public StockUnit saveEntity(StockUnit stockUnit) {
        return stockUnitRepository.save(stockUnit);
    }

    public StockUnit createDefault() {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setName("");
        stockUnit.setId(0);
        return stockUnit;
    }


    @Autowired
    public void setCategoryRepository(StockUnitRepository stockUnitRepository) {
        this.stockUnitRepository = stockUnitRepository;
    }

}

