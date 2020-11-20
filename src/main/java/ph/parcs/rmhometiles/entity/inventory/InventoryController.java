package ph.parcs.rmhometiles.entity.inventory;

import com.jfoenix.controls.JFXTabPane;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.CategoryTableController;
import ph.parcs.rmhometiles.entity.inventory.product.ProductTableController;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnitTableController;

@Controller
public class InventoryController {

    @FXML
    private Tab tabStockUnit;
    @FXML
    private Tab tabCategory;
    @FXML
    private Tab tabProduct;
    @FXML
    private JFXTabPane tpRoot;

    private ProductTableController productTableController;
    private CategoryTableController categoryTableController;
    private StockUnitTableController stockUnitTableController;

    @FXML
    private void initialize() {
        tpRoot.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab.equals(tabStockUnit)) stockUnitTableController.updateItems();
            else if (newTab.equals(tabCategory)) categoryTableController.updateItems();
            else if (newTab.equals(tabProduct)) productTableController.updateItems();
        });
        tpRoot.setDisableAnimation(true);
    }

    @Autowired
    public void setProductTableController(ProductTableController productTableController) {
        this.productTableController = productTableController;
    }

    @Autowired
    public void setCategoryTableController(CategoryTableController categoryTableController) {
        this.categoryTableController = categoryTableController;
    }

    @Autowired
    public void setStockUnitTableController(StockUnitTableController stockUnitTableController) {
        this.stockUnitTableController = stockUnitTableController;
    }

}
