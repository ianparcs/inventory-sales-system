package ph.parcs.rmhometiles.entity.order;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "orders_id"))
public class Orders extends BaseEntity {

    private ObjectProperty<Customer> customer = new SimpleObjectProperty<>();

    @ManyToOne
    @JoinTable(name = "customer_orders", joinColumns =
            {@JoinColumn(name = "orders_id", referencedColumnName = "orders_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id", referencedColumnName = "customer_id")})
    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer.set(customer);
    }

}
