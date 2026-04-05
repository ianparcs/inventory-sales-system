package ph.parcs.rmhometiles.util.date;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class DateUtil {


    public static final DateTimeFormatter FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("MMMM d, yyyy h:mm ")
            .appendText(java.time.temporal.ChronoField.AMPM_OF_DAY, TextStyle.SHORT)
            .toFormatter(Locale.US);
    private static final Map<DateRangeType, LocalDateTime[]> DATE_RANGES =
            new EnumMap<>(DateRangeType.class);

    static {
        DATE_RANGES.put(DateRangeType.TODAY, today());
        DATE_RANGES.put(DateRangeType.YESTERDAY, yesterday());
        DATE_RANGES.put(DateRangeType.THIS_WEEK, thisWeek());
        DATE_RANGES.put(DateRangeType.LAST_WEEK, lastWeek());
        DATE_RANGES.put(DateRangeType.THIS_MONTH, thisMonth());
        DATE_RANGES.put(DateRangeType.LAST_MONTH, lastMonth());
        DATE_RANGES.put(DateRangeType.THIS_YEAR, thisYear());
        DATE_RANGES.put(DateRangeType.LAST_YEAR, lastYear());
        DATE_RANGES.put(DateRangeType.ALL_TIME, allTime());
    }

    private DateUtil() {
    }

    // Public API
    public static LocalDateTime[] find(DateRangeType type) {
        return DATE_RANGES.getOrDefault(type, today());
    }

    // Utilities
    private static LocalDateTime[] range(LocalDateTime start, LocalDateTime end) {
        return new LocalDateTime[]{start, end};
    }

    private static LocalDateTime nowStart() {
        return LocalDateTime.now().with(LocalTime.MIN);
    }

    private static LocalDateTime nowEnd() {
        return LocalDateTime.now().with(LocalTime.MAX);
    }

    // Ranges
    private static LocalDateTime[] today() {
        return range(nowStart(), nowEnd());
    }

    private static LocalDateTime[] yesterday() {
        return range(nowStart().minusDays(1), nowEnd().minusDays(1));
    }

    private static LocalDateTime[] thisWeek() {
        LocalDateTime start = nowStart().with(java.time.DayOfWeek.MONDAY);
        return range(start, nowEnd());
    }

    private static LocalDateTime[] lastWeek() {
        LocalDateTime start = nowStart().with(java.time.DayOfWeek.MONDAY).minusWeeks(1);
        LocalDateTime end = nowEnd().with(java.time.DayOfWeek.SUNDAY).minusWeeks(1);
        return range(start, end);
    }

    private static LocalDateTime[] thisMonth() {
        return range(
                nowStart().with(TemporalAdjusters.firstDayOfMonth()),
                nowEnd().with(TemporalAdjusters.lastDayOfMonth())
        );
    }

    private static LocalDateTime[] lastMonth() {
        LocalDateTime baseStart = nowStart().minusMonths(1);
        LocalDateTime baseEnd = nowEnd().minusMonths(1);

        return range(
                baseStart.with(TemporalAdjusters.firstDayOfMonth()),
                baseEnd.with(TemporalAdjusters.lastDayOfMonth())
        );
    }

    private static LocalDateTime[] thisYear() {
        return range(
                nowStart().with(TemporalAdjusters.firstDayOfYear()),
                nowEnd().with(TemporalAdjusters.lastDayOfYear())
        );
    }

    private static LocalDateTime[] lastYear() {
        LocalDateTime baseStart = nowStart().minusYears(1);
        LocalDateTime baseEnd = nowEnd().minusYears(1);

        return range(
                baseStart.with(TemporalAdjusters.firstDayOfYear()),
                baseEnd.with(TemporalAdjusters.lastDayOfYear())
        );
    }

    private static LocalDateTime[] allTime() {
        return range(null, nowEnd());
    }
}