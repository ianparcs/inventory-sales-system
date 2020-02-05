package ph.parcs.rmhometiles.ui;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionTableCell<S> extends TableCell<S, HBox> {

    private HBox hBox;

    public ActionTableCell(Function<S, S> delFunction, Function<S, S> editFunction) {
        Text deleteIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TRASH, "1.5em");
        Text editIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PENCIL, "1.5em");

        deleteIcon.setId("delete-icon");
        editIcon.setId("edit-icon");

        JFXButton btnDelete = new JFXButton();
        JFXButton btnEdit = new JFXButton();

        btnDelete.setTooltip(new Tooltip("Delete"));
        btnEdit.setTooltip(new Tooltip("Edit"));

        btnDelete.setGraphic(deleteIcon);
        btnEdit.setGraphic(editIcon);

        btnDelete.setOnAction((ActionEvent e) -> delFunction.apply(getCurrentItem()));
        btnEdit.setOnAction((ActionEvent e) -> editFunction.apply(getCurrentItem()));

        hBox = new HBox();
        hBox.getChildren().add(btnEdit);
        hBox.getChildren().add(btnDelete);
        hBox.setAlignment(Pos.CENTER);
    }

    public static <S> Callback<TableColumn<S, HBox>, TableCell<S, HBox>> forColumn(Function<S, S> delFunction, Function<S, S> editFunction) {
        return param -> new ActionTableCell<>(delFunction, editFunction);
    }

    public S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(HBox item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(hBox);
        }
    }
}