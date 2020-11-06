package ph.parcs.rmhometiles.entity.invoice;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class InvoiceLineItem {

    private FloatProperty price = new SimpleFloatProperty();
    private IntegerProperty id = new SimpleIntegerProperty();
    private FloatProperty totalCost = new SimpleFloatProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();

    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(float price) {
        this.price.set(price);
    }

    @Column(name = "price")
    public float getPrice() {
        return price.get();
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost.set(totalCost);
    }

    @Column(name = "total_cost")
    public float getTotalCost() {
        return totalCost.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity.get();
    }
}
