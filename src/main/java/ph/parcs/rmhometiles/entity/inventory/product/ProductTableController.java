package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.util.Global;

import java.net.URL;


@Controller
public class ProductTableController extends ItemTableController<Product> {

    @FXML
    private TableColumn<Product, Supplier> tcSupplier;
    @FXML
    private TableColumn<Product, Category> tcCategory;
    @FXML
    private TableColumn<Product, Integer> tcDiscount;
    @FXML
    private TableColumn<Product, String> tcStock;
    @FXML
    private TableColumn<Product, Float> tcPrice;
    @FXML
    private TableColumn<Product, String> tcImage;

    private FileService fileService;

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

        tcImage.setCellFactory(param -> new TableCell<>() {
            @Override
            @SneakyThrows
            protected void updateItem(String fileName, boolean empty) {
                if (!empty && fileName != null) {
                    URL url = fileService.getResourcePath(fileName);
                    if (url != null) {
                        ImageView image = new ImageView(new Image(url.toURI().toString()));
                        image.setFitHeight(64);
                        image.setFitWidth(64);
                        setGraphic(image);
                    }
                }
            }
        });

        tcStock.setCellValueFactory(cellData -> {
            Product data = cellData.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (data.getStockUnit() != null && data.getStockUnit().getName() != null) {
                            return data.getStock().toString() + " " + data.getStockUnit().getName();
                        }
                        return data.getStock().toString();
                    }, data.stockProperty()
            );
        });
    }

    @FXML
    private void showEditItemDialog() {
        onItemEditAction(new Product());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
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
