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

    private ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private ObjectProperty<Money> amount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> price = new SimpleObjectProperty<>();
    private IntegerProperty quantity = new SimpleIntegerProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();
    private StringProperty code = new SimpleStringProperty();
    private IntegerProperty id = new SimpleIntegerProperty();

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
        return product.get();
    }

    public void setProduct(Product product) {
        this.product.set(product);
    }

    @Column(name = "amount")
    @Type(type = Global.JADIRA_PACKAGE)
    public Money getAmount() {
        return amount.get();
    }

    public void setAmount(Money amount) {
        this.amount.set(amount);
    }

    @Type(type = Global.JADIRA_PACKAGE)
    public Money getPrice() {
        return price.get();
    }

    public void setPrice(Money price) {
        this.price.set(price);
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

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

}
