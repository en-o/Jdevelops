package cn.jdevelops.util.time.core;

import cn.jdevelops.util.time.enums.TimeFormatEnum;
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

    public void testThisNow() {
        assertNotNull(TimeConvertUtil.thisNow(TimeFormatEnum.EN_FORMAT_DATETIME_DAY));
    }

    public void testNowMonday() {
        assertNotNull(TimeConvertUtil.nowMonday());
    }

    public void testNowWeekend() {
        assertNotNull(TimeConvertUtil.nowWeekend());
    }

    public void testNowBeginMonth() {
        assertNotNull(TimeConvertUtil.nowBeginMonth());
    }

    public void testNowEndMonth() {
        assertNotNull(TimeConvertUtil.nowEndMonth());
    }


    public void testVerifyFormat() {
        assertTrue(TimeConvertUtil.verifyFormat("2022-12-06 14:55:38", TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
        assertFalse(TimeConvertUtil.verifyFormat("2022/12/06 14:55:38", TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
    }
}
