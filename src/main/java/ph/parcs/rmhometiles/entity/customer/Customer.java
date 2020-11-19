package ph.parcs.rmhometiles.entity.customer;

import ph.parcs.rmhometiles.entity.order.Orders;
import ph.parcs.rmhometiles.entity.user.Person;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends Person {

    private Set<Orders> orders;

    @OneToMany(mappedBy = "customer")
    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }
}
