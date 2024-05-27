package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeVerifyUtilTest {

    @Test
    void beginGteEnd() {
        assertFalse(TimeVerifyUtil.beginGteEnd(DateTime.now().plusDays(-2).toDate(), new Date()));
        assertTrue(TimeVerifyUtil.beginGteEnd(new Date(), new Date()));
        assertFalse(TimeVerifyUtil.beginGteEnd(new Date(), DateTime.now().plusDays(2).toDate()));
        assertTrue(TimeVerifyUtil.beginGteEnd(new Date(), DateTime.now().plusDays(-2).toDate()));


        assertFalse(TimeVerifyUtil.beginGteEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeVerifyUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeVerifyUtil.beginGteEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }



    @Test
    void beginGtEnd() {
        assertFalse(TimeVerifyUtil.beginGtEnd(DateTime.now().plusDays(-2).toDate(), new Date()));
        assertFalse(TimeVerifyUtil.beginGtEnd(new Date(), new Date()));
        assertFalse(TimeVerifyUtil.beginGtEnd(new Date(), DateTime.now().plusDays(2).toDate()));
        assertTrue(TimeVerifyUtil.beginGtEnd(new Date(), DateTime.now().plusDays(-2).toDate()));


        assertFalse(TimeVerifyUtil.beginGtEnd(DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertTrue(TimeVerifyUtil.beginGtEnd(DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME),
                DateTime.now().plusDays(-2).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.beginGtEnd("2022-11-10 10:05:01",
                "2099-11-10 10:05:01"));
    }



    @Test
    void timeGteNow() {
        assertTrue(TimeVerifyUtil.timeGteNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.timeGteNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }

    @Test
    void timeGtNow() {
        assertTrue(TimeVerifyUtil.timeGtNow(DateTime.now().plusDays(+1).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
        assertFalse(TimeVerifyUtil.timeGtNow(DateTime.now().plusMinutes(-10).toString(TimeFormat.DEFAULT_FORMAT_DATETIME)));
    }

    @Test
    void verifyLeapYear() {
        //    (2020是闰年
        assertFalse(TimeVerifyUtil.verifyLeapYear(2021));
        assertTrue(TimeVerifyUtil.verifyLeapYear(2020));
        System.out.println("now: "+TimeVerifyUtil.verifyLeapYear());
    }

    @Test
    void verifyFormat() {
        assertTrue(TimeVerifyUtil.verifyFormat("2022-12-06 14:55:38", TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
        assertFalse(TimeVerifyUtil.verifyFormat("2022/12/06 14:55:38", TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
    }


}
