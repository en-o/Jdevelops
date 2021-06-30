package com.detabes.time.core;

import com.detabes.constant.time.TimeFormat;
import com.detabes.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TimeUtilTest {

    @Test
    public void formatStr() {
        DateTime dateTime = TimeUtil.formatStr("20200202", TimeFormatEnum.PURE_FORMAT_DATETIME_DAY);
        assertNotNull(dateTime);
    }
    @Test
    public void resultNumbers() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        List<Integer> integers = TimeUtil.resultNumbers(DateTime.parse("2021-06-30 17:31:51",dateTimeFormatter).toDate());
//         [年，月，日，周几，12小时制小时，二十四小时制小时，分，秒]
        assertEquals(Arrays.asList(2021, 6, 30, 3, 5, 17, 31, 51),integers);
    }

    @Test
    public void time2DefaultFormat() {
        String format = TimeUtil.time2DefaultFormat();
        assertNotNull(format);
    }

    @Test
    public void testTime2DefaultFormat() {
        DateTime dateTime = DateTime.parse("2021-05-25 14:42:34", DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));
        Date date = dateTime.toDate();
        String format = TimeUtil.time2DefaultFormat(date);
        assertEquals("2021-05-25 14:42:34",format);
    }
    

    @Test
    public void testFindDates() {
        List<String> list = new ArrayList<>(Collections.singleton(DateTime.now().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY)));
        for (int i = 1; i < 4; i++) {
            list.add(DateTime.now().plusDays(i).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY));
        }
        assertEquals(TimeUtil.findDates(DateTime.now().toDate(), DateTime.now().plusDays(3).toDate()),list);

    }
    @Test
    public void result() {
        long result = TimeUtil.result(DateTime.now().toDate(), DateTime.now().plusDays(3).toDate());
        long result1 = TimeUtil.result(DateTime.now().plusDays(3).toDate(),DateTime.now().toDate());
        assertEquals(result,3);
        assertEquals(result1,-1);
    }

    @Test
    public void testGetMonthForWeek() throws ParseException {
        Map<Integer, List<String>> forWeek = TimeUtil.getMonthForWeek("2021-05");
        String week = "{1=[2021-05-01, 2021-05-02], 2=[2021-05-03, 2021-05-04, 2021-05-05, 2021-05-06, 2021-05-07, 2021-05-08, 2021-05-09], 3=[2021-05-10, 2021-05-11, 2021-05-12, 2021-05-13, 2021-05-14, 2021-05-15, 2021-05-16], 4=[2021-05-17, 2021-05-18, 2021-05-19, 2021-05-20, 2021-05-21, 2021-05-22, 2021-05-23], 5=[2021-05-24, 2021-05-25, 2021-05-26, 2021-05-27, 2021-05-28, 2021-05-29, 2021-05-30], 6=[2021-05-31]}";
        assertEquals(forWeek.toString(),week);
    }


    @Test
    public void testGetLastDayOfMonth() {
        String lastDayOfMonth = TimeUtil.getLastDayOfMonth("2021-05");
        assertEquals("2021-05-31",lastDayOfMonth);
    }
}