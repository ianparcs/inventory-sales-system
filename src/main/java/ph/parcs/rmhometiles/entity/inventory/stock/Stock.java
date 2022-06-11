package ph.parcs.rmhometiles.entity.inventory.stock;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnit;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Access(AccessType.PROPERTY)
public class Stock {

    private IntegerProperty stocks = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();
    private Integer id;

    private StockUnit stockUnit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStocks() {
        return stocks.get();
    }

    public void setStocks(Integer stocks) {
        this.stocks.set(stocks);
    }

    public Integer getUnitSold() {
        return unitSold.get();
    }

    public void setUnitSold(Integer unitSold) {
        this.unitSold.set(unitSold);
    }

    @ManyToOne
    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }


}
