package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import cn.tannn.jdevelops.utils.time.enums.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    void thisNow() {
        // 2024/05/27
        String defNow = TimeUtil.thisNow(TimeFormatEnum.EN_FORMAT_DATETIME_DAY);
        System.out.println("defNow: " + defNow);
        assertNotNull(defNow);
    }

    @Test
    void thisDefNow() {
        // 2024-05-27 12:42:13
        String defNow = TimeUtil.thisDefNow();
        System.out.println("defNow: " + defNow);
        assertNotNull(defNow);
    }

    @Test
    void thisMacNow() {
        // 2024/05/27 12:42:13
        String defNow = TimeUtil.thisMacNow();
        System.out.println("thisMacNow: " + defNow);
        assertNotNull(defNow);
    }

    @Test
    void timeDifference() {
        // 小时
        assertEquals(1, TimeUtil.timeDifference("2024-05-27 12:43:58"
                , "2024-05-28 12:43:58",
                0));
        // 小时
        assertEquals(24, TimeUtil.timeDifference("2024-05-27 12:43:58"
                , "2024-05-28 12:43:58",
                1));
        // 分钟
        assertEquals(1440, TimeUtil.timeDifference("2024-05-27 12:43:58"
                , "2024-05-28 12:43:58",
                2));
        // 秒
        assertEquals(86400, TimeUtil.timeDifference("2024-05-27 12:43:58"
                , "2024-05-28 12:43:58",
                3));
        // 毫秒
        assertEquals(86400000, TimeUtil.timeDifference("2024-05-27 12:43:58"
                , "2024-05-28 12:43:58",
                4));
    }

    @Test
    void dateTimeMillis() {
        assertEquals(1716785038000L, TimeUtil.dateTimeMillis("2024-05-27 12:43:58"));
    }

    @Test
    void monthForWeek() throws ParseException {
        Map<Integer, List<String>> forWeek = TimeUtil.monthForWeek("2021-05");
        String week = "{1=[2021-05-01, 2021-05-02], 2=[2021-05-03, 2021-05-04, 2021-05-05, 2021-05-06, 2021-05-07, 2021-05-08, 2021-05-09], 3=[2021-05-10, 2021-05-11, 2021-05-12, 2021-05-13, 2021-05-14, 2021-05-15, 2021-05-16], 4=[2021-05-17, 2021-05-18, 2021-05-19, 2021-05-20, 2021-05-21, 2021-05-22, 2021-05-23], 5=[2021-05-24, 2021-05-25, 2021-05-26, 2021-05-27, 2021-05-28, 2021-05-29, 2021-05-30], 6=[2021-05-31]}";
        assertEquals(forWeek.toString(), week);
    }

    @Test
    void yearFirstTime() {
        assertEquals("2024-01-01 00:00:00"
                , TimeUtil.yearFirstTime(2024, TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
    }

    @Test
    void yearLastTime() {
        assertEquals("2024-12-31 23:59:59"
                , TimeUtil.yearLastTime(2024, TimeFormatEnum.DEFAULT_FORMAT_DATETIME));
    }

    @Test
    void nowBeginAfterTime() {
        String beginTime = TimeUtil.nowBeginAfterTime(4, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        String afterTime = TimeUtil.nowBeginAfterTime(-4, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        System.out.println("AfterTime: " + afterTime+" - BeginTime:"+beginTime);
        assertNotNull(beginTime);
        assertNotNull(afterTime);


        beginTime = TimeUtil.nowBeginAfterTime("2024-10-31 23:59:59", 4, TimeFormatEnum.DEFAULT_FORMAT_DATETIME, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        afterTime = TimeUtil.nowBeginAfterTime("2024-10-31 23:59:59", -4, TimeFormatEnum.DEFAULT_FORMAT_DATETIME, TimeFormatEnum.DEFAULT_FORMAT_DATETIME);
        System.out.println("AfterTime: " + afterTime+" - BeginTime:"+beginTime);
        assertNotNull(beginTime);
        assertNotNull(afterTime);
    }


    @Test
    void resultNumbers() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(TimeFormat.DEFAULT_FORMAT_DATETIME);
        List<Integer> integers = TimeUtil.resultNumbers(DateTime.parse("2021-06-30 17:31:51",dateTimeFormatter).toDate());
//         [年，月，日，周几，12小时制小时，二十四小时制小时，分，秒]
        assertEquals(Arrays.asList(2021, 6, 30, 3, 5, 17, 31, 51),integers);
    }
}
