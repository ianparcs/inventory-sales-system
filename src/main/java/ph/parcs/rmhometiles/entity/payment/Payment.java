package ph.parcs.rmhometiles.entity.payment;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.util.converter.MoneyConverter;


@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends BaseEntity {

    private final ObjectProperty<Money> paymentAmount = new SimpleObjectProperty<>();
    private final ObjectProperty<Invoice> invoice = new SimpleObjectProperty<>();
    private final ObjectProperty<Payment.Method> paymentType = new SimpleObjectProperty<>();

    @ManyToOne
    @JoinTable(name = "invoice_payment",
            joinColumns = {@JoinColumn(name = "payment_id", referencedColumnName = "payment_id")},
            inverseJoinColumns = {@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")})
    public Invoice getInvoice() {
        return invoice.get();
    }

    public void setInvoice(Invoice invoice) {
        this.invoice.set(invoice);
    }

    @Convert(converter = MoneyConverter.class)
    public Money getPaymentAmount() {
        return paymentAmount.get();
    }

    public void setPaymentAmount(Money paymentAmount) {
        this.paymentAmount.set(paymentAmount);
    }


    public enum Method {
        GCASH,
        CASH,
        UNKNOWN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    public Method getPaymentType() {
        return paymentType.get();
    }
    public void setPaymentType(Method paymentAmount) {
        this.paymentType.set(paymentAmount);
    }

    public enum Status {
        PAID,
        UNPAID
    }

}
