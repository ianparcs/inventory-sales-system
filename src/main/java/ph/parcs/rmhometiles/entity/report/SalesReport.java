package ph.parcs.rmhometiles.entity.report;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.customer.Customer;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

public class SalesReport extends BaseEntity {

    private final ObjectProperty<Money> totalAmount = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> subtotal = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> total = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> cost = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> tax = new SimpleObjectProperty<>();

    public Money getSubtotal() {
        return subtotal.get();
    }

    public ObjectProperty<Money> subtotalProperty() {
        return subtotal;
    }

    public void setSubtotal(Money subtotal) {
        this.subtotal.set(subtotal);
    }

    public Money getTotalAmount() {
        return totalAmount.get();
    }

    public ObjectProperty<Money> totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public Money getTotal() {
        return total.get();
    }

    public ObjectProperty<Money> totalProperty() {
        return total;
    }

    public void setTotal(Money total) {
        this.total.set(total);
    }

    public Money getCost() {
        return cost.get();
    }

    public ObjectProperty<Money> costProperty() {
        return cost;
    }

    public void setCost(Money cost) {
        this.cost.set(cost);
    }

    public Money getTax() {
        return tax.get();
    }

    public ObjectProperty<Money> taxProperty() {
        return tax;
    }

    public void setTax(Money tax) {
        this.tax.set(tax);
    }
}
