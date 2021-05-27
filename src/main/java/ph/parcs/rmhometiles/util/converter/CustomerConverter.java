package ph.parcs.rmhometiles.util.converter;

import javafx.util.StringConverter;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.util.Global;

public class CustomerConverter extends StringConverter<Customer> {

    private Customer customer;

    public CustomerConverter(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString(Customer customer) {
        if (customer == null) return Global.STRING_EMPTY;
        return customer.getName();
    }

    @Override
    public Customer fromString(String s) {
        return customer;
    }
}
