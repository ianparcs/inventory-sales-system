package ph.parcs.rmhometiles.util;

public interface Global {

    String STRING_EMPTY = "";
    String JADIRA_PACKAGE = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount";

    interface Unit {
        String PCS = "pcs";
        String PERCENT = "%";
        String PESO = "₱";
    }

    interface Regex {
        String DECIMAL_PERCENT = "\\d+(\\.\\d*)[%]|\\d+[%]|\\d+(\\.\\d*)?|(\\.\\d+)|^$";

    }

    interface Css {
        String CIRCLE = "circle-icon";
        String SYMBOL = "symbol-icon";
    }

    interface Message {
        String ADD = "Item successfully added!";
        String SAVED = "Item has been successfully saved";
        String DELETE = "Item successfully removed";
        String ASK = "Are you sure?";
        String QUANTITY_EXCEED = "Quantity must not exceed stocks";
    }

}
