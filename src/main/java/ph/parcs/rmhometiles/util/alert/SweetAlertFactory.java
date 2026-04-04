package ph.parcs.rmhometiles.util.alert;


import javafx.scene.layout.StackPane;
import ph.parcs.rmhometiles.util.AppConstant;

public class SweetAlertFactory {

    public static SweetAlert create(SweetAlert.Type type, String msg) {
        return switch (type) {
            case SUCCESS -> successDialog(msg);
            case WARNING -> deleteDialog(msg);
            case DANGER -> dangerDialog(msg);
            case INFO -> infoDialog(msg);
        };
    }

    public static SweetAlert create(SweetAlert.Type type) {
        return create(type, null);
    }

    private static SweetAlert successDialog(String message) {
        SweetAlert alert = new SweetAlert()
                .setHeaderMessage("Success!")
                .setType(SweetAlert.Type.SUCCESS)
                .setConfirmButton("Okay")
                .setContentMessage(message);
        alert.setConfirmListener(alert::close);
        return alert;
    }

    private static SweetAlert dangerDialog(String message) {
        SweetAlert alert = new SweetAlert()
                .setType(SweetAlert.Type.DANGER)
                .setHeaderMessage("Oops!")
                .setConfirmButton("Okay")
                .setContentMessage(message);
        alert.setConfirmListener(alert::close);
        return alert;
    }

    private static SweetAlert deleteDialog(String message) {
        return new SweetAlert()
                .setHeaderMessage(AppConstant.Message.ASK)
                .setContentMessage(message)
                .setType(SweetAlert.Type.WARNING)
                .setCancelButton("Cancel")
                .setConfirmButton("Remove");
    }

    private static SweetAlert infoDialog(String message) {
        return new SweetAlert()
                .setHeaderMessage(AppConstant.Message.ASK)
                .setContentMessage(message)
                .setType(SweetAlert.Type.INFO)
                .setCancelButton("Close");
    }

    public static void show(StackPane stackPane, SweetAlert.Type type, String message) {
        create(type, message).show(stackPane);
    }
}
