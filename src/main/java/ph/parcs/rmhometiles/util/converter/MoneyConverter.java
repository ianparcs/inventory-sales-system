package ph.parcs.rmhometiles.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;


@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, String> {

    @SneakyThrows
    public static Money convert(String value) {
        BigDecimal bc = new BigDecimal(value);
        return Money.of(CurrencyUnit.ofCountry("PH"), bc);
    }

    @Override
    public String convertToDatabaseColumn(Money attribute) {
        return attribute == null ? null : attribute.toString();
        // outputs "PHP 123.45"
    }
    @Override
    public Money convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Money.parse(dbData);
    }

}
