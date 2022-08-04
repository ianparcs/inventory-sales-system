package ph.parcs.rmhometiles.entity.payment;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import ph.parcs.rmhometiles.entity.inventory.item.BaseEntity;
import ph.parcs.rmhometiles.entity.invoice.Invoice;
import ph.parcs.rmhometiles.util.Global;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends BaseEntity {

    private ObjectProperty<Money> paymentAmount = new SimpleObjectProperty<>();
    private ObjectProperty<Invoice> invoice = new SimpleObjectProperty<>();
    private SimpleStringProperty paymentType = new SimpleStringProperty();

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

    @Type(type = Global.JADIRA_PACKAGE, parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "PHP")})
    public Money getPaymentAmount() {
        return paymentAmount.get();
    }

    public void setPaymentAmount(Money paymentAmount) {
        this.paymentAmount.set(paymentAmount);
    }

    @Column(name = "payment_type")
    public String getPaymentType() {
        return paymentType.get();
    }

    public void setPaymentType(String paymentType) {
        this.paymentType.set(paymentType);
    }

    public enum Method {
        GCASH,
        CASH,
        UNKNOWN;
    }

    public enum Status {
        PAID,
        UNPAID
    }

}
