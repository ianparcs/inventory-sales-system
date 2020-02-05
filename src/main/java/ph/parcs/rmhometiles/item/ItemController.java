package ph.parcs.rmhometiles.item;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.product.Product;

@Controller
public class ItemController {
    @FXML
    protected Label lblPageEntries;
    @FXML
    protected Pagination pagination;
    @FXML
    protected TableView<Product> tvProduct;
    @FXML
    protected ComboBox<Integer> cbRowCount;
    @FXML
    protected JFXTextField tfSearchItem;
}
