package ph.parcs.rmhometiles.entity.log;

import jakarta.persistence.*;
import lombok.Setter;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.entity.user.Person;
import ph.parcs.rmhometiles.entity.user.User;

import java.util.Set;

@Setter
@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "log_id"))
public class Log extends BaseEntity {

    private User user;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

}
