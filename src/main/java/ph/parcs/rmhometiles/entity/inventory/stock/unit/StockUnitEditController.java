package ph.parcs.rmhometiles.entity.inventory.stock.unit;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;

@Controller
public class StockUnitEditController extends EditItemController<StockUnit> {

    @FXML
    private JFXTextField tfName;

    @Override
    protected StockUnit createEntity(StockUnit stockUnit) {
        stockUnit.setName(tfName.getText());
        return stockUnit;
    }

    @Override
    protected void bindFields(StockUnit stockUnit) {
        if (!StringUtils.isEmpty(stockUnit.getName()))
            tfName.setText(stockUnit.getName());
    }

    @Override
    protected void setDialogTitle(StockUnit item) {
        String title = item.getId() > 0 ? "Edit" : "Add";
        lblTitle.setText(title + " " + "Stock Unit");
    }

    @Override
    protected void clearFields() {
        tfName.clear();
    }

    @Autowired
    public void setStockUnitService(StockUnitService stockUnitService) {
        this.baseService = stockUnitService;
    }

}
