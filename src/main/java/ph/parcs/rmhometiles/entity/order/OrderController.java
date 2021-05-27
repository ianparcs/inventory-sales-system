package ph.parcs.rmhometiles.entity.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.entity.invoice.lineitems.InvoiceLineItem;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.util.List;

@Controller
public class OrderController {

    @FXML
    private TableColumn<Orders, Integer> tcSubTotal;
    @FXML
    private TableColumn<Orders, String> tcPrice;
    @FXML
    private TableColumn<Orders, String> tcStock;
    @FXML
    private TableColumn<Orders, String> tcCode;
    @FXML
    private TableColumn<Orders, String> tcQty;
    @FXML
    private JFXComboBox<Product> cbProducts;
    @FXML
    private TableView<OrderItem> tvOrderItems;
    @FXML
    private JFXDatePicker dpDate;
    @FXML
    private StackPane spMain;

    private CustomerController customerController;
    private ProductService productService;

    @FXML
    public void initialize() {
        customerController.setSpMain(spMain);
        fillProductComboboxValues();
    }

    public void onQuantityEditCommit(TableColumn.CellEditEvent cellEditEvent) {

    }

    private void fillProductComboboxValues() {
        cbProducts.setConverter(new ProductConverter(cbProducts.getValue()));
        cbProducts.getEditor().textProperty().addListener((observable, oldVal, keyTyped) -> showProduct(keyTyped));
        cbProducts.focusedProperty().addListener((observableValue, outOfFocus, focus) -> {
            if (focus) showProduct(Global.STRING_EMPTY);
        });
    }

    private void showProduct(String query) {
        List<Product> products = productService.findEntities(query);
        cbProducts.show();
        Platform.runLater(() -> cbProducts.getItems().setAll(FXCollections.observableArrayList(products)));
    }

    @FXML
    public void onProductItemSearch() {
       /* Product product = cbProducts.getValue();
        if (product == null) return;
        tvOrderItems.getItems().add(new OrderItem(product));
        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
            tvInvoice.refresh();
        });*/

    }

    @Autowired
    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
