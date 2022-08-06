package ph.parcs.rmhometiles.entity.report;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.util.DateUtility;

import java.util.List;

@Controller
@Scope("singleton")
public class SalesReportController {

    @FXML
    private TableView<SalesReport> tvSalesReports;
    @FXML
    private JFXComboBox<String> cbDateRange;

    private SalesReportService salesReportService;

    @FXML
    private void initialize(){
        DateUtility.initialize();
    }

    @FXML
    public void onDateRangeSelect() {
        new Thread(() -> {
            List<SalesReport> salesReportsToday = salesReportService.findReports(cbDateRange.getValue());
            Platform.runLater(() -> {
                if (salesReportsToday != null) {
                    tvSalesReports.getItems().setAll(salesReportsToday);
                    tvSalesReports.refresh();
                }
            });
        }).start();
    }

    @Autowired
    public void setSalesReportService(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }
}
