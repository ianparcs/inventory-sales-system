package ph.parcs.rmhometiles.entity.inventory.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.ItemListener;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.category.CategoryService;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnit;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnitService;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.entity.supplier.SupplierService;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.util.FileUtils;
import ph.parcs.rmhometiles.util.MoneyConverter;

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
        validateField(tfUnitSold);
        validateField(tfStock);
        validateField(tfName);
        validateField(tfCode);

        validateNumberField(tfPrice);
        validateNumberField(tfCost);

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
        tfUnitSold.resetValidation();
        tfStock.resetValidation();
        tfName.resetValidation();
        tfPrice.resetValidation();
        tfCode.resetValidation();
        tfCost.resetValidation();
    }

    @Override
    protected void bindFields(Product product) {
        if (!baseTableService.isNew(product)) {
            tfName.setText(product.getName());
            tfCode.setText(product.getCodeProperty());
            tfCost.setText(product.getCost().getAmount().toString());
            tfPrice.setText(product.getPrice().getAmount().toString());
            tfDescription.setText(product.getDescriptionProperty());
            tfStock.setText(product.getStock().getStocks().toString());
            tfUnitSold.setText(product.getStock().getUnitSold().toString());

            if (product.getImageProduct() != null) {
                tfImage.setText(product.getImageProduct().getPath());
            }

        }

        setStockUnitValue(product);
        setCategoryValue(product);
        setSupplierValue(product);
    }

    @Override
    protected Product unbindFields(Integer id) {
        Product product = new Product();

        Stock stock = new Stock();
        stock.setUnitSold(Integer.valueOf(!tfUnitSold.getText().isEmpty() ? tfUnitSold.getText() : "0"));
        stock.setStocks(Integer.valueOf(!tfStock.getText().isEmpty() ? tfStock.getText() : "0"));

        if (!cbStockUnit.getSelectionModel().isEmpty()) stock.setStockUnit(cbStockUnit.getValue());
        if (!cbCategory.getSelectionModel().isEmpty()) product.setCategory(cbCategory.getValue());
        if (!cbSupplier.getSelectionModel().isEmpty()) product.setSupplier(cbSupplier.getValue());

        product.setPrice(MoneyConverter.convert(tfPrice.getText()));
        product.setCost(MoneyConverter.convert(tfCost.getText()));
        product.setDescriptionProperty(tfDescription.getText());
        product.setCodeProperty(tfCode.getText());
        product.setName(tfName.getText());
        product.setStock(stock);
        product.setId(id);

        if (!StringUtils.isEmpty(tfImage.getText())) {
            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setPath(tfImage.getText());
            product.setImageProduct(imageProduct);
        }

        return product;
    }

    private void setCategoryValue(Product product) {
        ObservableList<Category> items = categoryService.getCategories();
        if (items.isEmpty()) return;
        cbCategory.getItems().setAll(items);
        Optional<Category> search = categoryService.findCategoryByProduct(cbCategory.getItems(), product);
        search.ifPresent(category -> cbCategory.getSelectionModel().select(search.get()));
    }

    private void setSupplierValue(Product product) {
        ObservableList<Supplier> items = supplierService.getSuppliers();
        if (items.isEmpty()) return;
        cbSupplier.getItems().setAll(items);
        Optional<Supplier> search = supplierService.findSupplierByProduct(cbSupplier.getItems(), product);
        search.ifPresent(supplier -> cbSupplier.getSelectionModel().select(search.get()));
    }

    private void setStockUnitValue(Product product) {
        ObservableList<StockUnit> items = stockUnitService.getStockUnits();
        if (items.isEmpty()) return;
        cbStockUnit.getItems().setAll(items);
        Optional<StockUnit> search = stockUnitService.findStockUnitByProduct(cbStockUnit.getItems(), product);
        search.ifPresent(supplier -> cbStockUnit.getSelectionModel().select(search.get()));
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = FileUtils.getImageChooser();
        File selectedFile = fileChooser.showOpenDialog(editDialog.getScene().getWindow());
        if (selectedFile != null) tfImage.setText(selectedFile.getAbsolutePath());
    }

    @Override
    public void onEditItem(ItemListener<Product> itemListener, Product item) {
        setDialogTitle(item);
        bindFields(item);

        btnSave.setOnAction(actionEvent -> {
            closeDialog();
            String path = tfImage.getText();
            if (!path.isEmpty() && item.getImageProduct() != null) {
                String fileName = FileUtils.getFileName(path);
                String currentFile = item.getImageProduct().getName();
                if ((currentFile != null && !currentFile.equals(fileName)) &&
                        !StringUtils.isEmpty(currentFile)) {
                    fileService.deleteFile(item.getImageProduct().getName());
                }
            }

            Product savedItem = baseTableService.saveRowItem(unbindFields(item.getId()));
            if (savedItem != null) {
                itemListener.onSavedSuccess(savedItem);
            } else {
                itemListener.onSaveFailed(null);
            }
        });
    }

    @FXML
    private void selectSupplier() {
        unSelectBlankOption(cbSupplier);
    }

    @FXML
    private void selectCategory() {
        unSelectBlankOption(cbCategory);
    }

    @FXML
    private void selectStockUnit() {
        unSelectBlankOption(cbStockUnit);
    }

    private void unSelectBlankOption(JFXComboBox<?> comboBox) {
        BaseEntity entity = (BaseEntity) comboBox.getValue();
        if (entity != null) {
            if (StringUtils.isEmpty(entity.getName())) {
                comboBox.getSelectionModel().clearSelection();
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
        this.baseTableService = productService;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
