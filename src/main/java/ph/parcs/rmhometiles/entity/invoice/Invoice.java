package ph.parcs.rmhometiles.entity.invoice;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "invoice_id"))
public class Invoice extends BaseEntity {

    private ObjectProperty<Money> totalAmountDue = new SimpleObjectProperty<>();
    private ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private ObjectProperty<Money> totalAmount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> taxAmount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> discount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> amount = new SimpleObjectProperty<>();

    private ListProperty<OrderItem> orderItems = new SimpleListProperty<>();

    @ManyToOne
    @JoinTable(name = "customer_invoice", joinColumns =
            {@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id", referencedColumnName = "customer_id")})
    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer.set(customer);
    }

    @Column(name = "amount", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getAmount() {
        return amount.get();
    }

    public void setAmount(Money amount) {
        this.amount.set(amount);
    }

    @Column(name = "amount_due", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getTotalAmountDue() {
        return totalAmountDue.get();
    }

    public void setTotalAmountDue(Money totalAmountDue) {
        this.totalAmountDue.set(totalAmountDue);
    }

    @Column(name = "total_amount", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    @Column(name = "discount_amount", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getDiscount() {
        return discount.get();
    }

    public void setDiscount(Money discount) {
        this.discount.set(discount);
    }

    @Column(name = "tax_amount", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getTaxAmount() {
        return taxAmount.get();
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount.set(taxAmount);
    }

    @Transient
    public ObservableList<OrderItem> getOrderItems() {
        return orderItems.get();
    }

    public void setOrderItems(ObservableList<OrderItem> orderItems) {
        this.orderItems.set(orderItems);
    }

    @Transient
    public ObjectProperty<Money> taxAmountProperty() {
        return taxAmount;
    }

    @Transient
    public ObjectProperty<Money> discountProperty() {
        return discount;
    }

    @Transient
    public ObjectProperty<Money> amountProperty() {
        return amount;
    }

    @Transient
    public ObjectProperty<Money> totalAmountDueProperty() {
        return totalAmountDue;
    }

    @Transient
    public ObjectProperty<Money> totalAmountProperty() {
        return totalAmount;
    }
}
