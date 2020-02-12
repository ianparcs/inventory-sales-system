package ph.parcs.rmhometiles.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.category.Category;
import ph.parcs.rmhometiles.item.ItemTableController;
import ph.parcs.rmhometiles.product.Product;
import ph.parcs.rmhometiles.product.ProductService;
import ph.parcs.rmhometiles.util.Global;


@Controller
public class SupplierTableController extends ItemTableController<Supplier> {

    @FXML
    public void initialize() {
        super.initialize();
    }

    @FXML
    private void showEditItemDialog() {
        onItemEditAction(new Supplier());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(SupplierService supplierService) {
        this.itemService = supplierService;
    }

}
