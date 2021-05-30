package ph.parcs.rmhometiles.entity.order;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.joda.money.Money;
import org.springframework.stereotype.Controller;

@Controller
public class OrdersItemTableController {

    @FXML
    private TableColumn<OrderItem, Integer> tcSubTotal;
    @FXML
    private TableColumn<OrderItem, Integer> tcStock;
    @FXML
    private TableColumn<OrderItem, HBox> tcAction;
    @FXML
    private TableColumn<OrderItem, Integer> tcQty;
    @FXML
    private TableColumn<OrderItem, String> tcCode;
    @FXML
    private TableColumn<OrderItem, Money> tcPrice;
    @FXML
    private TableView<OrderItem> tvOrderItems;

    @FXML
    public void onQuantityEditCommit(TableColumn.CellEditEvent cellEditEvent) {


    }


}
