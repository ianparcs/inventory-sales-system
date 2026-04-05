package ph.parcs.rmhometiles.exception;

public enum ExceptionType {
    PASSWORD_NOT_MATCH("Password do not match"),
    ITEM_LOCKED("Unable to delete. Item is used from order items"),

    USER_EXIST("User already exist");
    private final String typeValue;

    ExceptionType(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
