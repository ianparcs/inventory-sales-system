package ph.parcs.rmhometiles;

public enum State {
    LOGIN("/fxml/login/login.fxml"), HOME("/fxml/main/home.fxml"),
    PRODUCT("/fxml/product/product.fxml"), SALE_REPORT("/fxml/report/report.fxml"),
    CATEGORY("/fxml/category/category.fxml"), DASHBOARD("/fxml/dashboard/dashboard.fxml"),
    SUPPLIER("/fxml/supplier/supplier.fxml"), INVOICE("/fxml/invoice/invoice.fxml"),
    LOG("/fxml/log/log.fxml"), ERROR("/fxml/main/error.fxml");

    public final String path;

    State(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
