package ph.parcs.rmhometiles.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.user.Person;

import java.util.Set;

@Setter
@Entity
@Access(AccessType.PROPERTY)
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends Person {

    private Set<Invoice> invoices;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    public Set<Invoice> getInvoices() {
        return invoices;
    }

}
