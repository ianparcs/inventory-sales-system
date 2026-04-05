package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.entity.user.User;

@Controller
public class StockUnitTableController extends EntityTableController<StockUnit> {

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new StockUnit());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setStockUnitService(StockUnitService stockUnitService) {
        this.baseService = stockUnitService;
    }

    @Autowired
    public void setEditItemController(EditItemController<StockUnit> editItemController) {
        this.editItemController = editItemController;
    }

    @Override
    protected void hideUIBasedOnUserRole(User user) {

    }
}
