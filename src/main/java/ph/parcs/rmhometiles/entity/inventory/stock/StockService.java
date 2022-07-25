package ph.parcs.rmhometiles.entity.inventory.stock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnit;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StockService extends BaseService<StockUnit> {

    @Override
    public StockUnit createDefault() {
        return null;
    }

    public Stock computeStocks(Product product, int quantity) {
        Stock stock = product.getStock();

        Integer productStock = stock.getStocks() - quantity;
        Integer totalUnitSold = stock.getUnitSold() + quantity;

        stock.setStocks(productStock);
        stock.setUnitSold(totalUnitSold);

        return stock;
    }

}

