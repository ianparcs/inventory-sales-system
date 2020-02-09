package ph.parcs.rmhometiles.category;

import org.hibernate.annotations.Cascade;
import ph.parcs.rmhometiles.item.Item;
import ph.parcs.rmhometiles.product.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends Item {

    private Set<Product> products;

    @OneToMany(mappedBy = "category")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
