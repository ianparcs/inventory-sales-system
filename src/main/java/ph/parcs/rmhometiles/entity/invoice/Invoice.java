package ph.parcs.rmhometiles.entity.invoice;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.order.OrderItem;
import ph.parcs.rmhometiles.entity.payment.Payment;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "invoice_id"))
public class Invoice extends BaseEntity {

    private ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private ObjectProperty<Money> totalAmount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> taxAmount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> discount = new SimpleObjectProperty<>();
    private ObjectProperty<Money> balance = new SimpleObjectProperty<>();
    private ObjectProperty<Money> change = new SimpleObjectProperty<>();
    private ObjectProperty<Money> amount = new SimpleObjectProperty<>();
    private StringProperty remarks = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty();

    private Set<OrderItem> orderItems = new HashSet<>();
    private Set<Payment> payments = new HashSet<>();

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

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Column(name = "amount", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getAmount() {
        return amount.get();
    }

    public void setAmount(Money amount) {
        this.amount.set(amount);
    }

    @Column(name = "balance", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getBalance() {
        return balance.get();
    }

    public void setBalance(Money balance) {
        this.balance.set(balance);
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

    @Column(name = "remarks")
    public String getRemarks() {
        return remarks.get();
    }

    public void setRemarks(String remarks) {
        this.remarks.set(remarks);
    }

    @Column(name = "status")
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    @Column(name = "change", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getChange() {
        return change.get();
    }

    public void setChange(Money change) {
        this.change.set(change);
    }

    public void addPayments(Payment payment) {
        if (payment != null && payments != null) {
            payments.add(payment);
        }
    }

    @Transient
    public Money getItemCosts() {
        Money cost = Money.parse("PHP 0.00");
        for (OrderItem orderItem : orderItems) {
            cost = cost.plus(orderItem.getProduct().getCost().multipliedBy(orderItem.getQuantity()));
        }
        return cost;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    @Transient
    public LocalDate getCreatedLocalDate() {
        return getCreatedAt().toLocalDate();
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
    public StringProperty statusProperty() {
        return status;
    }

    @Transient
    public ObjectProperty<Money> balanceProperty() {
        return balance;
    }

    @Transient
    public ObjectProperty<Money> totalAmountProperty() {
        return totalAmount;
    }

    @Transient
    public ObjectProperty<Money> changeProperty() {
        return change;
    }

}
