package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseTableService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.util.PageUtil;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StockUnitService extends BaseTableService<StockUnit> {

    private StockUnitRepository stockUnitRepository;

    public ObservableList<StockUnit> getStockUnits() {
        List<StockUnit> stockUnits = stockUnitRepository.findAll();
        stockUnits.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(stockUnits, ArrayList::new));
    }

    @Override
    public Page<StockUnit> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = PageUtil.requestPage(page, itemPerPage);
        return stockUnitRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public Set<StockUnit> findItems(String query) {
        return stockUnitRepository.findStockUnitByNameContains(query);
    }

    public Optional<StockUnit> findStockUnitByProduct(ObservableList<StockUnit> items, Product product) {
        Optional<StockUnit> search = Optional.empty();
/*        if (product.getStockUnit() != null) {
            search = items.stream()
                    .filter(item -> item.getId().equals(product.getStockUnit().getId()))
                    .findAny();
        }*/
        return search;
    }

    @Override
    public boolean deleteRowItem(StockUnit stockUnit) {
        stockUnitRepository.delete(stockUnit);
        Optional<StockUnit> search = stockUnitRepository.findById(stockUnit.getId());
        return search.isEmpty();
    }

    @Override
    public StockUnit saveRowItem(StockUnit stockUnit) {
        return stockUnitRepository.save(stockUnit);
    }

    public StockUnit createDefault() {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setName("");
        stockUnit.setId(0);
        return stockUnit;
    }

    @Override
    public boolean isNew(StockUnit stockUnit) {
        return stockUnitRepository.findById(stockUnit.getId()).isEmpty();
    }

    @Autowired
    public void setCategoryRepository(StockUnitRepository stockUnitRepository) {
        this.stockUnitRepository = stockUnitRepository;
    }

}

