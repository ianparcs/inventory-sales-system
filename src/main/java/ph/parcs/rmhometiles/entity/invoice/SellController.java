package ph.parcs.rmhometiles.entity.invoice;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class SellController {

    @FXML
    private StackPane spMain;
    @FXML
    private TableView tvItem;
    @FXML
    private TableColumn tcAction;

}
