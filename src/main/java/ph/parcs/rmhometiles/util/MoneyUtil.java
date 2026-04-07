package ph.parcs.rmhometiles.util;

import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;

public class MoneyUtil {

    private static final MoneyFormatter MONEY_FMT =
            new MoneyFormatterBuilder()
                    .appendLiteral("₱")
                    .appendAmount()
                    .toFormatter();

    public static String print(Money money) {
        if (money == null) return MONEY_FMT.print(Money.parse("PHP 0.00"));
        return MONEY_FMT.print(money);
    }
}
