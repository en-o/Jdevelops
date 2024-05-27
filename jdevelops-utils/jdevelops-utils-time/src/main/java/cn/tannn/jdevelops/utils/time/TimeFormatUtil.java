package cn.tannn.jdevelops.utils.time;


import cn.tannn.jdevelops.utils.time.enums.SqlTimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间格式之间的处理
 *
 * @author tn
 * @version 1
 * @date 2020/12/15 11:24
 */
public class TimeFormatUtil {

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
     * yyyy-MM-dd HH:mm:ss字符串格式转另一种字符串格式
     *
     * @param defTimeStr          待转格式的时间字符串
     * @param newTimeFormat 新的格式
     * @return timeStr
     */
    public static String str2StrTime(String defTimeStr, TimeFormatEnum newTimeFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        DateTime parse = DateTime.parse(defTimeStr, formatter);
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
     * Timestamp  转 LocalDateTime
     *
     * @param timestamp Timestamp
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }
    /**
     * org.joda.time.LocalDateTime 转化成  java.time.LocalDateTime
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
     * 默认字符串时间格式转 java.time.LocalDateTime
     *
     * @param defTimeStr yyyy-MM-dd HH:mm:ss
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime str2LocalDateTime(String defTimeStr) {
        java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        return java.time.LocalDateTime.parse(defTimeStr, df);
    }

    /**
     * 默认字符串时间格式转 java.time.LocalDateTime
     *
     * @param strDateTime 时间字符串
     * @param format      时间字符串的格式
     * @return java.time.LocalDateTime
     */
    public static java.time.LocalDateTime str2LocalDateTime(String strDateTime, String format) {
        java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ofPattern(format);
        return java.time.LocalDateTime.parse(strDateTime, df);
    }



    /**
     * java.time.LocalDate 转 java.time.Date
     * @param localDate LocalDate
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * java.time.LocalDateTime 转化成 java.util.Date
     *
     * @param localDateTime java.time.LocalDateTime
     * @return java.util.Date
     */
    public static Date LocalDateTimeToDate(java.time.LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDate
     * @param date date
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }


    /**
     * 格式化时间字符传为 dateTime
     *
     * @param time       时间
     * @param timeFormat 时间类型
     * @return DateTime
     */
    public static DateTime formatStr(String time, TimeFormatEnum timeFormat) {
        org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(timeFormat.getFormat());
        return DateTime.parse(time, dateTimeFormatter);
    }


    /**
     * 时间字符串格式转另外一个格式
     * @deprecated (TimeConvertUtil)
     * @param time 时间字符串
     * @param timeFormat 格式
     * @param newTimeFormat 需要转换的新格式
     * @return String
     */
    @Deprecated
    public static String formatStr2Str(String time, TimeFormatEnum timeFormat,
                                       TimeFormatEnum newTimeFormat) {
        DateTime dateTime = formatStr(time, timeFormat);
        return dateTime.toString(newTimeFormat.getFormat());
    }

    /**
     * 时间字符串格式转 yyyy-MM-dd HH:mm:ss 格式的字符串
     * @param time 时间字符串
     * @param timeFormat 格式
     * @return String
     */
    public static String formatStr2StrByDefault(String time, TimeFormatEnum timeFormat) {
        DateTime dateTime = formatStr(time, timeFormat);
        return dateTime.toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
    }


    /**
     * LocalDateTime 转 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime java.time.LocalDateTime
     * @return {yyyy-MM-dd HH:mm:ss}
     */
    public static String localDateTimeToString(java.time.LocalDateTime localDateTime) {
        return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 去除字符串时间的"-",返回格式"yyMMdd"
     *
     * @param time time
     * @return Integer
     */
    public static Integer transferTime(String time) {
        return Integer.parseInt(time.replaceAll("-", ""));
    }




    /**
     * 获取所有java用的时间格式字符串
     *
     * @return 所有时间格式
     * @throws IllegalAccessException 非法
     */
    static List<String> javaTimeFormats() throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        Field[] fields = TimeFormat.class.getFields();
        for (Field f : fields) {
            Object value = f.get(TimeFormat.class);
            list.add(value.toString());
        }
        return list;
    }

    /**
     * 获取所有sql用的时间格式字符串
     *
     * @return 所有时间格式
     * @throws IllegalAccessException 非法
     */
    static List<String> sqlTimeFormats() throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        Field[] fields = SqlTimeFormat.class.getFields();
        for (Field f : fields) {
            Object value = f.get(SqlTimeFormat.class);
            list.add(value.toString());
        }
        return list;
    }


    /**
     * 获取所有mysql用的时间格式字符串
     *
     * @return 所有时间格式
     * @throws IllegalAccessException 非法
     */
    static List<String> mySqlFormats() throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        Field[] fields = SqlTimeFormat.class.getFields();
        for (Field f : fields) {
            String name = f.getName();
            if (name.startsWith("MYSQL_")) {
                Object value = f.get(SqlTimeFormat.class);
                list.add(value.toString());
            }
        }
        return list;
    }

    /**
     * 将mysql的时间格式转换成java的
     *
     * @param format 格式
     * @return {String}
     */
    public static String mysql2Java(String format) {
        switch (format) {
            case SqlTimeFormat.MYSQL_FORMAT_DATETIME_SECOND:
                format = TimeFormat.DEFAULT_FORMAT_DATETIME;
                break;
            case SqlTimeFormat.MYSQL_FORMAT_DATETIME_DAY:
                format = TimeFormat.NORM_FORMAT_DATETIME_DAY;
                break;
            case SqlTimeFormat.MYSQL_FORMAT_DATETIME_MONTH:
                format = TimeFormat.NORM_FORMAT_DATETIME_MONTH;
                break;
            case SqlTimeFormat.MYSQL_FORMAT_DATETIME_YEAR:
                format = TimeFormat.NORM_FORMAT_DATETIME_YEAR;
                break;
            case SqlTimeFormat.MYSQL_FORMAT_DATETIME_SIMPLE_DAY:
                format = TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_SECOND;
                break;
            default:
                break;
        }
        return format;
    }
}
