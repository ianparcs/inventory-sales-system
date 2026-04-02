package ph.parcs.rmhometiles.entity.log;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.customer.CustomerService;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;


@Controller
public class LogTableController extends EntityTableController {

    public void initialize() {
        super.initialize();
    }

    @Autowired
    public void setLogService(LogService logService) {
        this.baseService = logService;
    }
}
