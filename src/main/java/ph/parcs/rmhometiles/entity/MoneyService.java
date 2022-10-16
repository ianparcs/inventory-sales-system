package ph.parcs.rmhometiles.entity;

import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.report.SalesReport;
import ph.parcs.rmhometiles.util.Global;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return paid.minus(balance);
    }

    public Money computeTotalAmount(Money currentTotal, Money taxAmount, Money deliveryRate) {
        Money totalAmount = currentTotal.plus(taxAmount);
        totalAmount = totalAmount.plus(deliveryRate);
        return totalAmount;
    }

    public Map<Global.Sales, String> computeAllMoney(List<SalesReport> salesReportsToday) {
        if (salesReportsToday == null) return new HashMap<>();

        Map<Global.Sales, String> moneyMap = new HashMap<>();
        Money tax = parseMoney("0.00");
        Money cost = parseMoney("0.00");
        Money total = parseMoney("0.00");

        for (SalesReport salesReport : salesReportsToday) {
            tax = tax.plus(salesReport.getTax() != null ? salesReport.getTax() : parseMoney("0.00"));
            cost = cost.plus(salesReport.getCost() != null ? salesReport.getCost() : parseMoney("0.00"));
            total = total.plus(salesReport.getTotal() != null ? salesReport.getTotal() : parseMoney("0.00"));
        }
        Money profit = total.minus(cost);

        moneyMap.put(Global.Sales.TAX, tax.toString().replace("PHP","₱"));
        moneyMap.put(Global.Sales.COST, cost.toString().replace("PHP","₱"));
        moneyMap.put(Global.Sales.TOTAL, total.toString().replace("PHP","₱"));
        moneyMap.put(Global.Sales.PROFIT, profit.toString().replace("PHP","₱"));
        return moneyMap;
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
