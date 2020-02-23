package ph.parcs.rmhometiles.entity.supplier;

import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.user.Person;

import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "supplier_id"))
public class Supplier extends Person {

    private List<Product> products;

    @OneToMany(mappedBy = "supplier")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
