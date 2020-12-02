package ph.parcs.rmhometiles.entity;

import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Service
public class MoneyService {

    public Money computePercentage(Money amount, String discountPercent) {
        Number discountNumber = getDiscountPercent(discountPercent);

        Money discountAmount = Money.parse("PHP 0.00");
        if (amount != null && !amount.isNegativeOrZero()) {
            discountAmount = amount.multipliedBy(discountNumber.doubleValue(), RoundingMode.HALF_EVEN);
        }
        return discountAmount;
    }

    @SneakyThrows
    private Number getDiscountPercent(String discountPercent) {
        if (!discountPercent.matches(".*\\d.*")) discountPercent = "0.00%";
        if (!discountPercent.endsWith("%")) discountPercent += "%";
        return new DecimalFormat("0.0#%").parse(discountPercent);
    }

    public Money computeTotalAmountDue(Money amount, Money cashPaid) {
        if (amount == null || cashPaid == null) return Money.parse("PHP 0.00");
        return amount.minus(cashPaid);
    }

    public Money computeTotalAmount(Money currentTotal, Money taxAmount, Money deliveryRate) {
        Money totalAmount = currentTotal.minus(taxAmount);
        if(totalAmount.isGreaterThan(deliveryRate)){
            totalAmount.minus(deliveryRate);
        }else{
            deliveryRate.minus(totalAmount);
        }
        return currentTotal.minus(taxAmount).minus(deliveryRate);
    }
}
