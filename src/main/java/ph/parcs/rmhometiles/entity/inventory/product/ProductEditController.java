package ph.parcs.rmhometiles.entity.inventory.product;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import ph.parcs.rmhometiles.util.converter.MoneyConverter;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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
        tfStock.resetValidation();
        tfName.resetValidation();
        tfPrice.resetValidation();
        tfCode.resetValidation();
        tfCost.resetValidation();
    }

    @Override
    protected void bindFields(Product product) {
        if (baseService.isExist(product.getId())) {
            tfName.setText(product.getName());
            tfCode.setText(product.getCode());
            tfCost.setText(product.getCost().getAmount().toString());
            tfPrice.setText(product.getPrice().getAmount().toString());
            tfDescription.setText(product.getDescription());
            tfStock.setText(product.getStock().getStocks().toString());

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
        stock.setStocks(Integer.valueOf(!tfStock.getText().isEmpty() ? tfStock.getText() : "0"));

        if (!cbStockUnit.getSelectionModel().isEmpty()) stock.setStockUnit(cbStockUnit.getValue());
        if (!cbCategory.getSelectionModel().isEmpty()) product.setCategory(cbCategory.getValue());
        if (!cbSupplier.getSelectionModel().isEmpty()) product.setSupplier(cbSupplier.getValue());

        product.setPrice(MoneyConverter.convert(tfPrice.getText()));
        product.setCost(MoneyConverter.convert(tfCost.getText()));
        product.setDescription(tfDescription.getText());
        product.setCode(tfCode.getText());
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
    public void onEditItem(ItemListener<Product> itemListener, Product product) {
        setDialogTitle(product);
        bindFields(product);

        btnSave.setOnAction(a -> {
            Service<Void> service = new Service<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            deleteFile(product);

                            Product savedItem = baseService.saveEntity(unbindFields(product.getId()));
                            CountDownLatch latch = new CountDownLatch(1);

                            Platform.runLater(() -> {
                                closeDialog();
                                if (savedItem != null) {
                                    itemListener.onSavedSuccess(savedItem);
                                } else {
                                    itemListener.onSaveFailed(null);
                                }
                                latch.countDown();
                            });
                            latch.await();
                            return null;
                        }
                    };
                }
            };
            service.start();
        });

    }

    private void deleteFile(Product product) {
        String path = tfImage.getText();
        if (!path.isEmpty() && product.getImageProduct() != null) {
            String fileName = FileUtils.getFileName(path);
            String currentFile = product.getImageProduct().getName();
            if ((!StringUtils.isEmpty(currentFile) && !currentFile.equals(fileName))) {
                fileService.deleteFile(product.getImageProduct().getName());
            }
        }
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
        this.baseService = productService;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
