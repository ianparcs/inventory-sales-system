package ph.parcs.rmhometiles.entity.report;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.util.DateUtility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@Scope("singleton")
public class SalesReportController {

    @FXML
    private TableColumn<SalesReport, String> tcSalesDate;
    @FXML
    private TableView<SalesReport> tvSalesReports;
    @FXML
    private JFXComboBox<String> cbDateRange;

    private SalesReportService salesReportService;

    @FXML
    private void initialize() {
        DateUtility.initialize();
        displaySalesReport();

        tcSalesDate.setCellValueFactory(cellData -> {
            LocalDateTime createdAt = cellData.getValue().getCreatedAt();
            return Bindings.createObjectBinding(() -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                if (createdAt != null) {
                    return createdAt.format(formatter);
                }
                return "";
            });
        });
    }

    @FXML
    public void onDateRangeSelect() {
        displaySalesReport();
    }

    private void displaySalesReport() {
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
