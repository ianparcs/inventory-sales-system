package ph.parcs.rmhometiles.entity.report;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.money.MoneyService;
import ph.parcs.rmhometiles.ui.pagination.PaginationController;
import ph.parcs.rmhometiles.util.AppConstant;
import ph.parcs.rmhometiles.util.date.DateRangeType;
import ph.parcs.rmhometiles.util.date.DateUtil;
import ph.parcs.rmhometiles.util.ThreadUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class SalesReportController extends PaginationController<SalesReport> {

    @FXML
    private TableColumn<SalesReport, String> tcSalesDate;
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
    protected void initialize() {
        super.initialize();
        displaySalesReport();
        tcSalesDate.setCellValueFactory(cellData -> {
            LocalDateTime createdAt = cellData.getValue().getCreatedAt();
            return Bindings.createObjectBinding(() -> {
                if (createdAt != null) return createdAt.format(DateUtil.FORMAT);
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

    @Override
    public void updateItems() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {

            DateRangeType dateRangeType = DateRangeType.fromValue(cbDateRange.getValue());
            List<SalesReport> salesReports = salesReportService.createSalesReports(dateRangeType);
            Map<AppConstant.Sales, String> moneyMap = moneyService.computeAllMoney(salesReports);

            Platform.runLater(() -> {
                lblTax.setText(moneyMap.get(AppConstant.Sales.TAX));
                lblCost.setText(moneyMap.get(AppConstant.Sales.COST));
                lblTotal.setText(moneyMap.get(AppConstant.Sales.TOTAL));
                lblProfit.setText(moneyMap.get(AppConstant.Sales.PROFIT));

                tvItem.getItems().setAll(salesReports);
                tvItem.refresh();
            });
        });
        ThreadUtil.shutdownAndAwaitTermination(executorService);
    }

    private void displaySalesReport() {
        String dateSelect = cbDateRange.getValue();
        if (dateSelect == null || dateSelect.equalsIgnoreCase("Custom Date Range")) return;
        updateItems();
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
