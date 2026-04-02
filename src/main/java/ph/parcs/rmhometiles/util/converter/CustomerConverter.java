package ph.parcs.rmhometiles.util.converter;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.util.AppConstant;

public class CustomerConverter extends StringConverter<Customer> {

    private final Customer customer;

    public CustomerConverter(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString(Customer customer) {
        if (customer == null) return AppConstant.STRING_EMPTY;
        return customer.getName();
    }

    @Override
    public Customer fromString(String s) {
        return customer;
    }
}
