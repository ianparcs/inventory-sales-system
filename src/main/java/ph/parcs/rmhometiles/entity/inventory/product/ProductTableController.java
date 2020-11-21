package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.ImageProduct;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.FileUtils;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;


@Controller
public class ProductTableController extends EntityTableController<Product> {


    @FXML
    private TableColumn<Product, String> tcDescription;
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
    private TableColumn<Product, ImageProduct> tcImage;
    @FXML
    private TableColumn<Product, String> tcCode;
    @FXML
    private TableColumn<Product, String> tcName;

    private SweetAlert sweetAlert;

    @FXML
    public void initialize() {
        super.initialize();
        initTableColumnValue();
        sweetAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);

        tcCode.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        tcName.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        tcDescription.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        tcSupplier.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        tcCategory.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        tcStock.setMaxWidth(1f * Integer.MAX_VALUE * 6);
        tcUnitSold.setMaxWidth(1f * Integer.MAX_VALUE * 6);
        tcPrice.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        tcCost.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        tcImage.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        tcAction.setMaxWidth(1f * Integer.MAX_VALUE * 5);

        tcStock.setComparator(Comparator.comparingInt(Stock::getStocks));
        tcUnitSold.setComparator(Comparator.comparingInt(Stock::getUnitSold));

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

        tcStock.setCellFactory(param -> new TableCell<>() {
            @Override
            public void updateItem(Stock stock, boolean empty) {
                if (stock == null || empty) return;
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
                if (stock == null || empty) return;
                setText(stock.getUnitSold().toString());
            }
        });

        tcImage.setCellFactory(tc -> {
            TableCell<Product, ImageProduct> cell = new TableCell<>() {
                @SneakyThrows
                @Override
                protected void updateItem(ImageProduct productImage, boolean empty) {
                    if (productImage != null) {
                        URL url = FileUtils.getResourcePath(productImage.getName());
                        Image picture = new Image(url.toURI().toString(), true);
                        ImageView imageView = createImageView(picture, 48, 48);
                        setUserData(productImage);
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                        setUserData(null);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() >= 1) {
                    ImageProduct imageProduct = (ImageProduct) cell.getUserData();
                    if (imageProduct == null) return;
                    URL url = FileUtils.getResourcePath(imageProduct.getName());
                    try {
                        Image picture = new Image(url.toURI().toString(), true);
                        ImageView imageView = createImageView(picture, 600, 400);
                        sweetAlert.setHeaderMessage(imageProduct.getName());
                        sweetAlert.setBody(imageView);
                        sweetAlert.show(spMain);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
            });
            return cell;
        });
    }

    private ImageView createImageView(Image image, int width, int height) {
        ImageView imageView = new ImageView(image);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Product());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.baseService = productService;
    }
}
