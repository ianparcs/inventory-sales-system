package ph.parcs.rmhometiles.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PASSWORD_NOT_MATCH("Password do not match"),
    PASSWORD_INCORRECT("The password is incorrect"),
    ITEM_LOCKED("Unable to delete. Item is used from order items"),
    USER_NOT_EXIST("User doesn't exist"),
    USER_EXIST("User already exist"),
    AMOUNT_IS_REQUIRED("Amount is required"),
    CUSTOMER_IS_REQUIRED("Customer is required"),
    ORDER_IS_REQUIRED("At least one order is required"),
    ORDER_QUANTITY_IS_REQUIRED("Order quantity is required"),
    ORDER_DUPLICATE("Order cannot be duplicate"),

    INVOICE_QUANTITY_EXCEEDED("Quantity must not exceed stocks");

    private final String typeValue;

    ErrorCode(String typeValue) {
        this.typeValue = typeValue;
    }

}
