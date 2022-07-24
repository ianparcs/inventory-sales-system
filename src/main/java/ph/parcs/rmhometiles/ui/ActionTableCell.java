package ph.parcs.rmhometiles.ui;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionTableCell<S> extends TableCell<S, HBox> {

    private final HBox hBox = new HBox();


    public ActionTableCell(Function<S, S> editFunction, String type) {
        JFXButton button = new JFXButton();
        Text icon;
        if (type.equalsIgnoreCase("EDIT")) {
            icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PENCIL, "1.5em");
            icon.setId("edit-icon");
            button.setTooltip(new Tooltip("Edit"));
        } else {
            icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TRASH, "1.5em");
            icon.setId("delete-icon");
            button.setTooltip(new Tooltip("Delete"));
        }
        button.setGraphic(icon);
        button.setPadding(new Insets(5));
        button.setOnAction((ActionEvent e) -> editFunction.apply(getCurrentItem()));

        hBox.getChildren().add(button);
        hBox.setAlignment(Pos.CENTER);
    }

    public ActionTableCell(Function<S, S> editFunction, Function<S, S> delFunction) {
        this(editFunction, "EDIT");

        Text deleteIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TRASH, "1.5em");
        deleteIcon.setId("delete-icon");
        JFXButton btnDelete = new JFXButton();
        btnDelete.setPadding(new Insets(5));
        btnDelete.setTooltip(new Tooltip("Delete"));
        btnDelete.setGraphic(deleteIcon);
        btnDelete.setOnAction((ActionEvent e) -> delFunction.apply(getCurrentItem()));

        hBox.getChildren().add(btnDelete);
        hBox.setAlignment(Pos.CENTER);
    }

    public ActionTableCell(Function<S, S> viewFunction, Function<S, S> editFunction, Function<S, S> delFunction) {
        this(editFunction, delFunction);

        Text viewIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.EYE, "1.5em");
        viewIcon.setId("view-icon");

        JFXButton btnView = new JFXButton();
        btnView.setTooltip(new Tooltip("View"));
        btnView.setGraphic(viewIcon);
        btnView.setPadding(new Insets(5));
        btnView.setOnAction((ActionEvent e) -> viewFunction.apply(getCurrentItem()));
        hBox.getChildren().add(btnView);
        hBox.getChildren().get(1).toFront();
    }

    public static <S> Callback<TableColumn<S, HBox>, TableCell<S, HBox>> forActions(Function<S, S> viewFunction, Function<S, S> editFunction, Function<S, S> delFunction) {
        return param -> new ActionTableCell<>(viewFunction, editFunction, delFunction);
    }

    public static <S> Callback<TableColumn<S, HBox>, TableCell<S, HBox>> forActions(Function<S, S> editFunction, Function<S, S> delFunction) {
        return param -> new ActionTableCell<>(editFunction, delFunction);
    }

    public static <S> Callback<TableColumn<S, HBox>, TableCell<S, HBox>> forActions(Function<S, S> delFunction, String type) {
        return param -> new ActionTableCell<>(delFunction, type);
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