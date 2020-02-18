package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.*;
import ph.parcs.rmhometiles.entity.BaseEntity;
import ph.parcs.rmhometiles.entity.Category;
import ph.parcs.rmhometiles.entity.Supplier;
import ph.parcs.rmhometiles.entity.inventory.unit.QuantityUnit;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends BaseEntity {

    private IntegerProperty quantity = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();

    private IntegerProperty discount = new SimpleIntegerProperty();
    private FloatProperty price = new SimpleFloatProperty();
    private FloatProperty cost = new SimpleFloatProperty();

    private StringProperty description = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty filePath = new SimpleStringProperty();

    private Supplier supplier;
    private Category category;
    private QuantityUnit quantityUnit;

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    @Column(name = "unit_sold")
    public Integer getUnitSold() {
        return unitSold.get();
    }

    public void setUnitSold(Integer unitSold) {
        this.unitSold.set(unitSold);
    }

    public Float getCost() {
        return cost.get();
    }

    @Column(name = "cost")
    public void setCost(Float cost) {
        this.cost.set(cost);
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

    @ManyToOne
    @JoinColumn(name = "quantity_unit_id", referencedColumnName = "quantity_unit_id")
    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(QuantityUnit quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getFilePath() {
        return filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }
}
