package ph.parcs.rmhometiles.product;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.item.ItemTableController;
import ph.parcs.rmhometiles.supplier.Supplier;
import ph.parcs.rmhometiles.util.Global;


@Controller
public class ProductTableController extends ItemTableController<Product> {

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
    public void initialize() {
        super.initialize();
        initTableColumnValue();
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
                if (!empty) setText(stock + " " + Global.UNIT.PCS);
            }
        });

        tcPrice.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Float price, boolean empty) {
                if (!empty) setText(Global.UNIT.PESO + String.format("%,.2f", price));
            }
        });

        tcDiscount.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Integer discount, boolean empty) {
                if (!empty) setText(discount + Global.UNIT.PERCENT);
            }
        });
    }

    @FXML
    private void showAddItemDialog() {
        onItemEditAction(new Product());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.itemService = productService;
    }

}
