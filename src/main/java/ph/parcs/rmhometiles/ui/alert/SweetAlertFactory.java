package ph.parcs.rmhometiles.ui.alert;


import ph.parcs.rmhometiles.util.Global;

public class SweetAlertFactory {

    public static SweetAlert create(SweetAlert.Type type, String msg) {
        switch (type) {
            case SUCCESS:
                return successDialog(msg);
            case WARNING:
                return deleteDialog(msg);
            case DANGER:
                return dangerDialog(msg);
            case INFO:
                return infoDialog(msg);

        }
        return null;
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
                .setHeaderMessage("Error")
                .setConfirmButton("Okay")
                .setContentMessage(message);
        alert.setConfirmListener(alert::close);
        return alert;
    }

    private static SweetAlert deleteDialog(String message) {
        return new SweetAlert()
                .setHeaderMessage(Global.MSG.ASK)
                .setContentMessage(message)
                .setType(SweetAlert.Type.WARNING)
                .setCancelButton("Cancel")
                .setConfirmButton("Remove");
    }

    private static SweetAlert infoDialog(String message) {
        return new SweetAlert()
                .setHeaderMessage(Global.MSG.ASK)
                .setContentMessage(message)
                .setType(SweetAlert.Type.INFO)
                .setCancelButton("Close");
    }
}
