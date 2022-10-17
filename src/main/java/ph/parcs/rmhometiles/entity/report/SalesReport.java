package ph.parcs.rmhometiles.entity.report;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;

import java.time.LocalDateTime;

public class SalesReport extends BaseEntity {

    private final ObjectProperty<Money> totalAmount = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> subtotal = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> total = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> cost = new SimpleObjectProperty<>();
    private final ObjectProperty<Money> tax = new SimpleObjectProperty<>();

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public Money getSubtotal() {
        return subtotal.get();
    }

    public void setSubtotal(Money subtotal) {
        this.subtotal.set(subtotal);
    }

    public ObjectProperty<Money> subtotalProperty() {
        return subtotal;
    }

    public Money getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public ObjectProperty<Money> totalAmountProperty() {
        return totalAmount;
    }

    public Money getTotal() {
        return total.get();
    }

    public void setTotal(Money total) {
        this.total.set(total);
    }

    public ObjectProperty<Money> totalProperty() {
        return total;
    }

    public Money getCost() {
        return cost.get();
    }

    public void setCost(Money cost) {
        this.cost.set(cost);
    }

    public ObjectProperty<Money> costProperty() {
        return cost;
    }

    public Money getTax() {
        return tax.get();
    }

    public void setTax(Money tax) {
        this.tax.set(tax);
    }

    public ObjectProperty<Money> taxProperty() {
        return tax;
    }
}
