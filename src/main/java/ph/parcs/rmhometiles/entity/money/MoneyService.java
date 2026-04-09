package ph.parcs.rmhometiles.entity.money;

import org.joda.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ph.parcs.rmhometiles.entity.report.SalesReport;
import ph.parcs.rmhometiles.util.AppConstant;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MoneyService {
    public Money computeTax(Money amount) {
        return amount.multipliedBy(AppConstant.TAX, RoundingMode.UNNECESSARY);
    }

    public Money computeBalance(Money balance, Money cashPaid) {
        if (balance == null || cashPaid == null) {
            return parseMoney("0.00");
        }
        Money result = balance.minus(cashPaid);
        return result.isNegative() ? parseMoney("0.00") : result;
    }

    public Map<AppConstant.Sales, Double> computeAllMoney(List<SalesReport> salesReportsToday) {
        if (salesReportsToday == null) return new HashMap<>();

        Money tax = parseMoney("0.00");
        Money cost = parseMoney("0.00");
        Money total = parseMoney("0.00");

        for (SalesReport salesReport : salesReportsToday) {
            tax = tax.plus(salesReport.getTax() != null ? salesReport.getTax() : parseMoney("0.00"));
            cost = cost.plus(salesReport.getCost() != null ? salesReport.getCost() : parseMoney("0.00"));
            total = total.plus(salesReport.getTotal() != null ? salesReport.getTotal() : parseMoney("0.00"));
        }
        Money profit = total.minus(cost);

        Map<AppConstant.Sales, Double> moneyMap = new HashMap<>();
        moneyMap.put(AppConstant.Sales.TAX, tax.getAmount().doubleValue());
        moneyMap.put(AppConstant.Sales.COST, cost.getAmount().doubleValue());
        moneyMap.put(AppConstant.Sales.TOTAL, total.getAmount().doubleValue());
        moneyMap.put(AppConstant.Sales.PROFIT, profit.getAmount().doubleValue());
        return moneyMap;
    }

    public Money parseMoney(String text) {
        if (StringUtils.hasText(text)) {
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

    public Money computeTotalMoney(Stream<Money> totalMoney) {
        return totalMoney.reduce(Money.parse("PHP 0.00"), Money::plus);
    }

    public Money computeTotalAmount(Money subTotal, Money tax, Money discount, Money deliveryRate) {
        return subTotal
                .plus(tax)
                .plus(deliveryRate)
                .minus(discount);
    }

}
