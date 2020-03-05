package ph.parcs.rmhometiles.entity.customer;

import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.user.Person;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends Person {

    private Set<Invoice> invoices;

    @OneToMany(mappedBy = "customer")
    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
}
