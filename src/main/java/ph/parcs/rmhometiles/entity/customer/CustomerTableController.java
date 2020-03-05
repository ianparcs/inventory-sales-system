package ph.parcs.rmhometiles.entity.customer;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.ItemTableController;


@Controller
public class CustomerTableController extends ItemTableController<Customer> {

    public void initialize() {
        super.initialize();
/*        tvItem.setEditable(true);
        tcName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcName.setOnEditCommit(
                (TableColumn.CellEditEvent<Customer, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
        */
    }

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
