package cn.jdevelops.time.core;

import cn.jdevelops.constant.time.TimeFormat;
import cn.jdevelops.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 时间格式转换相关
 *
 * @author tn
 * @version 1
 * @date 2021-10-25 15:00
 */
public class TimeConvertUtil {

    /**
     * 获取当前时间的默认字符格式
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String thisDefNow() {
        return DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
    }

    /**
     * 获取 苹果系统上的默认时间格式
     *
     * @return yyyy/MM/dd HH:mm:ss
     */
    public static String thisMacNow() {
        return DateTime.now().toString(TimeFormat.EN_FORMAT_DATETIME_SECOND);
    }

    /**
     * 字符串格式转另一种字符串格式
     *
     * @param time          待转格式的时间字符串
     * @param timeFormat    待转格式的时间字符串的格式
     * @param newTimeFormat 新的格式
     * @return timeStr
     */
    public static String str2StrTime(String time, TimeFormatEnum timeFormat, TimeFormatEnum newTimeFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(timeFormat.getFormat());
        DateTime parse = DateTime.parse(time, formatter);
        return parse.toString(newTimeFormat.getFormat());
    }

    /**
     * 默认格式转 util.Date
     *
     * @param timeStr yyyy-MM-dd HH:mm:ss
     * @return util.Date
     */
    public static Date defStr2Date(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        return DateTime.parse(timeStr, formatter).toDate();
    }

    /**
     * 指定时间字符串格式转 util.Date
     *
     * @param timeStr    时间字符串
     * @param timeFormat 时间字符串格式
     * @return util.Date
     */
    public static Date str2Date(String timeStr, TimeFormatEnum timeFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(timeFormat.getFormat());
        return DateTime.parse(timeStr, formatter).toDate();
    }


    /**
     * 指定时间字符串格式转 joda.time.DateTime
     *
     * @param timeStr    时间字符串
     * @param timeFormat 时间字符串格式
     * @return joda.time.DateTime
     */
    public static DateTime str2DateTime(String timeStr, TimeFormatEnum timeFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(timeFormat.getFormat());
        return DateTime.parse(timeStr, formatter).toDateTime();
    }


    /**
     * 时间 转默认时间字符串
     *
     * @param date util.Date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2DefStr(Date date) {
        return new DateTime(date).toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
    }

    /**
     * 时间 转默认时间字符串
     *
     * @param date joda.time.DateTime
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateTime2DefStr(DateTime date) {
        return date.toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
    }

    /**
     * java.util.Date 转化成 java.time.LocalDateTime
     *
     * @param date java.util.Date
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * java.time.LocalDateTime 转化成 java.util.Date
     *
     * @param localDateTime java.time.LocalDateTime
     * @return java.util.Date
     */
    public static Date localDateTime2Date(java.time.LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * joda.time.LocalDateTime  转化成  java.time.LocalDateTime
     *
     * @param localDateTime joda.time.LocalDateTime
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime joda2JavaLocalDateTime(LocalDateTime localDateTime) {
        Instant instant = localDateTime.toDate().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return java.time.LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * 获取当前时间的周一
     * @return yyyy-MM-dd
     */
    public static String nowMonday() {
        return DateTime.now().dayOfWeek().withMinimumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }

    /**
     * 获取当前时间的周末
     * @return yyyy-MM-dd
     */
    public static String nowWeekend() {
        return DateTime.now().dayOfWeek().withMaximumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }

    /**
     *获取 当前月的第一天
     * @return yyyy-MM-dd
     */
    public static String nowBeginMonth(){
        return  DateTime.now().dayOfMonth().withMinimumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);

    }

    /**
     *获取  当前月的最后一天
     * @return yyyy-MM-dd
     */
    public static String nowEndMonth(){
        return DateTime.now().dayOfMonth().withMaximumValue().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
    }


    /**
     * 默认字符串时间格式转 java.time.LocalDateTime
     * @param defTimeStr yyyy-MM-dd HH:mm:ss
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime str2LocalDate(String defTimeStr) {
        java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        return java.time.LocalDateTime.parse(defTimeStr, df);
    }

    /**
     * 默认字符串时间格式转 java.time.LocalDateTime
     * @param strDateTime 时间字符串
     * @param format 时间字符串的格式
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime str2LocalDate(String strDateTime, String format) {
        java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ofPattern(format);
        return java.time.LocalDateTime.parse(strDateTime, df);
    }

}
