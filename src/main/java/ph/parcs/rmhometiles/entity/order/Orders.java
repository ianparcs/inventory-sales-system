package ph.parcs.rmhometiles.entity.order;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "orders_id"))
public class Orders extends BaseEntity {

    private ObjectProperty<LocalDateTime> dateOrderPlaced = new SimpleObjectProperty<>();
    private ObjectProperty<Customer> customer;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer.set(customer);
    }

    @Column(name = "date_order_placed", columnDefinition = "TIMESTAMP")
    public LocalDateTime getDateOrderPlaced() {
        return dateOrderPlaced.get();
    }

    public ObjectProperty<LocalDateTime> dateOrderPlacedProperty() {
        return dateOrderPlaced;
    }

    public void setDateOrderPlaced(LocalDateTime dateOrderPlaced) {
        this.dateOrderPlaced.set(dateOrderPlaced);
    }
}
