package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.Image;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseEntity {

    private StringProperty descriptionProperty = new SimpleStringProperty();
    private StringProperty codeProperty = new SimpleStringProperty();

    private Supplier supplier;
    private Category category;
    private Stock stock;
    private Image image;

    private Money price;
    private Money cost;

    @Column(name = "description")
    public String getDescriptionProperty() {
        return descriptionProperty.get();
    }

    void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty.set(descriptionProperty);
    }

    @ManyToOne
    @JoinTable(name = "product_category", joinColumns =
            {@JoinColumn(name = "product_id", referencedColumnName = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "category_id")})
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    void setStock(Stock stock) {
        this.stock = stock;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Stock getStock() {
        return stock;
    }

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_image",
            joinColumns =
                    {@JoinColumn(name = "product_id", referencedColumnName = "product_id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "image_id", referencedColumnName = "image_id")})
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Column(name = "code")
    public String getCodeProperty() {
        return codeProperty.get();
    }

    void setCodeProperty(String codeProperty) {
        this.codeProperty.set(codeProperty);
    }

    void setPrice(Money price) {
        this.price = price;
    }

    public Money getPrice() {
        return price;
    }

    void setCost(Money cost) {
        this.cost = cost;
    }

    public Money getCost() {
        return cost;
    }
}
