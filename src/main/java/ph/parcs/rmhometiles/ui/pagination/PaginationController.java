package ph.parcs.rmhometiles.ui.pagination;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.inventory.item.ItemPageEntry;
import ph.parcs.rmhometiles.util.PageUtil;


@Scope("prototype")
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
    @FXML
    protected StackPane spMain;

    protected BaseService<T> baseService;
    protected String searchValue = "";

    @FXML
    protected void initialize() {
        pagination.currentPageIndexProperty().addListener((observable) -> updateItems());

        spMain.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateItems();
            }
        });
        updateItems();
    }

    @FXML
    private void onPageRowChanged() {
        updateItems();
    }

    public void updatePageEntries(Page<BaseEntity> items) {
        ItemPageEntry itemPageEntry = PageUtil.getPageEntries(items);
        lblPageEntries.setText("Showing " + itemPageEntry.fromEntry() + " to " + itemPageEntry.toEntry() + " of " + items.getTotalElements() + " entries");
        pagination.setPageCount(items.getTotalPages());
    }

    public void updateItems() {
        new Thread(() -> {
            Page<T> items = baseService.findPages(getCurrentPage(), getRowsPerPage(), searchValue);
            Platform.runLater(() -> {
                tvItem.getItems().setAll(items.toList());
                tvItem.refresh();
                updatePageEntries((Page<BaseEntity>) items);
            });
        }).start();
    }

    private int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    private int getRowsPerPage() {
        if (cbRowCount.getValue().equalsIgnoreCase("all")) return 10000;
        return Integer.parseInt(cbRowCount.getValue());
    }
}
