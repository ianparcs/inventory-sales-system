package ph.parcs.rmhometiles.entity.report;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("singleton")
public class SalesReportController {


    public TableView tvSalesReports;

    @FXML
    public void onDateRangeSelect() {

    }
}
