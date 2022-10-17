package ph.parcs.rmhometiles.util.converter;

import javafx.util.converter.FloatStringConverter;
import org.apache.commons.lang3.StringUtils;

public class FloatConverter extends FloatStringConverter {

    @Override
    public Float fromString(String s) {
        if (StringUtils.isNumeric(s)) {
            return super.fromString(s);
        }
        return 0.00f;
    }
}
