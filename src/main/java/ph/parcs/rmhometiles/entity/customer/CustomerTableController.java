package ph.parcs.rmhometiles.entity.customer;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;
import ph.parcs.rmhometiles.ui.ActionTableCell;


@Controller
public class CustomerTableController extends EntityTableController<Customer> {

    @FXML
    private void showEditItemDialog() {
        onEditActionClick(new Customer());
        editItemController.showDialog((StackPane) tvItem.getScene().getRoot());
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.baseService = customerService;
    }

    @Autowired
    public void setEditItemController(EditItemController<Customer> editItemController) {
        this.editItemController = editItemController;
    }
}
