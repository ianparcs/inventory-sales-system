package ph.parcs.rmhometiles.entity.inventory.stock.unit;


import jakarta.persistence.*;
import lombok.Setter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;

import java.util.List;

@Setter
@Entity
@Access(AccessType.PROPERTY)
public class StockUnit extends BaseEntity {

    private List<Stock> stocks;

    @OneToMany(mappedBy = "stockUnit", fetch = FetchType.EAGER)
    public List<Stock> getStocks() {
        return stocks;
    }

}
