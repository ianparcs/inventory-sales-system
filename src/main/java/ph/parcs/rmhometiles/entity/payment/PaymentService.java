package ph.parcs.rmhometiles.entity.payment;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.money.MoneyService;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;

    public Payment createPayment(Money cashPay, String paymentType) {
        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setPaymentAmount(cashPay);
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
