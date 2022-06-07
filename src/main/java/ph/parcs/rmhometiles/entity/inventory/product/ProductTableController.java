package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import ph.parcs.rmhometiles.util.alert.SweetAlert;
import ph.parcs.rmhometiles.util.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.FileUtils;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;


@Controller
public class ProductTableController extends EntityTableController<Product> {

    @FXML
    private TableColumn<Product, String> tcDescription;
    @FXML
    private TableColumn<Product, ImageProduct> tcImage;
    @FXML
    private TableColumn<Product, Supplier> tcSupplier;
    @FXML
    private TableColumn<Product, Category> tcCategory;
    @FXML
    private TableColumn<Product, Integer> tcUnitSold;
    @FXML
    private TableColumn<Product, String> tcStock;
    @FXML
    private TableColumn<Product, String> tcCode;
    @FXML
    private TableColumn<Product, String> tcName;
    @FXML
    private TableColumn<Product, Money> tcPrice;
    @FXML
    private TableColumn<Product, Money> tcCost;

    @FXML
    public void initialize() {
        super.initialize();
        initTableColumnValue();
        initTableColumnSize();
        initTableColumnSort();
        createFakeData();
    }

    private void initTableColumnSort() {
        tcStock.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
    }

    private void initTableColumnSize() {
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
    }

    private void initTableColumnValue() {
        tcSupplier.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().supplierProperty(), "name"));
        tcCategory.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().categoryProperty(), "name"));
        tcUnitSold.setCellValueFactory(cellData -> Bindings.select(cellData.getValue().stockProperty(), "unitSold"));

        initStockColumnProperty();
        initImageColumnProperty();
    }

    private void initImageColumnProperty() {
        tcImage.setCellFactory(tc -> {
            TableCell<Product, ImageProduct> cell = new TableCell<>() {
                @SneakyThrows
                @Override
                protected void updateItem(ImageProduct productImage, boolean empty) {
                    setGraphic(null);
                    setUserData(null);
                    if (productImage != null) {
                        URL url = FileUtils.getResourcePath(productImage.getName());
                        Image picture = new Image(url.toURI().toString(), true);
                        ImageView imageView = createImageView(picture, 48, 48);
                        setUserData(productImage);
                        setGraphic(imageView);
                    }
                }
            };

            cell.setOnMouseClicked(mouseEvent -> {
                ImageProduct imageProduct = (ImageProduct) cell.getUserData();
                if (imageProduct == null) return;
                URL url = FileUtils.getResourcePath(imageProduct.getName());
                try {
                    Image picture = new Image(url.toURI().toString(), true);
                    ImageView imageView = createImageView(picture, 600, 400);
                    showAlert(imageView, imageProduct.getName());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            return cell;
        });
    }

    private void initStockColumnProperty() {
        tcStock.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return Bindings.createObjectBinding(() -> {
                Stock stock = product.getStock();
                if (stock != null) {
                    String value = stock.getStocks() + " ";
                    if (stock.getStockUnit() != null) value += stock.getStockUnit().getName();
                    return value.trim();
                }
                return "";
            });
        });
    }

    private void showAlert(ImageView imageView, String name) {
        SweetAlert sweetAlert = SweetAlertFactory.create(SweetAlert.Type.INFO);
        sweetAlert.setHeaderMessage(name);
        sweetAlert.setBody(imageView);
        sweetAlert.show(spMain);
    }

    private ImageView createImageView(Image image, int width, int height) {
        ImageView imageView = new ImageView(image);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void createFakeData() {
        Product product = new Product();
        product.setCode("test");
        product.setName("Test");
        Stock stock = new Stock();
        stock.setStocks(5);
        product.setStock(stock);
        Money money = Money.parse("PHP 23");
        Money cost = Money.parse("PHP 15.17");
        product.setPrice(money);
        product.setCost(cost);
        product.setDescription("");
        baseService.saveEntity(product);
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
