package ph.parcs.rmhometiles.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
        dateRangeSets.put("This Year", createTodayDate());
        dateRangeSets.put("Last Year", createTodayDate());
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
        LocalDateTime endTime = getTodayEndTime();
        return createLocalDateTime(startTime, endTime);
    }

    private static LocalDateTime[] createYesterdayDate() {
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.MIN).minusDays(1);
        LocalDateTime endTime = getTodayEndTime().minusDays(1);
        return createLocalDateTime(startTime, endTime);
    }

    private static LocalDateTime[] createThisWeekDate() {
        LocalDateTime nowStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime nowEnd =  LocalDateTime.now().with(LocalTime.MAX);
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
        LocalDateTime now = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime monthStart = now.minusDays(now.getDayOfMonth());
        return createLocalDateTime(monthStart, now);
    }

    private static LocalDateTime[] createLastMonthDate() {
        LocalDateTime now = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime monthStart = now.minusMonths(now.getDayOfMonth());
        return createLocalDateTime(monthStart, now);
    }

    private static LocalDateTime[] createAllTimeDate() {
        return createLocalDateTime(null, getTodayEndTime());
    }

    private static LocalDateTime getTodayEndTime() {
        return LocalDateTime.now().with(LocalTime.MAX);
    }

}
