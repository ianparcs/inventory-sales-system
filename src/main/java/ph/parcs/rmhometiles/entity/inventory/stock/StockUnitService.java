package ph.parcs.rmhometiles.entity.inventory.stock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.ItemService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductRepository;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StockUnitService extends ItemService<StockUnit> {

    private StockUnitRepository stockUnitRepository;
    private ProductRepository productRepository;

    public ObservableList<StockUnit> getStockUnits() {
        List<StockUnit> stockUnits = stockUnitRepository.findAll();
        stockUnits.add(0, createDefault());
        return FXCollections.observableArrayList(Objects.requireNonNullElseGet(stockUnits, ArrayList::new));
    }

    @Override
    public Page<StockUnit> findPages(int page, int itemPerPage, String name) {
        PageRequest pageRequest = super.requestPage(page, itemPerPage);
        return stockUnitRepository.findAllByNameContains(pageRequest, name);
    }

    @Override
    public Set<StockUnit> findItems(String query) {
        return stockUnitRepository.findStockUnitByNameContains(query);
    }

    public Optional<StockUnit> findStockUnitByProduct(ObservableList<StockUnit> items, Product product) {
        Optional<StockUnit> search = Optional.empty();
        if (product.getStockUnit() != null) {
            search = items.stream()
                    .filter(item -> item.getId().equals(product.getStockUnit().getId()))
                    .findAny();
        }
        return search;
    }

    @Override
    public boolean deleteItem(StockUnit stockUnit) {
        StockUnit cleared = removeStockUnitsOfProduct(stockUnit);
        stockUnitRepository.delete(cleared);
        Optional<StockUnit> search = stockUnitRepository.findById(stockUnit.getId());
        return search.isEmpty();
    }

    private StockUnit removeStockUnitsOfProduct(StockUnit stockUnit) {
        Set<Product> productSet = productRepository.findProductsByStockUnit(stockUnit);
        if (productSet != null) {
            for (Product product : productSet) {
                product.setStockUnit(null);
            }
        }
        stockUnit.setProducts(null);
        return stockUnit;
    }

    @Override
    public StockUnit saveItem(StockUnit stockUnit) {
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

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}

