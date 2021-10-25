package com.detabes.time.core;

import com.detabes.enums.time.TimeFormatEnum;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Date;


public class TimeConvertUtilTest extends TestCase {

    public void testThisDefNow() {
        assertNotNull(TimeConvertUtil.thisDefNow());
    }

    public void testThisMacNow() {
        assertNotNull(TimeConvertUtil.thisMacNow());
    }

    public void testStr2StrTime() {
        assertEquals(TimeConvertUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.DEFAULT_FORMAT_DATETIME,
                TimeFormatEnum.CN_FORMAT_DATETIME_DAY), "2021年01月09日");
        assertEquals(TimeConvertUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.DEFAULT_FORMAT_DATETIME,
                TimeFormatEnum.EN_FORMAT_DATETIME_SECOND), "2021/01/09 22:12:00");

    }

    public void testDefStr2Date() {
        assertNotNull(TimeConvertUtil.defStr2Date("2021-01-09 22:12:00"));
    }

    public void testStr2Date() {
        assertNotNull(TimeConvertUtil.str2Date("2021-01-09",TimeFormatEnum.NORM_FORMAT_DATETIME_DAY));
    }

    public void testDate2DefStr() {
        assertNotNull(TimeConvertUtil.date2DefStr(new Date()));
    }

    public void testDateTime2DefStr() {
        assertNotNull(TimeConvertUtil.dateTime2DefStr(new DateTime()));
    }

    public void testDate2LocalDate() {
        assertNotNull(TimeConvertUtil.date2LocalDate(new Date()));
    }

    public void testLocalDateTime2Date() {
        assertNotNull(TimeConvertUtil.localDateTime2Date(java.time.LocalDateTime.now()));
    }

    public void testJoda2JavaLocalDateTime() {
        assertNotNull(TimeConvertUtil.joda2JavaLocalDateTime(new LocalDateTime()));
    }

//    public void testNowMonday() {
//        assertEquals(TimeConvertUtil.nowMonday(),"2021-10-25");
//    }
//
//    public void testNowWeekend() {
//        assertEquals(TimeConvertUtil.nowWeekend(),"2021-10-31");
//    }
//
//    public void testNowBeginMonth() {
//        assertEquals(TimeConvertUtil.nowBeginMonth(),"2021-10-01");
//    }
//
//    public void testNowEndMonth() {
//        assertEquals(TimeConvertUtil.nowEndMonth(),"2021-10-31");
//    }
}