package ph.parcs.rmhometiles.item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class Item implements Serializable {

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

}
