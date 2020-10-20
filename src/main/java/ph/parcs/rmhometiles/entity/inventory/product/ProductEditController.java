package ph.parcs.rmhometiles.entity.inventory.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.category.CategoryService;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.stock.StockUnit;
import ph.parcs.rmhometiles.entity.inventory.stock.StockUnitService;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.entity.supplier.SupplierService;
import ph.parcs.rmhometiles.file.FileImage;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.util.FileUtils;

import java.io.File;
import java.util.Optional;

@Controller
public class ProductEditController extends EditItemController<Product> {

    @FXML
    private JFXComboBox<StockUnit> cbStockUnit;
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
    private JFXTextField tfStock;
    @FXML
    private JFXTextField tfImage;
    @FXML
    private JFXTextField tfPrice;
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextField tfCost;
    @FXML
    private JFXTextField tfCode;

    private StockUnitService stockUnitService;
    private CategoryService categoryService;
    private SupplierService supplierService;
    private FileService fileService;

    @FXML
    public void initialize() {
        super.initialize();
        validateField(tfDescription);
        validateField(tfDiscount);
        validateField(tfUnitSold);
        validateField(tfStock);
        validateField(tfPrice);
        validateField(tfName);
        validateField(tfCode);
        validateField(tfCost);

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
        cbStockUnit.setConverter(new StringConverter<>() {
            @Override
            public String toString(StockUnit stockUnit) {
                return stockUnit.getName();
            }

            @Override
            public StockUnit fromString(String s) {
                return null;
            }
        });
    }

    @Override
    protected void clearFields() {
        cbStockUnit.getSelectionModel().clearSelection();
        cbCategory.getSelectionModel().clearSelection();
        cbSupplier.getSelectionModel().clearSelection();
        tfDescription.clear();
        tfDiscount.clear();
        tfUnitSold.clear();
        tfStock.clear();
        tfImage.clear();
        tfPrice.clear();
        tfName.clear();
        tfCode.clear();
        tfCost.clear();
        clearValidators();
    }

    private void clearValidators() {
        tfDescription.resetValidation();
        tfDiscount.resetValidation();
        tfUnitSold.resetValidation();
        tfStock.resetValidation();
        tfName.resetValidation();
        tfPrice.resetValidation();
        tfCode.resetValidation();
    }

    @Override
    protected void bindFields(Product product) {
        if (!itemService.isNew(product)) {
            tfCode.setText(product.getCode());
            tfName.setText(product.getName());
            tfCost.setText(product.getCost().toString());
            tfDescription.setText(product.getDescription());
            tfPrice.setText(product.getPrice().toString());
            tfStock.setText(product.getStock().toString());
            tfDiscount.setText(product.getDiscount().toString());
            tfUnitSold.setText(product.getUnitSold().toString());

            if (product.getFileImage() != null) {
                tfImage.setText(product.getFileImage().getPath());
            }
        }

        setStockUnitValue(product);
        setCategoryValue(product);
        setSupplierValue(product);
    }

    @Override
    protected Product unbindFields(Integer id) {
        Product product = new Product();
        product.setUnitSold(Integer.valueOf(!tfUnitSold.getText().isEmpty() ? tfUnitSold.getText() : "0"));
        product.setDiscount(Integer.valueOf(!tfDiscount.getText().isEmpty() ? tfDiscount.getText() : "0"));
        product.setStock(Integer.valueOf(!tfStock.getText().isEmpty() ? tfStock.getText() : "0"));
        product.setPrice(Float.valueOf(!tfPrice.getText().isEmpty() ? tfPrice.getText() : "0.00"));
        product.setPrice(Float.valueOf(!tfCost.getText().isEmpty() ? tfCost.getText() : "0.00"));
        product.setStockUnit(cbStockUnit.getValue());
        product.setDescription(tfDescription.getText());
        product.setCategory(cbCategory.getValue());
        product.setSupplier(cbSupplier.getValue());
        product.setName(tfName.getText());
        product.setCode(tfCode.getText());
        product.setId(id);

        FileImage image = new FileImage();
        image.setPath(tfImage.getText());
        product.setFileImage(image);

        return product;
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

    private void setStockUnitValue(Product product) {
        cbStockUnit.getItems().setAll(stockUnitService.getStockUnits());
        Optional<StockUnit> search = stockUnitService.findStockUnitByProduct(cbStockUnit.getItems(), product);
        search.ifPresent(supplier -> cbStockUnit.getSelectionModel().select(search.get()));
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = FileUtils.getImageChooser();
        File selectedFile = fileChooser.showOpenDialog(editDialog.getScene().getWindow());
        if (selectedFile != null) tfImage.setText(selectedFile.getAbsolutePath());
    }

    @FXML
    private void clearSupplier() {
        clearComboboxSelection(cbSupplier);
    }

    @FXML
    private void clearCategory() {
        clearComboboxSelection(cbCategory);
    }

    @FXML
    private void clearStockUnit() {
        clearComboboxSelection(cbStockUnit);
    }

    private void clearComboboxSelection(JFXComboBox<?> comboBox) {
        BaseEntity entity = (BaseEntity) comboBox.getValue();
        if (entity != null) {
            if (entity.getName().isEmpty()) {
                Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
            }
        }
    }

    @Autowired
    public void setStockUnitService(StockUnitService stockUnitService) {
        this.stockUnitService = stockUnitService;
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

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

}
