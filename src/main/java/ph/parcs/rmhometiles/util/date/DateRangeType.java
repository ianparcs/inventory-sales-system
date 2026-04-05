package ph.parcs.rmhometiles.util.date;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DateRangeType {
    TODAY("Today"),
    YESTERDAY("Yesterday"),
    THIS_WEEK("This Week"),
    LAST_WEEK("Last Week"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    THIS_YEAR("This Year"),
    LAST_YEAR("Last Year"),
    ALL_TIME("All Time");

    private final String value;

    DateRangeType(String value) {
        this.value = value;
    }

    public static DateRangeType fromValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown range: " + value));
    }


}
