package ph.parcs.rmhometiles.category;

import ph.parcs.rmhometiles.item.Item;
import ph.parcs.rmhometiles.product.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends Item {

    private List<Product> products;

    @OneToMany(mappedBy = "category")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
