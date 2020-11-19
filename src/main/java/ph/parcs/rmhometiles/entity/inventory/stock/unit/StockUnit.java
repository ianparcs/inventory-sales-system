package ph.parcs.rmhometiles.entity.inventory.stock.unit;


import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
public class StockUnit extends BaseEntity {

    private Set<Stock> stocks;

    @OneToMany(mappedBy = "stockUnit")
    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }
}
