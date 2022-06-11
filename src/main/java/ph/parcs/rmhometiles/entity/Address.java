package ph.parcs.rmhometiles.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Address {

    protected IntegerProperty id = new SimpleIntegerProperty();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

}
