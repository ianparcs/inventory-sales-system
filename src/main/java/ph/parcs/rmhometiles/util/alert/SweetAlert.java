package ph.parcs.rmhometiles.util.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import ph.parcs.rmhometiles.util.Global;

public class SweetAlert {

    private static final String DEFAULT_CSS = SweetAlert.class.getResource("/css/sweetalert/sweetalert.css").toExternalForm();

    private JFXDialogLayout content;
    private JFXButton btnRemove;
    private JFXDialog dialog;
    private Label lblMessage;
    private Label lblHeader;

    public SweetAlert() {
        FontAwesomeIconView circleIcon = new FontAwesomeIconView();
        FontAwesomeIconView symbolIcon = new FontAwesomeIconView();
        circleIcon.getStyleClass().add(Global.Css.CIRCLE);
        symbolIcon.getStyleClass().add(Global.Css.SYMBOL);
        circleIcon.setSize("6em");
        symbolIcon.setSize("3em");

        GlyphsStack icons = new GlyphsStack();
        icons.add(symbolIcon);
        icons.add(circleIcon);

        lblMessage = new Label();
        lblHeader = new Label();

        HBox headerContainer = new HBox(15);
        headerContainer.getChildren().add(icons);
        headerContainer.getChildren().add(lblHeader);
        headerContainer.setAlignment(Pos.CENTER);

        content = new JFXDialogLayout();
        content.setHeading(headerContainer);
        content.setBody(lblMessage);

        dialog = new JFXDialog();
        dialog.setContent(content);
    }

    public SweetAlert show(StackPane root) {
        dialog.show(root);
        return this;
    }

    public SweetAlert setType(Type type) {
        dialog.getStylesheets().setAll(getClass().getResource(type.name).toExternalForm());
        if (!dialog.getStylesheets().contains(DEFAULT_CSS)) {
            dialog.getStylesheets().add(DEFAULT_CSS);
        }
        return this;
    }

    public SweetAlert setContentMessage(String value) {
        lblMessage.setText(value);
        return this;
    }

    public SweetAlert setHeaderMessage(String value) {
        lblHeader.setText(value);
        return this;
    }

    public void close() {
        dialog.close();
    }

    public SweetAlert setConfirmListener(ConfirmListener listener) {
        btnRemove.setOnAction(actionEvent -> {
            listener.confirm();
            close();
        });
        return this;
    }

    SweetAlert setConfirmButton(String text) {
        btnRemove = new JFXButton();
        btnRemove.setText(text);
        btnRemove.getStyleClass().add("button-confirm");
        content.getActions().add(btnRemove);
        return this;
    }

    SweetAlert setCancelButton(String text) {
        JFXButton btnCancel = new JFXButton();
        btnCancel.setText(text);
        btnCancel.getStyleClass().add("button-cancel");
        btnCancel.setOnAction(actionEvent -> close());
        content.getActions().add(btnCancel);
        return this;
    }

    public void setBody(Node node) {
        content.setBody(node);
    }

    public enum Type {
        WARNING("/css/sweetalert/warning.css"),
        DANGER("/css/sweetalert/danger.css"),
        SUCCESS("/css/sweetalert/success.css"),
        INFO("/css/sweetalert/info.css");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface ConfirmListener {
        void confirm();
    }
}
