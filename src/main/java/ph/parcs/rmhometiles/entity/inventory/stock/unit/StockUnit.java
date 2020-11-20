package ph.parcs.rmhometiles.entity.inventory.stock.unit;


import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;

import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
public class StockUnit extends BaseEntity {

    private List<Stock> stocks;

    @OneToMany(mappedBy = "stockUnit", fetch = FetchType.EAGER)
    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
