package cn.jdevelops.util.time.core;

import cn.jdevelops.util.time.enums.TimeFormat;
import org.joda.time.DateTime;

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

}
