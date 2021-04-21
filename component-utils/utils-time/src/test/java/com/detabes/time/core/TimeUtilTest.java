package com.detabes.time.core;

import com.detabes.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TimeUtilTest {

    @Test
    public void formatStr() {
        DateTime dateTime = TimeUtil.formatStr("20200202", TimeFormatEnum.PURE_FORMAT_DATETIME_DAY);
        System.out.println();
        assertNotNull(dateTime);
    }

    @Test
    public void time2DefaultFormat() {
        String format = TimeUtil.time2DefaultFormat();
        assertNotNull(format);
        System.out.println(format);
    }

    @Test
    public void testTime2DefaultFormat() {
        String format = TimeUtil.time2DefaultFormat(new Date());
        assertNotNull(format);
        System.out.println(format);
    }
}