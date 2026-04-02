package ph.parcs.rmhometiles.entity.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.EditItemController;
import ph.parcs.rmhometiles.entity.inventory.item.EntityTableController;


@Controller
public class LogTableController extends EntityTableController<Log> {

    public void initialize() {
        super.initialize();
    }

    @Autowired
    public void setLogService(LogService logService) {
        this.baseService = logService;
    }

    @Autowired
    public void setEditItemController(EditItemController<Log> editItemController) {
        this.editItemController = editItemController;
    }
}
