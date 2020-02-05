package ph.parcs.rmhometiles.dialog;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.category.CategoryService;
import ph.parcs.rmhometiles.item.EditItemController;
import ph.parcs.rmhometiles.product.Product;
import ph.parcs.rmhometiles.supplier.Supplier;
import ph.parcs.rmhometiles.supplier.SupplierService;

@Controller
public class EditProductController extends EditItemController<Product> {

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
    private JFXTextField tfCode;

    private CategoryService categoryService;
    private SupplierService supplierService;

    @FXML
    private void initialize() {
        validateField(tfDescription);
        validateField(tfDiscount);
        validateField(tfUnitSold);
        validateField(tfQuantity);
        validateField(tfPrice);
        validateField(tfCode);

        initComboBoxValues();
    }

    private void initComboBoxValues() {
        cbCategory.setItems(categoryService.getCategories());
        cbCategory.getSelectionModel().selectFirst();
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

        cbSupplier.setItems(supplierService.getSuppliers());
        cbSupplier.getSelectionModel().selectFirst();
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
        tfDescription.clear();
        tfDiscount.clear();
        tfUnitSold.clear();
        tfQuantity.clear();
        tfCode.clear();
        tfPrice.clear();
    }

    @Override
    protected void bindFields(Product product) {
        tfCode.setText(product.getName());
        tfDescription.setText(product.getDescription());
        tfPrice.setText(product.getPrice().toString());
        tfUnitSold.setText(product.getUnitSold().toString());
        tfQuantity.setText(product.getQuantity().toString());
        tfDiscount.setText(product.getDiscount().toString());

        cbSupplier.getItems().forEach(supplier -> {
            Supplier prodSupplier = product.getSupplier();
            if (prodSupplier != null) {
                if (prodSupplier.getId().equals(supplier.getId())) {
                    cbSupplier.getSelectionModel().select(supplier);
                }
            }
        });

        cbCategory.getItems().forEach(category -> {
            Category prodCategory = product.getCategory();
            if (prodCategory != null) {
                if (prodCategory.getId().equals(category.getId())) {
                    cbCategory.getSelectionModel().select(category);
                }
            }
        });
    }

    @Override
    protected Product unbindFields(Product product) {
        product.setName(tfCode.getText());
        product.setCategory(cbCategory.getValue());
        product.setSupplier(cbSupplier.getValue());
        product.setDescription(tfDescription.getText());
        product.setPrice(Float.valueOf(!tfPrice.getText().isEmpty() ? tfPrice.getText() : "0.00"));
        product.setUnitSold(Integer.valueOf(!tfUnitSold.getText().isEmpty() ? tfUnitSold.getText() : "0"));
        product.setQuantity(Integer.valueOf(!tfQuantity.getText().isEmpty() ? tfQuantity.getText() : "0"));
        product.setDiscount(Integer.valueOf(!tfDiscount.getText().isEmpty() ? tfDiscount.getText() : "0"));
        clearFields();
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
}
