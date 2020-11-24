package ph.parcs.rmhometiles.util;

import com.jfoenix.controls.JFXSnackbarLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SnackbarLayoutFactory {

    public static JFXSnackbarLayout create(String message) {
        FontAwesomeIconView circleIcon = new FontAwesomeIconView();
        circleIcon.getStyleClass().add("snack-bar-icon");

        Label label = new Label(message);
        label.getStyleClass().add("snack-bar-label");

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(circleIcon);
        hBox.getChildren().add(label);

        JFXSnackbarLayout snackbarLayout = new JFXSnackbarLayout("");
        snackbarLayout.setLeft(hBox);
        return snackbarLayout;
    }
}
