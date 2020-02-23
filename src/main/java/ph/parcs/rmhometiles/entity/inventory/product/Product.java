package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.*;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.StockUnit;
import ph.parcs.rmhometiles.entity.supplier.Supplier;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends BaseEntity {

    private IntegerProperty stock = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();

    private IntegerProperty discount = new SimpleIntegerProperty();
    private FloatProperty price = new SimpleFloatProperty();
    private FloatProperty cost = new SimpleFloatProperty();


    private StringProperty description = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty filePath = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();

    private Supplier supplier;
    private Category category;
    private StockUnit stockUnit;

    @Column(name = "stock")
    public Integer getStock() {
        return stock.get();
    }

    public void setStock(Integer stock) {
        this.stock.set(stock);
    }

    public IntegerProperty stockProperty() {
        return stock;
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
    @JoinColumn(name = "stock_unit_id", referencedColumnName = "stock_unit_id")
    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public String getFilePath() {
        return filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    @Column(name = "code")
    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }
}
