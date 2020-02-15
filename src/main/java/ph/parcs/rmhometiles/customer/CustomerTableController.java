package ph.parcs.rmhometiles.customer;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.Customer;
import ph.parcs.rmhometiles.item.ItemTableController;

@Controller
public class CustomerTableController extends ItemTableController<Customer> {

    @FXML
    private void showAddItemDialog() {
        onItemEditAction(new Customer());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }
}
