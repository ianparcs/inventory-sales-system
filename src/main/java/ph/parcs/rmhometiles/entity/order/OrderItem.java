package ph.parcs.rmhometiles.entity.order;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "order_item_id"))
public class OrderItem extends BaseEntity {

    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> amount = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final ObjectProperty<Invoice> invoice = new SimpleObjectProperty<>();

    public OrderItem(Product product) {
        this.product.set(product);
        amount.set(Money.parse("PHP 0.00"));
        quantity.addListener((observableValue, number, t1) -> amount.set(product.priceProperty().get().multipliedBy(quantity.get())));
    }

    public OrderItem() {

    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    @ManyToOne
    @JoinTable(name = "invoice_order_item",
            joinColumns = {@JoinColumn(name = "order_item_id", referencedColumnName = "order_item_id")},
            inverseJoinColumns = {@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")})
    public Invoice getInvoice() {
        return invoice.get();
    }

    public void setInvoice(Invoice invoice) {
        this.invoice.set(invoice);
    }

    @ManyToOne(fetch = FetchType.EAGER)
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
