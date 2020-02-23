package ph.parcs.rmhometiles.entity.customer;

import ph.parcs.rmhometiles.entity.user.Person;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "customer_id"))
public class Customer extends Person {

}
