package ph.parcs.rmhometiles.util;

public interface Global {

    String STRING_EMPTY = "";
    String JADIRA_PACKAGE = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount";
    String TAX = "3%";

    interface Unit {
        String PCS = "pcs";
        String PERCENT = "%";
        String CURRENCY = "PHP";
    }

    interface Regex {
        String DECIMAL_PERCENT = "^$|\\d+(\\.\\d*)[%]|\\d+[%]|\\d+(\\.\\d*)?|(\\.\\d+)|^$";
        String DECIMAL = "\\d+(\\.\\d*)?|(\\.\\d+)|^$";
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
        String ENTER_CUSTOMER = "Please select a customer";

    }

}
