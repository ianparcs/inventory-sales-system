package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.property.*;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.stock.StockUnit;
import ph.parcs.rmhometiles.entity.invoice.InvoiceLineItem;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.Image;

import javax.persistence.*;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseEntity {

    private IntegerProperty stock = new SimpleIntegerProperty();
    private IntegerProperty unitSold = new SimpleIntegerProperty();

    private FloatProperty price = new SimpleFloatProperty();
    private FloatProperty cost = new SimpleFloatProperty();

    private StringProperty description = new SimpleStringProperty();

    private StringProperty code = new SimpleStringProperty();

    private Set<InvoiceLineItem> invoiceLineItem;
    private StockUnit stockUnit;
    private Supplier supplier;
    private Category category;
    private Image image;

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
    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
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
    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setInvoiceLineItem(Set<InvoiceLineItem> invoiceLineItem) {
        this.invoiceLineItem = invoiceLineItem;
    }

    @OneToMany(mappedBy = "product")
    public Set<InvoiceLineItem> getInvoiceLineItem() {
        return invoiceLineItem;
    }
}
