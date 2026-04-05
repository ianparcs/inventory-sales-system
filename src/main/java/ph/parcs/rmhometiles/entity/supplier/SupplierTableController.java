package ph.parcs.rmhometiles.entity.supplier;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.user.User;

@Controller
public class SupplierTableController extends EntityTableController<Supplier> {

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Supplier());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setProductService(SupplierService supplierService) {
        this.baseService = supplierService;
    }

    @Autowired
    public void setEditItemController(EditItemController<Supplier> editItemController) {
        this.editItemController = editItemController;
    }

    @Override
    protected void hideUIBasedOnUserRole(User user) {

    }
}
