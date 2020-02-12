package ph.parcs.rmhometiles.product;

import javafx.beans.property.*;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.item.Item;
import ph.parcs.rmhometiles.supplier.Supplier;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends Item {

    private IntegerProperty quantity = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();

    private IntegerProperty discount = new SimpleIntegerProperty();
    private FloatProperty price = new SimpleFloatProperty();

    private StringProperty description = new SimpleStringProperty();

    private Supplier supplier;
    private Category category;


    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity.get();
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
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
