package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 时间 验证
 *
 * @author tn
 * @version 1
 * @date 2021-10-25 15:13
 */
public class TimeVerifyUtil {

    /**
     * 验证 开始时间 大于等于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于(等于)  ， false 小于
     */
    public static boolean beginGteEnd(Date begin, Date end) {
        return begin.toInstant().isAfter(end.toInstant()) || begin.toInstant().equals(end.toInstant());
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
        return begin.isAfter(end) || begin.isEqual(end);
    }


    /**
     * 验证 开始时间 大于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于 ， false 小于（等于）
     */
    public static boolean beginGtEnd(Date begin, Date end) {
        return begin.toInstant().isAfter(end.toInstant());
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
        return begin.isAfter(end);
    }

    /**
     * 验证时间是否大于等于现在
     *
     * @param defTimeFormat "2021-10-21 13:24:30"
     * @return true 大于等于
     */
    public static boolean timeGteNow(String defTimeFormat) {
        DateTime begin = TimeFormatUtil.formatStr(defTimeFormat, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        DateTime end = new DateTime();
        return begin.isAfter(end) || begin.isEqual(end);
    }

    /**
     * 验证 默认字符串时间格式是否大于现在
     *
     * @param defTimeFormat "2021-10-21 13:24:30"
     * @return true 大于
     */
    public static boolean timeGtNow(String defTimeFormat) {
        DateTime dateTime = TimeFormatUtil.formatStr(defTimeFormat, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        DateTime end = new DateTime();
        return dateTime.isAfter(end);
    }



    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear() {
        DateTime time = new DateTime();
        DateTime.Property year = time.year();
        return year.isLeap();
    }


    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear(Date date) {
        DateTime time = new DateTime(date);
        DateTime.Property year = time.year();
        return year.isLeap();
    }

    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear(int year) {
        Date date = TimeFormatUtil.str2Date(String.valueOf(year), TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR);
        return verifyLeapYear(date);
    }


    /**
     * 时间格式验证
     *
     * @param sDate  校验的日期
     * @param format 校验的格式
     * @return true  时间字符串格式=format
     */
    public static boolean verifyFormat(String sDate, TimeFormatEnum format) {
        if (Objects.isNull(sDate) || sDate.isEmpty()) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(format.getFormat());
            DateTime.parse(sDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /***
     *  比较时间先后顺序，a<b,return true
     *@date 2020/6/5
     *@param  aTime （HH:mm）, bTime（HH:mm）
     *@return java.lang.Boolean
     */
    public static Boolean checkTimeBefore(String aTime, String bTime) throws ParseException {

        if (aTime.length() != bTime.length() || aTime.length() != 5) {
            throw new NullPointerException("时间格式不正确");
        } else {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date asd = df.parse(aTime);
            Date bsd = df.parse(bTime);
            return asd.before(bsd);
        }

    }

}
