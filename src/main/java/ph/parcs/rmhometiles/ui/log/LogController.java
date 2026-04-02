package ph.parcs.rmhometiles.ui.log;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.log.Log;
import ph.parcs.rmhometiles.entity.log.LogService;
import ph.parcs.rmhometiles.ui.pagination.PaginationController;

@Controller
public class LogController extends PaginationController<Log> {

    @FXML
    private TableView<Log> tvItem;
    @FXML
    private JFXComboBox<String> cbDateRange;

    private LogService logService;

    @FXML
    protected void initialize() {
        super.initialize();
    }

    @FXML
    public void onDateRangeSelect(ActionEvent actionEvent) {
    }

    @Override
    public void updateItems() {

    }

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
