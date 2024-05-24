package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.tannn.jdevelops.utils.time.DateUtil.getLastDayOfMonth;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/24 下午3:44
 */
public class TimeUtil {


    /**
     * 获取当前时间的字符格式
     *
     * @param timeFormat 需要的格式
     * @return string of timeFormat
     */
    public static String thisNow(TimeFormatEnum timeFormat) {
        return DateTime.now().toString(timeFormat.getFormat());
    }


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
     * 计算时间差
     *
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param beginTimeFormat 开始时间格式
     * @param endTimeFormat   结束时间格式
     * @param returnTime      输出类型 0：天，1:小时，2:分钟，3：秒，4：毫秒
     */
    public static Long timeDifference(String beginTime, TimeFormatEnum beginTimeFormat,
                                      String endTime, TimeFormatEnum endTimeFormat,
                                      Integer returnTime) {

        org.joda.time.format.DateTimeFormatter beginFormatter = DateTimeFormat.forPattern(beginTimeFormat.getFormat());
        org.joda.time.format.DateTimeFormatter endTtimeFormatter = DateTimeFormat.forPattern(endTimeFormat.getFormat());
        DateTime begin = DateTime.parse(beginTime, beginFormatter);
        DateTime end = DateTime.parse(endTime, endTtimeFormatter);
//        计算区间毫秒数
        Duration etime = new Duration(begin, end);
        switch (returnTime) {
            case 0:
                return etime.getStandardDays();
            case 1:
                return etime.getStandardHours();
            case 2:
                return etime.getStandardMinutes();
            case 3:
                return etime.getStandardSeconds();
            default:
                return etime.getMillis();
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss 格式的字符串转为long
     *
     * @param datetime yyyy-MM-dd HH:mm:ss
     * @return long
     */
    public static long dateTimeMillis(String datetime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        DateTime dateTime = DateTime.parse(datetime, formatter);
        return dateTime.getMillis();
    }



    /**
     * 获取指定月份的以周分组的结果集
     *
     * @param times 指定 月份  eg：2021-05
     * @return Map
     */
    public static Map<Integer, List<String>> getMonthForWeek(String times) throws ParseException {
        Map<Integer, List<String>> map = new HashMap<>(10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cBegin = new GregorianCalendar();
        Calendar cEnd = new GregorianCalendar();
        DateFormatSymbols dfs = new DateFormatSymbols();
        cBegin.setTime(sdf.parse(times + "-01"));
        cEnd.setTime(sdf.parse(getLastDayOfMonth(times)));
        //  c_begin.set(2019, 3, 2); //Calendar的月从0-11，所以4月是3.
        //  c_end.set(2019, 3, 29); //Calendar的月从0-11，所以5月是4.

        int count = 1;
        int count2 = 1;
        //结束日期下滚一天是为了包含最后一天
        cEnd.add(Calendar.DAY_OF_YEAR, 1);
        ArrayList<String> objects = new ArrayList<>();
        while (cBegin.before(cEnd)) {
            if (count == count2) {
                objects = new ArrayList<>();
                count2++;
            }
            objects.add(new java.sql.Date(cBegin.getTime().getTime()).toString());
            if (cBegin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                map.put(count, objects);
                count++;
            }
            cBegin.add(Calendar.DAY_OF_YEAR, 1);

        }
        map.put(count, objects);
        return map;
    }



    /**
     * 获取指定年份的第一天日期
     * @param year           指定年份
     * @param timeFormatEnum 指定时间格式
     * @return {String}   yy-mm-dd 00:00:00
     */
    public static String getYearFirst(int year, TimeFormatEnum timeFormatEnum) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return new DateTime(currYearFirst).toString(timeFormatEnum.getFormat());
    }

    /**
     * 获取指定年份最后一天日期
     *
     * @param year           指定年份
     * @param timeFormatEnum 指定返回时间格式
     * @return {String}   timeFormatEnum
     */
    public static String getYearLast(int year, TimeFormatEnum timeFormatEnum) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        String format8 = new DateTime(currYearLast).toString(timeFormatEnum.getFormat());
        if (TimeFormat.DEFAULT_FORMAT_DATETIME.equals(timeFormatEnum.getFormat())) {
            format8 = new DateTime(currYearLast).withMillisOfDay(0).plusHours(23).plusMinutes(59).plusSeconds(59).toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
        }
        return format8;
    }


    /**
     * 获取当前时间的前 后N小时
     *
     * @param hour   +后 -前 N小时 (1-24之间)
     * @param format 返回格式
     * @return String
     */
    public static String getTimeBeginAfterTime(final Integer hour, final TimeFormat format) {
        return DateTime.now().plusHours(hour).toString(format.toString());
    }

    /**
     * *  获取指定时间的前 后N小时
     *
     * @param time      +后 -前 N小时 (1-24之间)
     * @param inFormat  指定的时间的格式
     * @param outFormat 时间返回格式
     * @return String
     */
    public static String getTimeBeginAfterTime(final String time,
                                               final Integer hour,
                                               final TimeFormat inFormat,
                                               final TimeFormat outFormat) {
        org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(inFormat.toString());
        DateTime dateTime = DateTime.parse(time, dateTimeFormatter);
        return dateTime.plusHours(hour).toString(outFormat.toString());
    }


    /**
     * 当前时间的数字拆分数字集合
     *
     * @param date 时间
     * @return [年，月，日，周几，12小时制小时，二十四小时制小时，分，秒]
     */
    public static List<Integer> resultNumbers(Date date) {
        List<Integer> l = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        l.add(c.get(Calendar.YEAR));
        l.add(c.get(Calendar.MONTH) + 1);
        l.add(c.get(Calendar.DATE));
        l.add(c.get(Calendar.DAY_OF_WEEK) - 1);
        l.add(c.get(Calendar.HOUR));
        l.add(c.get(Calendar.HOUR_OF_DAY));
        l.add(c.get(Calendar.MINUTE));
        l.add(c.get(Calendar.SECOND));
        return l;
    }

}
