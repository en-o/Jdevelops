package com.detabes.time.core;

import com.detabes.constant.time.TimeFormat;
import com.detabes.enums.time.TimeFormatEnum;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class TimeVerifyUtilTest extends TestCase {


    public void testTestVerifyLeapYear() {
        //  (2020是闰年
        Date y2021 = TimeConvertUtil.str2Date("2021", TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR);
        Date y2020 = TimeConvertUtil.str2Date("2020", TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR);
        assertFalse(TimeVerifyUtil.verifyLeapYear(y2021));
        assertTrue(TimeVerifyUtil.verifyLeapYear(y2020));
    }

    public void testTestVerifyLeapYear1() {
        //    (2020是闰年
        assertFalse(TimeVerifyUtil.verifyLeapYear(2021));
        assertTrue(TimeVerifyUtil.verifyLeapYear(2020));
    }
}