package ph.parcs.rmhometiles.entity.user;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.util.AppConstant;


@Entity
@Table(name = "users")
@Access(AccessType.PROPERTY)
public class User extends BaseEntity {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<AppConstant.Role> role = new SimpleObjectProperty<>();

    @Column(name = "username", unique = true)
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }


    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Enumerated(EnumType.STRING)
    public AppConstant.Role getRole() {
        return role.get();
    }

    public void setRole(AppConstant.Role role) {
        this.role.set(role);
    }



}
