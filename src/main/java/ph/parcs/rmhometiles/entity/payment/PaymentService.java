package ph.parcs.rmhometiles.entity.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;
import ph.parcs.rmhometiles.entity.invoice.Invoice;

import java.time.LocalDateTime;

@Service
public class PaymentService extends BaseService<Payment> {

    public Payment createPayment(boolean isCash, Invoice invoice) {
        var paymentType = getPaymentType(isCash);

        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentAmount(invoice.getBalance());
        return payment;
    }

    private Payment.Method getPaymentType(boolean cashPayment) {
        if (cashPayment) return Payment.Method.CASH;
        return Payment.Method.GCASH;
    }

    @Override
    public Payment createDefault() {
        return null;
    }

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

}
