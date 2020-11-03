package ph.parcs.rmhometiles.entity.inventory.item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class BaseEntity {

    protected IntegerProperty id = new SimpleIntegerProperty();
    protected StringProperty name = new SimpleStringProperty();

    @Column(name = "name")
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

}
