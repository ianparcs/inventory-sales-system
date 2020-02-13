package ph.parcs.rmhometiles.category;

import ph.parcs.rmhometiles.item.BaseEntity;
import ph.parcs.rmhometiles.product.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity {

    private Set<Product> products;

    @OneToMany(mappedBy = "category")
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
