package ph.parcs.rmhometiles.entity.invoice.lineitems;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private IntegerProperty quantity = new SimpleIntegerProperty();

    public InvoiceLineItem(Product product) {
        this.product.set(product);
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
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getAmount() {
        return amount.get();
    }

    public void setAmount(Money amount) {
        this.amount.set(amount);
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    @Transient
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    @Transient
    public ObjectProperty<Product> productProperty() {
        return product;
    }

    @Transient
    public ObjectProperty<Money> amountProperty() {
        return amount;
    }

}
