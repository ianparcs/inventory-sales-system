package ph.parcs.rmhometiles.entity.order;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerController;
import ph.parcs.rmhometiles.entity.inventory.product.Product;
import ph.parcs.rmhometiles.entity.inventory.product.ProductService;
import ph.parcs.rmhometiles.ui.alert.SweetAlert;
import ph.parcs.rmhometiles.ui.alert.SweetAlertFactory;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.converter.ProductConverter;

import java.util.List;

@Controller
public class OrdersController {

    @FXML
    private CustomerController customerController;
    @FXML
    private JFXComboBox<Product> cbProducts;
    @FXML
    private StackPane spMain;

    private ProductService productService;
    private OrdersService ordersService;

    @FXML
    public void initialize() {
        customerController.setSpMain(spMain);
        fillProductComboboxValues();
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
    public void createOrder() {
        Customer customer = customerController.getCustomer();
        if (customer == null) showErrorDialog();
        Orders orders = ordersService.createOrder(customer);
        ordersService.saveEntity(orders);
    }

    @FXML
    public void onProductItemSearch() {
        Product product = cbProducts.getValue();
        if (product == null) return;
        Platform.runLater(() -> {
            cbProducts.valueProperty().set(null);
            cbProducts.hide();
            spMain.requestFocus();
  /*          tvOrderItems.getItems().add(product);
            tvOrderItems.refresh();*/
        });
    }

    private void showErrorDialog() {
        SweetAlert alert = SweetAlertFactory.create(SweetAlert.Type.DANGER);
        alert.setHeaderMessage("No customer selected");
        alert.setContentMessage("Please select customer");
        alert.show(spMain);
    }

    @Autowired
    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
}
