package ph.parcs.rmhometiles.file;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import javax.persistence.*;


@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "image_id"))
public class Image extends BaseEntity {

    private StringProperty path = new SimpleStringProperty();
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    @OneToOne(mappedBy = "image")
    public Product getProduct() {
        return product;
    }

    @Column(name = "path")
    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.set(path);
    }
}
