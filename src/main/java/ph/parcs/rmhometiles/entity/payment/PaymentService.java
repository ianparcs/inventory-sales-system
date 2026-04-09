package ph.parcs.rmhometiles.entity.payment;

import org.joda.money.Money;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.invoice.Invoice;

import java.time.LocalDateTime;

@Service
public class PaymentService extends BaseService<Payment> {

    public Payment createPayment(boolean isCash, Invoice invoice) {
        var paymentType = getPaymentType(isCash);
        Payment payment = createDefault();
        payment.setPaymentType(paymentType);
        payment.setPaymentAmount(invoice.getBalance());
        return payment;
    }

    @Override
    public Payment createDefault() {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    private Payment.Method getPaymentType(boolean cashPayment) {
        if (cashPayment) return Payment.Method.CASH;
        return Payment.Method.GCASH;
    }

    public Payment.Status processPaymentStatus(Money balance) {
        if (balance.isNegativeOrZero()) return Payment.Status.PAID;
        return Payment.Status.UNPAID;
    }
}
