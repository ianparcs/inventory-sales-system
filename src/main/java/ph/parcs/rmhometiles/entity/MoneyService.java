package ph.parcs.rmhometiles.entity;

import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Service
public class MoneyService {

    public Money computeDiscount(Money amount, String discountPercent) {
        Number discountNumber = getDiscountPercent(discountPercent);

        Money discountAmount = parseMoney("0.00");
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

    public Money computeTotalAmountDue(Money totalAmount, String cashPaidText) {
        Money cashPaid = parseMoney(cashPaidText);
        if (totalAmount == null || cashPaid == null) return parseMoney("0.00");
        if (totalAmount.isGreaterThan(cashPaid)) {
            return cashPaid.minus(totalAmount);
        }
        return totalAmount.minus(cashPaid).abs();
    }

    public Money computeTotalAmount(Money currentTotal, Money taxAmount, Money deliveryRate) {
        Money totalAmount = currentTotal.minus(taxAmount);
        totalAmount = totalAmount.plus(deliveryRate);
        return totalAmount;
    }

    public Money parseMoney(String text) {
        if (!StringUtils.isEmpty(text)) {
            if(text.contains(".") && text.lastIndexOf(".") + 1 != text.length()){
                String[] split = text.split("\\.");
                String decimal = split[1];
                if(decimal.length() > 2){
                    text = split[0] + "." +  decimal.substring(0, 2);
                }
            }
            return Money.parse("PHP " + text);
        }
        return Money.parse("PHP 0.00");
    }
}
