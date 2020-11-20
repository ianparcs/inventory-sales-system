package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;

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
}
