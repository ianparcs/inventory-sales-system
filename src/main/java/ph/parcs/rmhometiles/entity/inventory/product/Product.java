package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseEntity {

    private StringProperty description = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();

    private ImageProduct imageProduct;
    private Supplier supplier;
    private Category category;
    private ObjectProperty<Stock> stock = new SimpleObjectProperty<>();

    private ObjectProperty<Money> price = new SimpleObjectProperty<>();
    private ObjectProperty<Money> cost = new SimpleObjectProperty<>();

    @Column(name = "description")
    public String getDescription() {
        return description.get();
    }

    void setDescription(String description) {
        this.description.set(description);
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Stock getStock() {
        return stock.get();
    }

    public void setStock(Stock stock) {
        this.stock.set(stock);
    }

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "product_image",
            joinColumns =
                    {@JoinColumn(name = "product_id", referencedColumnName = "product_id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "image_id", referencedColumnName = "image_id")})
    ImageProduct getImageProduct() {
        return imageProduct;
    }

    void setImageProduct(ImageProduct imageProduct) {
        this.imageProduct = imageProduct;
    }

    @Column(name = "code")
    public String getCode() {
        return code.get();
    }

    void setCode(String code) {
        this.code.set(code);
    }

    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getPrice() {
        return price.get();
    }

    void setPrice(Money price) {
        this.price.set(price);
    }

    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    Money getCost() {
        return cost.get();
    }

    void setCost(Money cost) {
        this.cost.set(cost);
    }

    @Transient
    public StringProperty getCodeProperty() {
        return code;
    }

    @Transient
    public StringProperty getDescriptionProperty() {
        return description;
    }

    @Transient
    public ObjectProperty<Stock> stockProperty() {
        return stock;
    }

    @Transient
    public ObjectProperty<Money> priceProperty() {
        return price;
    }

}
