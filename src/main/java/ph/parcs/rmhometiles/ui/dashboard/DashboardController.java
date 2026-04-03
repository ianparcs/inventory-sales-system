package ph.parcs.rmhometiles.ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {

    @FXML
    private LineChart<String, Number> lcSalesOverview;

    @FXML
    public void initialize() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>("Jan", 1));
        series.getData().add(new XYChart.Data<>("Feb", 2));
        series.getData().add(new XYChart.Data<>("May", 2));
        series.getData().add(new XYChart.Data<>("June", 3));
        series.getData().add(new XYChart.Data<>("July", 5));
        series.setName("Sales Overview");

        lcSalesOverview.setTitle("Sample Line Chart");
        lcSalesOverview.getData().add(series);
    }

}
