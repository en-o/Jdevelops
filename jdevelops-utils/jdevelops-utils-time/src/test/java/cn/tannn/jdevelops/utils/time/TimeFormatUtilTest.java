package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static cn.tannn.jdevelops.utils.time.enums.SqlTimeFormat.MYSQL_FORMAT_DATETIME_YEAR;
import static cn.tannn.jdevelops.utils.time.enums.TimeFormat.NORM_FORMAT_DATETIME_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TimeFormatUtilTest {

    @Test
    void str2StrTime() {
        assertEquals(TimeFormatUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.DEFAULT_FORMAT_DATETIME,
                TimeFormatEnum.CN_FORMAT_DATETIME_DAY), "2021年01月09日");
        assertEquals(TimeFormatUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.DEFAULT_FORMAT_DATETIME,
                TimeFormatEnum.EN_FORMAT_DATETIME_SECOND), "2021/01/09 22:12:00");

        assertEquals(TimeFormatUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.CN_FORMAT_DATETIME_DAY), "2021年01月09日");
        assertEquals(TimeFormatUtil.str2StrTime("2021-01-09 22:12:00",
                TimeFormatEnum.EN_FORMAT_DATETIME_SECOND), "2021/01/09 22:12:00");

    }



    @Test
    void defStr2Date() {
        assertEquals("Sat Jan 09 22:12:00 CST 2021", TimeFormatUtil.defStr2Date("2021-01-09 22:12:00").toString());
    }

    @Test
    void str2Date() {
        assertEquals("Sat Jan 09 00:00:00 CST 2021"
                , TimeFormatUtil.str2Date("2021-01-09", TimeFormatEnum.NORM_FORMAT_DATETIME_DAY).toString());
    }

    @Test
    void str2DateTime() {
        assertEquals("2021-01-09T00:00:00.000+08:00"
                , TimeFormatUtil.str2DateTime("2021-01-09", TimeFormatEnum.NORM_FORMAT_DATETIME_DAY).toString());
    }

    @Test
    void date2DefStr() {
        Date date = TimeFormatUtil.str2Date("2021-01-09 10:08:09", TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        assertEquals("2021-01-09 10:08:09", TimeFormatUtil.date2DefStr(date));
    }

    @Test
    void dateTime2DefStr() {
        DateTime dateTime = TimeFormatUtil.str2DateTime("2021-01-09 10:08:09", TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        assertEquals("2021-01-09 10:08:09", TimeFormatUtil.dateTime2DefStr(dateTime));
    }

    @Test
    void date2LocalDate() {
        Date date = TimeFormatUtil.str2Date("2021-01-09 10:08:09", TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        assertEquals("2021-01-09T10:08:09", TimeFormatUtil.date2LocalDate(date).toString());
    }

    @Test
    void LocalDateTimeToDate() {
        java.time.LocalDateTime localDateTime = TimeFormatUtil.str2LocalDateTime("2021-01-09 10:08:09"
                , TimeFormat.DEFAULT_FORMAT_DATETIME);
        assertEquals("Sat Jan 09 10:08:09 CST 2021", TimeFormatUtil.LocalDateTimeToDate(localDateTime).toString());
    }

    @Test
    void joda2JavaLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.parse("2021-01-09 10:08:09"
                , DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));
        assertEquals("2021-01-09T10:08:09", TimeFormatUtil.joda2JavaLocalDateTime(localDateTime).toString());
    }

    @Test
    void str2LocalDateTime() {
        assertEquals("2021-01-09T10:08:09"
                , TimeFormatUtil.str2LocalDateTime("2021-01-09 10:08:09").toString());
        assertEquals("2021-01-09T10:08:09"
                , TimeFormatUtil.str2LocalDateTime("2021/01/09 10:08:09"
                        , TimeFormat.EN_FORMAT_DATETIME_SECOND).toString());
    }

    @Test
    void localDateToDate() {
        LocalDate parse = LocalDate.parse("2021-01-09 10:08:09"
                , DateTimeFormatter.ofPattern(TimeFormat.DEFAULT_FORMAT_DATETIME));
        assertEquals("Sat Jan 09 00:00:00 CST 2021"
                , TimeFormatUtil.localDateToDate(parse).toString());
    }

    @Test
    void timestampToLocalDateTime() {
        Timestamp timestamp = Timestamp.valueOf("2021-01-09 10:08:09");
        assertEquals("2021-01-09T10:08:09"
                , TimeFormatUtil.timestampToLocalDateTime(timestamp).toString());
    }

    @Test
    void dateToLocalDate() {
        Date date = TimeFormatUtil.str2Date("2021-01-09 10:08:09", TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        assertEquals("2021-01-09"
                , TimeFormatUtil.dateToLocalDate(date).toString());
    }

    @Test
    void formatStr() {
        assertEquals("2020-02-02T00:00:00.000+08:00"
                , TimeFormatUtil.formatStr("20200202", TimeFormatEnum.PURE_FORMAT_DATETIME_DAY).toString());
    }

    @Test
    void formatStr2Str() {
        assertEquals("2020-02-02 00:00:00"
                , TimeFormatUtil.formatStr2Str("20200202", TimeFormatEnum.PURE_FORMAT_DATETIME_DAY
                        ,TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
    }

    @Test
    void formatStr2StrByDefault() {
        assertEquals("2020-02-02 00:00:00"
                , TimeFormatUtil.formatStr2StrByDefault("20200202", TimeFormatEnum.PURE_FORMAT_DATETIME_DAY));
    }

    @Test
    void localDateTimeToString() {
        java.time.LocalDateTime localDateTime = TimeFormatUtil.str2LocalDateTime("2021-01-09 10:08:09"
                , TimeFormat.DEFAULT_FORMAT_DATETIME);
        assertEquals("2021-01-09 10:08:09"
                , TimeFormatUtil.localDateTimeToString(localDateTime));
    }



    @Test
    void transferTime() {
        assertEquals(20210109
                , TimeFormatUtil.transferTime("2021-01-09"));
    }

    @Test
    void javaTimeFormats() throws IllegalAccessException {
        assertNotNull(TimeFormatUtil.javaTimeFormats().toString());
    }

    @Test
    void sqlTimeFormats() throws IllegalAccessException {
        assertEquals("[%Y, %Y-%m, %Y-%m-%d, %Y-%m-%d %T, %T, %Y-%u, %u]",TimeFormatUtil.sqlTimeFormats().toString());
    }

    @Test
    void mySqlFormats() throws IllegalAccessException {
        assertEquals("[%Y, %Y-%m, %Y-%m-%d, %Y-%m-%d %T, %T, %Y-%u, %u]",TimeFormatUtil.mySqlFormats().toString());
    }

    @Test
    void mysql2Java() {
        assertEquals(NORM_FORMAT_DATETIME_YEAR, TimeFormatUtil.mysql2Java(MYSQL_FORMAT_DATETIME_YEAR));
    }
}
