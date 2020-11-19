package ph.parcs.rmhometiles.entity.order;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ph.parcs.rmhometiles.entity.customer.Customer;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Orders {

    protected IntegerProperty id = new SimpleIntegerProperty();
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
