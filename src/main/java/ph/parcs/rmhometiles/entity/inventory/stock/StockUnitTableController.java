package ph.parcs.rmhometiles.entity.inventory.stock;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;

@Controller
public class StockUnitTableController extends ItemTableController<StockUnit> {

    @FXML
    private void showAddItemDialog() {
        onItemEditAction(new StockUnit());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setCategoryService(StockUnitService stockUnitService) {
        this.itemService = stockUnitService;
    }
}
