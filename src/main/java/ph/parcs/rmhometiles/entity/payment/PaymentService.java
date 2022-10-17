package ph.parcs.rmhometiles.entity.payment;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ph.parcs.rmhometiles.entity.MoneyService;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private MoneyService moneyService;

    public Payment createPayment(String amount, String paymentType) {
        Money paymentAmount = moneyService.parseMoney(amount);
        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setPaymentAmount(paymentAmount);
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    @Autowired
    public void setMoneyService(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
