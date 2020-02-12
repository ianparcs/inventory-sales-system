package ph.parcs.rmhometiles.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.category.CategoryService;
import ph.parcs.rmhometiles.item.EditItemController;
import ph.parcs.rmhometiles.item.Item;
import ph.parcs.rmhometiles.supplier.Supplier;
import ph.parcs.rmhometiles.supplier.SupplierService;

import java.util.Optional;

@Controller
public class ProductEditController extends EditItemController<Product> {

    @FXML
    private JFXComboBox<Supplier> cbSupplier;
    @FXML
    private JFXComboBox<Category> cbCategory;
    @FXML
    private JFXTextField tfDescription;
    @FXML
    private JFXTextField tfDiscount;
    @FXML
    private JFXTextField tfUnitSold;
    @FXML
    private JFXTextField tfQuantity;
    @FXML
    private JFXTextField tfPrice;
    @FXML
    private JFXTextField tfName;

    private CategoryService categoryService;
    private SupplierService supplierService;

    @FXML
    public void initialize() {
        super.initialize();
        validateField(tfDescription);
        validateField(tfDiscount);
        validateField(tfUnitSold);
        validateField(tfQuantity);
        validateField(tfPrice);
        validateField(tfName);

        initComboBoxValues();
    }

    private void initComboBoxValues() {
        cbCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category.getName();
            }

            @Override
            public Category fromString(String s) {
                return null;
            }
        });
        cbSupplier.setConverter(new StringConverter<>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier.getName();
            }

            @Override
            public Supplier fromString(String s) {
                return null;
            }
        });
    }

    @Override
    protected void clearFields() {
        cbCategory.getSelectionModel().clearSelection();
        cbSupplier.getSelectionModel().clearSelection();
        tfDescription.clear();
        tfDiscount.clear();
        tfUnitSold.clear();
        tfQuantity.clear();
        tfPrice.clear();
        tfName.clear();
        clearValidators();
    }

    protected void clearValidators() {
        tfDescription.resetValidation();
        tfDiscount.resetValidation();
        tfUnitSold.resetValidation();
        tfQuantity.resetValidation();
        tfName.resetValidation();
        tfPrice.resetValidation();
    }

    @Override
    protected void bindFields(Product product) {
        if (!itemService.isNew(product)) {
            tfName.setText(product.getName());
            tfDescription.setText(product.getDescription());
            tfPrice.setText(product.getPrice().toString());
            tfUnitSold.setText(product.getUnitSold().toString());
            tfQuantity.setText(product.getQuantity().toString());
            tfDiscount.setText(product.getDiscount().toString());
        }
        setCategoryValue(product);
        setSupplierValue(product);
    }

    private void setCategoryValue(Product product) {
        cbCategory.getItems().setAll(categoryService.getCategories());
        Optional<Category> search = categoryService.findCategoryByProduct(cbCategory.getItems(), product);
        search.ifPresent(category -> cbCategory.getSelectionModel().select(search.get()));
    }

    private void setSupplierValue(Product product) {
        cbSupplier.getItems().setAll(supplierService.getSuppliers());
        Optional<Supplier> search = supplierService.findSupplierByProduct(cbSupplier.getItems(), product);
        search.ifPresent(supplier -> cbSupplier.getSelectionModel().select(search.get()));
    }

    @FXML
    public void onSupplierSelect() {
        clearComboboxSelection(cbSupplier);
    }

    @FXML
    public void onCategorySelect() {
        clearComboboxSelection(cbCategory);
    }

    private void clearComboboxSelection(JFXComboBox<?> comboBox) {
        Item item = (Item) comboBox.getValue();
        if (item != null) {
            if (item.getName().isEmpty()) {
                Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
            }
        }
    }

    @Override
    protected Product unbindFields(Integer id) {
        Product product = new Product();
        product.setUnitSold(Integer.valueOf(!tfUnitSold.getText().isEmpty() ? tfUnitSold.getText() : "0"));
        product.setQuantity(Integer.valueOf(!tfQuantity.getText().isEmpty() ? tfQuantity.getText() : "0"));
        product.setDiscount(Integer.valueOf(!tfDiscount.getText().isEmpty() ? tfDiscount.getText() : "0"));
        product.setPrice(Float.valueOf(!tfPrice.getText().isEmpty() ? tfPrice.getText() : "0.00"));
        product.setDescription(tfDescription.getText());
        product.setCategory(cbCategory.getValue());
        product.setSupplier(cbSupplier.getValue());
        product.setName(tfName.getText());
        product.setId(id);
        return product;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.itemService = productService;
    }

}
