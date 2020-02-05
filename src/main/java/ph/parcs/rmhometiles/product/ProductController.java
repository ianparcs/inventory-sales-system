package ph.parcs.rmhometiles.product;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.custom.ActionTableCell;
import ph.parcs.rmhometiles.dialog.EditProductController;
import ph.parcs.rmhometiles.dialog.alert.SweetAlert;
import ph.parcs.rmhometiles.dialog.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.item.ItemController;
import ph.parcs.rmhometiles.supplier.Supplier;
import ph.parcs.rmhometiles.util.Constant;


@Controller
public class ProductController extends ItemController {

    @FXML
    private TableColumn<Product, Supplier> tcSupplier;
    @FXML
    private TableColumn<Product, Category> tcCategory;
    @FXML
    private TableColumn<Product, Integer> tcDiscount;
    @FXML
    private TableColumn<Product, Integer> tcStock;
    @FXML
    private TableColumn<Product, Float> tcPrice;
    @FXML
    private TableColumn<Product, HBox> tcAction;

    private EditProductController editProductController;
    private ProductService productService;
    private String searchValue = "";

    @FXML
    public void initialize() {
        updateItems();

        initTableColumnValue();
        initActionColumn();
        initPagination();

        tfSearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            searchValue = newValue;
            updateItems();
        });
    }

    private void initPagination() {
        pagination.currentPageIndexProperty().addListener((observable) -> updateItems());
    }

    private void initTableColumnValue() {
        tcSupplier.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Supplier supplier, boolean empty) {
                if (!empty) setText((supplier != null) ? supplier.getName() : "n/a");
            }
        });

        tcCategory.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Category category, boolean empty) {
                if (!empty) setText((category != null) ? category.getName() : "n/a");
            }
        });
        tcStock.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Integer stock, boolean empty) {
                if (!empty) setText(stock + " " + Constant.UNIT.PCS);
            }
        });

        tcPrice.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Float price, boolean empty) {
                if (!empty) setText(Constant.UNIT.PESO + String.format("%,.2f", price));
            }
        });

        tcDiscount.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Integer discount, boolean empty) {
                if (!empty) setText(discount + Constant.UNIT.PERCENT);
            }
        });
    }

    private void initActionColumn() {
        tcAction.setCellFactory(ActionTableCell.forColumn(
                this::actionDeleteProduct, this::actionEditProduct));
    }

    private Product actionEditProduct(Product product) {
        editProductController.onEditProduct(this::onSaveProduct, product);
        editProductController.showDialog((StackPane) tvProduct.getScene().getRoot());
        return product;
    }

    private Product actionDeleteProduct(Product product) {
        final StackPane root = (StackPane) tvProduct.getScene().getRoot();

        SweetAlert alert = SweetAlertFactory.deleteDialog();
        alert.setMessage("Are you sure you want to delete " + product.getName() + "?");
        alert.setConfirmListener(() -> {
            productService.deleteItem(product);
            if (productService.isItemEmpty(product)) {
                updateItems();
                SweetAlertFactory.successDialog(Constant.MSG.DELETE).show(root);
            }
        }).show(root);

        return product;
    }

    @FXML
    private void showAddProductDialog() {
        editProductController.onSaveProduct(this::onSaveProduct);
        editProductController.showDialog((StackPane) tvProduct.getScene().getRoot());
    }

    private void onSaveProduct(Product product) {
        StackPane root = (StackPane) tvProduct.getScene().getRoot();
        Product savedItem = productService.saveItem(product);
        if (savedItem != null) {
            updateItems();
            String message = productService.isItemEmpty(product) ? Constant.MSG.ADD : Constant.MSG.EDIT;
            SweetAlertFactory.successDialog(message).show(root);
        }
    }

    @FXML
    private void onPageRowChanged() {
        updateItems();
    }

    private void updateItems() {
        Page<Product> products = productService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
        tvProduct.setItems(FXCollections.observableArrayList(products.toList()));
        tvProduct.refresh();
        updatePageEntries(products);
        pagination.setPageCount(products.getTotalPages());
    }

    private void updatePageEntries(Page<Product> products) {
        long toEntry = products.getNumberOfElements() * (getCurrentPage() + 1);
        long fromEntry = (toEntry - getRowsPerPage()) + 1;

        if (products.isLast()) {
            toEntry = products.getTotalElements();
            fromEntry = toEntry - products.getNumberOfElements() + 1;
        }
        if (products.isFirst()) {
            fromEntry = 1;
            if (products.getTotalElements() == 0) fromEntry = 0;
        }
        lblPageEntries.setText("Showing " + fromEntry + " to "
                + toEntry + " of " + products.getTotalElements() + " entries");
    }

    private int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    private int getRowsPerPage() {
        return cbRowCount.getValue();
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setEditProductController(EditProductController editProductController) {
        this.editProductController = editProductController;
    }

}
