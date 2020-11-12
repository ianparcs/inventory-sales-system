package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.Image;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.FileUtils;

import java.net.URISyntaxException;
import java.net.URL;


@Controller
public class ProductTableController extends ItemTableController<Product> {


    @FXML
    private TableColumn<Product, Supplier> tcSupplier;
    @FXML
    private TableColumn<Product, Category> tcCategory;
    @FXML
    private TableColumn<Product, Stock> tcStock;
    @FXML
    private TableColumn<Product, Stock> tcUnitSold;
    @FXML
    private TableColumn<Product, Money> tcPrice;
    @FXML
    private TableColumn<Product, Money> tcCost;
    @FXML
    private TableColumn<Product, Image> tcImage;

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
            public void updateItem(Money price, boolean empty) {
                if (empty || price == null) return;
                setText(price.toString());
            }
        });

        tcCost.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Money cost, boolean empty) {
                if (empty || cost == null) return;
                setText(cost.toString());
            }
        });

        tcImage.setCellFactory(tc -> {
            TableCell<Product, Image> cell = new TableCell<>() {

                @SneakyThrows
                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if (!empty && image != null) {
                        URL url = FileUtils.getResourcePath(image.getName());
                        if (url != null) {
                            ImageView imageView = new ImageView(new javafx.scene.image.Image(url.toURI().toString()));
                            imageView.setPreserveRatio(true);
                            imageView.setFitHeight(128);
                            imageView.setFitWidth(128);
                            setGraphic(imageView);
                            setUserData(image);
                        }
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() >= 1) {
                    try {
                        Image fileImage = (Image) cell.getUserData();
                        if (fileImage == null) return;
                        URL url = FileUtils.getResourcePath(fileImage.getName());
                        ImageView image = new ImageView(new javafx.scene.image.Image(url.toURI().toString()));
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

        tcStock.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Stock stock, boolean empty) {
                if (empty) return;
                String unit = stock.getStocks() + " ";
                if (stock.getStockUnit() != null) {
                    unit += stock.getStockUnit().getName();
                }
                setText(unit);
            }
        });

        tcUnitSold.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Stock stock, boolean empty) {
                if (empty) return;
                setText(stock.getStocks().toString());
            }
        });
    }

    @FXML
    private void showEditItemDialog() {
        onItemEditAction(new Product());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.baseTableService = productService;
    }

}
