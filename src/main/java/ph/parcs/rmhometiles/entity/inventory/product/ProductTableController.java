package ph.parcs.rmhometiles.entity.inventory.product;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.Category;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;
import ph.parcs.rmhometiles.entity.inventory.stock.Stock;
import ph.parcs.rmhometiles.entity.supplier.Supplier;
import ph.parcs.rmhometiles.file.ImageProduct;
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
    private TableColumn<Product, ImageProduct> tcImage;

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

        tcImage.setCellFactory(tc -> {
            TableCell<Product, ImageProduct> cell = new TableCell<>() {
                @Override
                protected void updateItem(ImageProduct productImage, boolean empty) {
                    if (productImage != null) {
                        if (productImage.getPath().isEmpty()) return;
                        URL url = FileUtils.getResourcePath(productImage.getName());
                        if (url != null) {
                            new Thread(() -> {
                                Image image = null;
                                try {
                                    Thread.sleep(1000);
                                    image = new Image(url.toURI().toString());
                                } catch (URISyntaxException | InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    Image finalImage = image;
                                    Platform.runLater(() -> {
                                        productImage.setImage(finalImage);
                                        ImageView imageView = createImageView(finalImage, 64, 64);
                                        setUserData(productImage);
                                        setGraphic(imageView);
                                    });
                                }
                            }).start();
                        }
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() >= 1) {
                    ImageProduct imageProduct = (ImageProduct) cell.getUserData();
                    if (imageProduct == null || imageProduct.getImage() == null) return;
                    Image image = imageProduct.getImage();
                    ImageView imageView = createImageView(image, 600, 400);
                    sweetAlert.setHeaderMessage(imageProduct.getName());
                    sweetAlert.setBody(imageView);
                    sweetAlert.show(spMain);
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
        imageView.setPreserveRatio(true);
        return imageView;
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
