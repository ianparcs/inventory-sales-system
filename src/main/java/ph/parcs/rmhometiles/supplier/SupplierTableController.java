package ph.parcs.rmhometiles.supplier;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.Supplier;
import ph.parcs.rmhometiles.item.ItemTableController;

@Controller
public class SupplierTableController extends ItemTableController<Supplier> {

    @FXML
    private void showAddItemDialog() {
        onItemEditAction(new Supplier());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(SupplierService supplierService) {
        this.itemService = supplierService;
    }

}
