package ph.parcs.rmhometiles.entity.inventory;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.category.CategoryTableController;
import ph.parcs.rmhometiles.entity.inventory.product.ProductTableController;
import ph.parcs.rmhometiles.entity.inventory.stock.unit.StockUnitTableController;

@Controller
public class InventoryTabController {

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
        tpRoot.setDisableAnimation(true);
        tpRoot.visibleProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab) {
                if (tpRoot.getSelectionModel().getSelectedItem().equals(tabStockUnit))
                    stockUnitTableController.updateItems();
                else if (tpRoot.getSelectionModel().getSelectedItem().equals(tabCategory))
                    categoryTableController.updateItems();
                else if (tpRoot.getSelectionModel().getSelectedItem().equals(tabProduct))
                    productTableController.updateItems();
            }
        });
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
