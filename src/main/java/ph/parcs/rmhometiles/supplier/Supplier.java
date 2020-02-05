package ph.parcs.rmhometiles.supplier;

import javafx.beans.property.*;
import ph.parcs.rmhometiles.Item;
import ph.parcs.rmhometiles.product.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
public class Supplier extends Item {

    private StringProperty contact = new SimpleStringProperty();
    private List<Product> products;

    @Column(name = "contact")
    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    @OneToMany(mappedBy = "supplier")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
