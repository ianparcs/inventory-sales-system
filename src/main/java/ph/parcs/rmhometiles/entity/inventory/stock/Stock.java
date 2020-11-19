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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Integer getUnitSold() {
        return unitSold;
    }

    public void setUnitSold(Integer unitSold) {
        this.unitSold = unitSold;
    }

    @ManyToOne
    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }
}
