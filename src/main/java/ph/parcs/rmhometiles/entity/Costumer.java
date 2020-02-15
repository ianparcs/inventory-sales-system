package ph.parcs.rmhometiles.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "costumer_id"))
public class Costumer extends Person {

    private List<Product> products;


    @OneToMany(mappedBy = "supplier")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
