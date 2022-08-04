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

    public Money computeMoneyChange(Money balance, String cashPaidText) {
        Money cashPaid = parseMoney(cashPaidText);
        if (balance == null || cashPaid == null) return parseMoney("0.00");
        if (balance.isGreaterThan(cashPaid)) {
            return cashPaid.minus(balance);
        }
        return balance.minus(cashPaid).abs();
    }

    public Money computeBalance(Money balance, String paidText) {
        Money paid = parseMoney(paidText);
        if (balance == null || paid == null) return parseMoney("0.00");
        if (paid.isGreaterThan(balance)) {
            return parseMoney("0.00");
        }
        return balance.minus(paid);
    }

    public Money computeTotalAmount(Money currentTotal, Money taxAmount, Money deliveryRate) {
        Money totalAmount = currentTotal.minus(taxAmount);
        totalAmount = totalAmount.plus(deliveryRate);
        return totalAmount;
    }

    public Money parseMoney(String text) {
        if (!StringUtils.isEmpty(text)) {
            if (text.contains(".") && text.lastIndexOf(".") + 1 != text.length()) {
                String[] split = text.split("\\.");
                String decimal = split[1];
                if (decimal.length() > 2) {
                    text = split[0] + "." + decimal.substring(0, 2);
                }
            }
            return Money.parse("PHP " + text);
        }
        return Money.parse("PHP 0.00");
    }
}
