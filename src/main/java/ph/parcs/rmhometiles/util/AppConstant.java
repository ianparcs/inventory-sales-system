package ph.parcs.rmhometiles.util;

public interface AppConstant {

    String STRING_EMPTY = "";
    double TAX = 3d / 100d;

    enum Sales {
        TAX, PROFIT, TOTAL, COST
    }

    enum ActionType {
        EDIT, VIEW, DELETE
    }

    enum Role {
        ADMIN, USER
    }

    interface Unit {
        String PCS = "pcs";
        String PERCENT = "%";
        String CURRENCY = "PHP";
    }

    interface Regex {
        String DECIMAL = "\\d+(\\.\\d{1,2})?";
    }

    interface Css {
        String CIRCLE = "circle-icon";
        String SYMBOL = "symbol-icon";
    }

    interface Message {
        String USER_REGISTERED = "You have successfully registered. Welcome aboard!";
        String SAVED = "Item has been successfully saved";
        String DELETE = "Item successfully removed";
        String ASK = "Are you sure?";
        String QUANTITY_EXCEED = "Quantity must not exceed stocks";
        String ENTER_CUSTOMER = "Please select a customer";
        String EXPORT = "File exported successfully";
    }

}
