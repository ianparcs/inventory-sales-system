package ph.parcs.rmhometiles.exception;

public enum ExceptionType {
    PASSWORD_NOT_MATCH("Password do not match"),
    PASSWORD_INCORRECT("The password is incorrect"),

    ITEM_LOCKED("Unable to delete. Item is used from order items"),
    USER_NOT_EXIST("User doesn't exist"),
    USER_EXIST("User already exist"),
    INVOICE_QUANTITY_EXCEEDED("Quantity must not exceed stocks");
    private final String typeValue;

    ExceptionType(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
