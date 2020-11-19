package ph.parcs.rmhometiles.entity.invoice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

@Controller
public class InvoiceProductController {

    @FXML
    protected JFXButton btnAddItem;
    @FXML
    protected JFXDialog editDialog;

    public void show(StackPane stackPane) {
        editDialog.show(stackPane);
    }

    public void closeDialog() {
        editDialog.close();
    }
}
