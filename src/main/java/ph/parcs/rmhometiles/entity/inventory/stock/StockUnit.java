package ph.parcs.rmhometiles.entity.inventory.stock;


import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
public class StockUnit extends BaseEntity {

    private Set<Product> products;

    @OneToMany(mappedBy = "stockUnit")
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
