package ph.parcs.rmhometiles.entity;

import javafx.beans.property.*;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends BaseEntity {

    private IntegerProperty quantity = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();

    private IntegerProperty discount = new SimpleIntegerProperty();
    private FloatProperty price = new SimpleFloatProperty();

    private StringProperty description = new SimpleStringProperty();
    private StringProperty imagePath = new SimpleStringProperty();

    private Supplier supplier;
    private Category category;


    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity.get();
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    @Column(name = "imagePath")
    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    @Column(name = "unit_sold")
    public Integer getUnitSold() {
        return unitSold.get();
    }

    public void setUnitSold(Integer unitSold) {
        this.unitSold.set(unitSold);
    }

    @Column(name = "discount")
    public Integer getDiscount() {
        return discount.get();
    }

    public void setDiscount(Integer discount) {
        this.discount.set(discount);
    }

    @Column(name = "price")
    public Float getPrice() {
        return price.get();
    }

    public void setPrice(Float price) {
        this.price.set(price);
    }

    @Column(name = "description")
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
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

}
