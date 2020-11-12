package ph.parcs.rmhometiles.util;

import lombok.SneakyThrows;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

public class MoneyConverter {


    @SneakyThrows
    public static Money convert(String value) {
        BigDecimal bc = new BigDecimal(value);
        return Money.of(CurrencyUnit.ofCountry("PH"), bc);
    }
}
