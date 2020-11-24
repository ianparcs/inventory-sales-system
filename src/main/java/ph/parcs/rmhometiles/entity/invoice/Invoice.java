package ph.parcs.rmhometiles.entity.invoice;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.entity.order.Orders;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "invoice_id"))
public class Invoice extends BaseEntity {

    private ListProperty<InvoiceLineItem> invoiceLineItems = new SimpleListProperty<>();
    private ObjectProperty<Money> totalPrice = new SimpleObjectProperty<>();
    private ObjectProperty<Date> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<Orders> order = new SimpleObjectProperty<>();

    @Column(name = "total_price", precision = 8, scale = 2)
    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    @OneToOne
    @JoinColumn(name = "orders_id", referencedColumnName = "orders_id")
    public Orders getOrder() {
        return order.get();
    }

    public void setOrder(Orders order) {
        this.order.set(order);
    }

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt.set(createdAt);
    }


    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    @Transient
    public String getName() {
        return super.getName();
    }
}
