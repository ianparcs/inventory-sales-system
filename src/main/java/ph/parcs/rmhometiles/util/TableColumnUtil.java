package ph.parcs.rmhometiles.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import org.joda.money.Money;

public class TableColumnUtil {

    public static <T> void configureMoneyColumn(TableColumn<T, Money> tableCell) {
        tableCell.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Money item, boolean empty) {
                if (!empty) {
                    setText(MoneyUtil.print(item));
                }
            }
        });
    }
}
