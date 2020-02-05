package ph.parcs.rmhometiles.ui.alert;


public class SweetAlertFactory {

    public static SweetAlert create(SweetAlert.Type type, String msg) {
        switch (type) {
            case SUCCESS:
                return successDialog(msg);
            case WARNING:
                return deleteDialog();
        }
        return null;
    }

    public static SweetAlert create(SweetAlert.Type type) {
        return create(type, null);
    }

    private static SweetAlert successDialog(String message) {
        SweetAlert alert = new SweetAlert()
                .setType(SweetAlert.Type.SUCCESS)
                .setConfirmButton("Okay")
                .setMessage(message);
        alert.setConfirmListener(alert::close);
        return alert;
    }

    private static SweetAlert deleteDialog() {
        return new SweetAlert()
                .setType(SweetAlert.Type.WARNING)
                .setCancelButton("Cancel")
                .setConfirmButton("Remove");
    }
}
