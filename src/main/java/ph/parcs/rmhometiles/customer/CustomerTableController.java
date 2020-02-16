package ph.parcs.rmhometiles.customer;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.itemService = customerService;
    }
}
