package ph.parcs.rmhometiles.entity.inventory.stock;

import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnit;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Access(AccessType.PROPERTY)
public class Stock {

    private Integer id;
    private Integer stocks;
    private Integer unitSold;

    private StockUnit stockUnit;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    public Integer getId() {
        return id;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setUnitSold(Integer unitSold) {
        this.unitSold = unitSold;
    }

    public Integer getUnitSold() {
        return unitSold;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    @ManyToOne
    public StockUnit getStockUnit() {
        return stockUnit;
    }
}
