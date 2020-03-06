package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.stereotype.Controller;

@Controller
public class InvoiceTabController {
    @FXML
    private JFXTabPane tpRoot;
    @FXML
    private Tab tabManageSell;
    @FXML
    private Tab tabNewSell;

}
