package ph.parcs.rmhometiles;

public enum State {
    LOGIN("/fxml/login/login.fxml"), HOME("/fxml/main/home.fxml"),
    INVENTORY("/fxml/inventory/inventory.fxml"), SALE_REPORT("/fxml/report/report.fxml"),
    DASHBOARD("/fxml/dashboard/dashboard.fxml"),
    SUPPLIER("/fxml/supplier/supplier.fxml"), INVOICE("/fxml/invoice/invoice.fxml"),
    LOG("/fxml/log/log.fxml"), ERROR("/fxml/main/error.fxml"),
    NAVIGATION("/fxml/main/navigation.fxml"), CUSTOMER("/fxml/customer/customer.fxml");

    public final String path;

    State(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
