package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/20 13:31
 */
public class DateUtil {

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     */
    public static String today() {
        return DateTime.now().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }

    /**
     * 昨天
     *
     * @return 昨天
     */
    public static DateTime yesterday() {
        return DateTime.now().plusDays(-1);
    }

    /**
     * 明天
     * @return 明天
     */
    public static DateTime tomorrow() {
        return DateTime.now().plusDays(1);
    }

    /**
     * 上周
     * @return 上周
     */
    public static DateTime lastWeek() {
        return DateTime.now().plusWeeks(-1);
    }

    /**
     * 下周
     * @return 下周
     */
    public static DateTime nextWeek() {
        return DateTime.now().plusWeeks(1);
    }

    /**
     * 上个月
     * @return 上个月
     */
    public static DateTime lastMonth() {
        return DateTime.now().plusMonths(-1);
    }

    /**
     * 下个月
     * @return 下个月
     */
    public static DateTime nextMonth() {
        return DateTime.now().plusMonths(1);
    }


    /**
     * 去年
     * @return 去年
     */
    public static DateTime lastYear() {
        return DateTime.now().plusYears(-1);
    }

    /**
     * 明年
     * @return 明年
     */
    public static DateTime nextYears() {
        return DateTime.now().plusYears(1);
    }


    /**
     * 获取当前时间的周末
     *
     * @return yyyy-MM-dd
     */
    public static String nowWeekend() {
        return DateTime.now().dayOfWeek().withMaximumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }

    /**
     * 获取 当前月的第一天
     *
     * @return yyyy-MM-dd
     */
    public static String nowBeginMonth() {
        return DateTime.now().dayOfMonth().withMinimumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);

    }

    /**
     * 获取  当前月的最后一天
     *
     * @return yyyy-MM-dd
     */
    public static String nowEndMonth() {
        return DateTime.now().dayOfMonth().withMaximumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }

    /**
     * 获取当前时间的周一
     *
     * @return yyyy-MM-dd
     */
    public static String nowMonday() {
        return DateTime.now().dayOfWeek().withMinimumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }


    /**
     * 月最后一天
     *
     * @param yearMonth yyyy-MM
     * @return String
     */
    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.split("-")[0]);
        int month = Integer.parseInt(yearMonth.split("-")[1]);
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份(设置当前月的上一个月)
        cal.set(Calendar.MONTH, month);
        // 获取某月最大天数(获取月份中的最小值，即第一天)
        int lastDay = cal.getMinimum(Calendar.DATE);
        // 设置日历中月份的最大天数(上月的第一天减去1就是当月的最后一天)
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 某个时间段内所有的日期
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return {List}
     */
    public static List<String> findDates(Date beginDate, Date endDate) {
        List<String> dateList = new ArrayList<>();
        String begin = new DateTime(beginDate).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
        dateList.add(begin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);
        // 测试此日期是否在指定日期之后
        while (endDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            String end = new DateTime(calBegin.getTime()).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
            dateList.add(end);
        }
        return dateList;
    }

}
