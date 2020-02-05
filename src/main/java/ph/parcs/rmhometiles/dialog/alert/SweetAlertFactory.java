package ph.parcs.rmhometiles.dialog.alert;


public class SweetAlertFactory {

    public static SweetAlert successDialog(String message) {
        SweetAlert alert = new SweetAlert()
                .setType(SweetAlert.Type.SUCCESS)
                .setConfirmButton("Okay")
                .setMessage(message);
        alert.setConfirmListener(alert::close);
        return alert;
    }

    public static SweetAlert deleteDialog() {
        return new SweetAlert()
                .setType(SweetAlert.Type.WARNING)
                .setCancelButton("Cancel")
                .setConfirmButton("Remove");
    }
}
