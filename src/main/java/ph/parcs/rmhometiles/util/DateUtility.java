package ph.parcs.rmhometiles.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

public class DateUtility {

    private final static HashMap<String, LocalDateTime[]> dateRangeSets = new HashMap<>();
    private final static int MAX_SIZE = 2;

    public static void initialize() {
        dateRangeSets.put("Today", createTodayDate());
        dateRangeSets.put("Yesterday", createYesterdayDate());
        dateRangeSets.put("This Week", createThisWeekDate());
        dateRangeSets.put("Last Week", createLastWeekDate());
        dateRangeSets.put("This Month", createThisMonthDate());
        dateRangeSets.put("Last Month", createLastMonthDate());
        dateRangeSets.put("This Year", createThisYearDate());
        dateRangeSets.put("Last Year", createLastYearDate());
        dateRangeSets.put("All Time", createAllTimeDate());
    }

    public static LocalDateTime[] findDate(String dateRange) {
        if (!StringUtils.isEmpty(dateRange)) return dateRangeSets.get(dateRange);
        return dateRangeSets.get("Today");
    }

    private static LocalDateTime[] createLocalDateTime(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime[] dateTimes = new LocalDateTime[MAX_SIZE];
        dateTimes[0] = startTime;
        dateTimes[1] = endTime;
        return dateTimes;
    }

    private static LocalDateTime[] createTodayDate() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        return createLocalDateTime(startTime, endTime);
    }

    private static LocalDateTime[] createYesterdayDate() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.MIN).minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX).minusDays(1);
        return createLocalDateTime(startTime, endTime);
    }

    private static LocalDateTime[] createThisWeekDate() {
        LocalDateTime nowStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime nowEnd = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime weekStart = nowStart.minusDays(nowStart.getDayOfWeek().getValue() - 1);
        return createLocalDateTime(weekStart, nowEnd);
    }

    private static LocalDateTime[] createLastWeekDate() {
        LocalDateTime now = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime weekStart = now.minusDays(7 + now.getDayOfWeek().getValue() - 1);
        LocalDateTime weekEnd = now.minusDays(now.getDayOfWeek().getValue());
        return createLocalDateTime(weekStart, weekEnd);
    }

    private static LocalDateTime[] createThisMonthDate() {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.now().with(LocalTime.MAX);

        LocalDateTime monthStart = todayStart.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime monthEnd = todayEnd.with(TemporalAdjusters.lastDayOfMonth());

        return createLocalDateTime(monthStart, monthEnd);
    }

    private static LocalDateTime[] createLastMonthDate() {
        LocalDateTime nowStart = LocalDateTime.now().with(LocalTime.MIN).minusMonths(1);
        LocalDateTime nowEnd = LocalDateTime.now().with(LocalTime.MAX).minusMonths(1);

        LocalDateTime monthStart = nowStart.withDayOfMonth(1);
        LocalDateTime monthEnd = nowEnd.with(TemporalAdjusters.lastDayOfMonth());
        return createLocalDateTime(monthStart, monthEnd);
    }

    private static LocalDateTime[] createThisYearDate() {
        LocalDateTime nowStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime nowEnd = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime yearStart = nowStart.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime yearEnd = nowEnd.with(TemporalAdjusters.lastDayOfYear());
        return createLocalDateTime(yearStart, yearEnd);
    }

    private static LocalDateTime[] createLastYearDate() {
        LocalDateTime nowStart = LocalDateTime.now().with(LocalTime.MIN).minusYears(1);
        LocalDateTime nowEnd = LocalDateTime.now().with(LocalTime.MAX).minusYears(1);

        LocalDateTime yearStart = nowStart.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime yearEnd = nowEnd.with(TemporalAdjusters.lastDayOfYear());

        return createLocalDateTime(yearStart, yearEnd);
    }

    private static LocalDateTime[] createAllTimeDate() {
        return createLocalDateTime(null, LocalDateTime.now().with(LocalTime.MAX));
    }
}
