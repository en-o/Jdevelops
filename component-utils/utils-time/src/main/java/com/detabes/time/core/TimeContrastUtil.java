package com.detabes.time.core;

import com.detabes.constant.time.TimeFormat;
import com.detabes.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 时间比较
 *
 * @author tn
 * @version 1
 * @date 2021-10-25 15:13
 */
public class TimeContrastUtil {

    /**
     * 验证 开始时间 大于等于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于(等于)  ， false 小于
     */
    public static boolean beginGteEnd(Date begin, Date end) {
        DateTime beginTime = new DateTime(begin);
        DateTime endTime = new DateTime(end);
        int seconds = Seconds.secondsBetween(beginTime, endTime).getSeconds();
        return seconds <= 0;
    }


    /**
     * 验证 开始时间 大于等于 结束时间
     * ps：默认字符串时间格式
     * @param defTimeFormatBegin      开始 "2021-10-21 13:24:30"
     * @param defTimeFormatBeginEnd   结束 "2021-10-21 13:24:30"
     * @return true 大于(等于)  ， false 小于
     */
    public static boolean beginGteEnd(String defTimeFormatBegin,
                                      String defTimeFormatBeginEnd) {
        DateTime begin = DateTime.parse(defTimeFormatBegin,
                DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));

        DateTime end = DateTime.parse(defTimeFormatBeginEnd,
                DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));
        return beginGteEnd(begin.toDate(), end.toDate());
    }


    /**
     * 验证 开始时间 大于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于 ， false 小于（等于）
     */
    public static boolean beginGtEnd(Date begin, Date end) {
        DateTime beginTime = new DateTime(begin);
        DateTime endTime = new DateTime(end);
        int seconds = Seconds.secondsBetween(beginTime, endTime).getSeconds();
        return seconds < 0;
    }


    /**
     * 验证 开始时间 大于 结束时间
     * ps：默认字符串时间格式
     * @param defTimeFormatBegin      开始 "2021-10-21 13:24:30"
     * @param defTimeFormatBeginEnd   结束 "2021-10-21 13:24:30"
     * @return  true 大于 ， false 小于（等于）
     */
    public static boolean beginGtEnd(String defTimeFormatBegin,
                                      String defTimeFormatBeginEnd) {
        DateTime begin = DateTime.parse(defTimeFormatBegin,
                DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));

        DateTime end = DateTime.parse(defTimeFormatBeginEnd,
                DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));
        return beginGtEnd(begin.toDate(), end.toDate());
    }

    /**
     * 验证时间是否大于等于现在
     *
     * @param defTimeFormat "2021-10-21 13:24:30"
     * @return true 大于等于
     */
    public static boolean timeGteNow(String defTimeFormat) {
        DateTime dateTime = TimeUtil.formatStr(defTimeFormat, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        int seconds = Seconds.secondsBetween(dateTime, new DateTime()).getSeconds();
        return seconds <= 0;
    }

    /**
     * 验证 默认字符串时间格式是否大于现在
     *
     * @param defTimeFormat "2021-10-21 13:24:30"
     * @return true 大于
     */
    public static boolean timeGtNow(String defTimeFormat) {
        DateTime dateTime = TimeUtil.formatStr(defTimeFormat, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        int seconds = Seconds.secondsBetween(dateTime, new DateTime()).getSeconds();
        return seconds < 0;
    }





}
