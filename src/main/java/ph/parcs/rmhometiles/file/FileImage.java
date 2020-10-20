package ph.parcs.rmhometiles.file;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.product.Product;

import javax.persistence.*;


@Entity
@Access(AccessType.PROPERTY)
public class FileImage extends BaseEntity {

    private StringProperty path = new SimpleStringProperty();
    private Product product;

    public void setProduct(Product product) {
        this.product = product;
    }

    @OneToOne(mappedBy = "fileImage")
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
