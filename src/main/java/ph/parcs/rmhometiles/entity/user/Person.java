package ph.parcs.rmhometiles.entity.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public class Person extends BaseEntity {

    public StringProperty contact = new SimpleStringProperty();
    public StringProperty address = new SimpleStringProperty();

    @Column(name = "contact")
    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    @Column(name = "address")
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
