package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.FileImage;
import ph.parcs.rmhometiles.file.FileService;
import ph.parcs.rmhometiles.ui.ActionTableCell;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.FileUtils;
import ph.parcs.rmhometiles.util.Global;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
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
    private TableColumn<Product, FileImage> tcImage;

    private SweetAlert sweetAlert;

    @FXML
    public void initialize() {
        super.initialize();
        initTableColumnValue();
        sweetAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);
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

        tcImage.setCellFactory(tc -> {
            TableCell<Product, FileImage> cell = new TableCell<>() {

                @SneakyThrows
                @Override
                protected void updateItem(FileImage fileImage, boolean empty) {
                    super.updateItem(fileImage, empty);
                    if (!empty && fileImage != null) {
                        URL url = FileUtils.getResourcePath(fileImage.getName());
                        if (url != null) {
                            ImageView image = new ImageView(new Image(url.toURI().toString()));
                            image.setPreserveRatio(true);
                            image.setFitHeight(128);
                            image.setFitWidth(128);
                            setGraphic(image);
                            setUserData(fileImage);
                        }
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() >= 1) {
                    try {
                        FileImage fileImage = (FileImage) cell.getUserData();
                        URL url = FileUtils.getResourcePath(fileImage.getName());
                        ImageView image = new ImageView(new Image(url.toURI().toString()));
                        image.setFitWidth(612);
                        image.setFitHeight(450);
                        sweetAlert.setHeaderMessage(fileImage.getName());
                        sweetAlert.setBody(image);
                        sweetAlert.show(spMain);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
            return cell;
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

}
