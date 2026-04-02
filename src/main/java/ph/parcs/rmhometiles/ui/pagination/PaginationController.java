package ph.parcs.rmhometiles.ui.pagination;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.item.ItemPageEntry;
import ph.parcs.rmhometiles.util.PageUtil;
import ph.parcs.rmhometiles.util.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Controller
public abstract class PaginationController<T extends BaseEntity> {

    @FXML
    protected ComboBox<String> cbRowCount;
    @FXML
    protected Pagination pagination;
    @FXML
    protected Label lblPageEntries;
    @FXML
    protected TableView<T> tvItem;

    protected BaseService<T> baseService;
    protected String searchValue = "";

    @FXML
    protected void initialize() {
        pagination.currentPageIndexProperty().addListener((observable) -> updateItems());
    }

    public void updatePageEntries(Page<BaseEntity> items) {
        ItemPageEntry itemPageEntry = PageUtil.getPageEntries(items);
        lblPageEntries.setText("Showing " + itemPageEntry.getFromEntry() + " to " + itemPageEntry.getToEntry() + " of " + items.getTotalElements() + " entries");
        pagination.setPageCount(items.getTotalPages());
    }

    public void updateItems() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            Page<T> items = baseService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
            Platform.runLater(() -> {
                tvItem.setItems(FXCollections.observableArrayList(items.toList()));
                tvItem.refresh();
                updatePageEntries((Page<BaseEntity>) items);
            });
        });
        ThreadUtil.shutdownAndAwaitTermination(executorService);
    }

    private int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    private int getRowsPerPage() {
        if (cbRowCount.getValue().equalsIgnoreCase("all")) return 10000;
        return Integer.parseInt(cbRowCount.getValue());
    }
}
