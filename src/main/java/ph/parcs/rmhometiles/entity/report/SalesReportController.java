package ph.parcs.rmhometiles.entity.report;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.MoneyService;
import ph.parcs.rmhometiles.util.DateUtility;
import ph.parcs.rmhometiles.util.Global;
import ph.parcs.rmhometiles.util.ThreadUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Scope("singleton")
public class SalesReportController {

    @FXML
    private TableColumn<SalesReport, String> tcSalesDate;
    @FXML
    private TableView<SalesReport> tvSalesReports;
    @FXML
    private JFXComboBox<String> cbDateRange;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private Label lblProfit;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblCost;
    @FXML
    private Label lblTax;

    private SalesReportService salesReportService;
    private MoneyService moneyService;

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
        displayCalendarDate();
        displaySalesReport();
    }

    private void displayCalendarDate() {
        if (cbDateRange.getValue().equalsIgnoreCase("Custom Date Range")) {
            dpStartDate.setVisible(true);
            dpEndDate.setVisible(true);
            return;
        }
        dpStartDate.setVisible(false);
        dpEndDate.setVisible(false);
    }

    private void displaySalesReport() {
        String dateSelect = cbDateRange.getValue();
        if (dateSelect == null || dateSelect.equalsIgnoreCase("Custom Date Range")) return;

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(()->{
            List<SalesReport> salesReportsToday = salesReportService.findReports(cbDateRange.getValue());
            Map<Global.Sales, String> moneyMap = moneyService.computeAllMoney(salesReportsToday);
            Platform.runLater(() -> {
                lblTax.setText(moneyMap.get(Global.Sales.TAX));
                lblCost.setText(moneyMap.get(Global.Sales.COST));
                lblTotal.setText(moneyMap.get(Global.Sales.TOTAL));
                lblProfit.setText(moneyMap.get(Global.Sales.PROFIT));

                tvSalesReports.getItems().setAll(salesReportsToday);
                tvSalesReports.refresh();
            });
        });
        ThreadUtil.shutdownAndAwaitTermination(executorService);
    }

    @Autowired
    public void setSalesReportService(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @Autowired
    public void setMoneyService(MoneyService moneyService) {
        this.moneyService = moneyService;
    }
}
