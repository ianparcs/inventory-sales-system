package ph.parcs.rmhometiles.entity.user;

import jakarta.persistence.*;
import javafx.beans.property.*;
import ph.parcs.rmhometiles.util.AppConstant;


@Entity
@Table(name = "users")
@Access(AccessType.PROPERTY)
public class User {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<AppConstant.Role> role = new SimpleObjectProperty<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    @Column(name = "username", unique = true)
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    @Enumerated(EnumType.STRING)
    public AppConstant.Role getRole() {
        return role.get();
    }

    public void setRole(AppConstant.Role role) {
        this.role.set(role);
    }

    public ObjectProperty<AppConstant.Role> roleProperty() {
        return role;
    }


}
