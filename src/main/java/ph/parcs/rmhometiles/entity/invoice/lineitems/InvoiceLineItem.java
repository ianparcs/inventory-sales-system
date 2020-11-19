package ph.parcs.rmhometiles.entity.invoice.lineitems;

import javafx.beans.property.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class InvoiceLineItem extends BaseEntity {

    private FloatProperty amountProperty = new SimpleFloatProperty();
    private FloatProperty priceProperty = new SimpleFloatProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();
    private StringProperty itemCode = new SimpleStringProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();
    private IntegerProperty id = new SimpleIntegerProperty();

    private Product product;
    private Money amount;
    private Money price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "amount")
    @Type(type = Global.JADIRA_PACKAGE)
    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    @Type(type = Global.JADIRA_PACKAGE)
    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public String getItemCode() {
        return itemCode.get();
    }

    public void setItemCode(String itemCode) {
        this.itemCode.set(itemCode);
    }

}
