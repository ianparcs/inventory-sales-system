package ph.parcs.rmhometiles.entity.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.user.Person;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Access(AccessType.PROPERTY)
@EqualsAndHashCode(callSuper = true)
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
