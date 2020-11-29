package ph.parcs.rmhometiles.util.converter;

import javafx.util.converter.IntegerStringConverter;
import org.apache.commons.lang3.StringUtils;

public class NumberConverter extends IntegerStringConverter {

    @Override
    public Integer fromString(String s) {
        if (StringUtils.isNumeric(s)) {
            return super.fromString(s);
        }
        return 0;
    }
}
