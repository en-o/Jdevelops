package cn.tannn.jdevelops.utils.time;

import cn.tannn.jdevelops.utils.time.enums.TimeFormat;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {

    @Test
    void today() {
        //  2024-05-26
        assertEquals(DateTime.now().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY),DateUtil.today());
    }

    @Test
    void yesterday() {
        // 2024-05-26T09:04:51.404+08:00
        System.out.println(DateUtil.yesterday());
    }

    @Test
    void tomorrow() {
        // 2024-05-28T09:05:37.500+08:00
        System.out.println(DateUtil.tomorrow());
    }

    @Test
    void lastWeek() {
        // 2024-05-20T09:06:20.629+08:00
        System.out.println(DateUtil.lastWeek());
    }

    @Test
    void nextWeek() {
        // 2024-06-03T09:06:42.137+08:00
        System.out.println(DateUtil.nextWeek());
    }

    @Test
    void lastMonth() {
        // 2024-04-27T09:07:04.352+08:00
        System.out.println(DateUtil.lastMonth());
    }

    @Test
    void nextMonth() {
        // 2024-06-27T09:07:50.948+08:00
        System.out.println(DateUtil.nextMonth());
    }

    @Test
    void lastYear() {
        // 2023-05-27T09:08:21.277+08:00
        System.out.println(DateUtil.lastYear());
    }

    @Test
    void nextYears() {
        // 2025-05-27T09:08:34.726+08:00
        System.out.println(DateUtil.nextYears());
    }

    @Test
    void nowWeekend() {
        // 2024-06-02
        System.out.println(DateUtil.nowWeekend());
    }

    @Test
    void nowBeginMonth() {
        // 2024-05-01
        System.out.println(DateUtil.nowBeginMonth());
    }

    @Test
    void nowEndMonth() {
        // 2024-05-31
        System.out.println(DateUtil.nowEndMonth());
    }

    @Test
    void nowMonday() {
        // 2024-05-27
        System.out.println(DateUtil.nowMonday());
    }

    @Test
    void endMonth() {
        // 2024-05-31
        System.out.println(DateUtil.endMonth("2024-05"));
    }

    @Test
    void findDates() {
        List<String> list = new ArrayList<>(Collections.singleton(DateTime.now().toString(TimeFormat.NORM_FORMAT_DATETIME_DAY)));
        for (int i = 1; i < 4; i++) {
            list.add(DateTime.now().plusDays(i).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY));
        }
        List<String> dates = DateUtil.findDates(DateTime.now().toDate(), DateTime.now().plusDays(3).toDate());
        // findDates:[2024-05-27, 2024-05-28, 2024-05-29, 2024-05-30]
        //      list:[2024-05-27, 2024-05-28, 2024-05-29, 2024-05-30]
        System.out.println("findDates:"+dates);;
        System.out.println("list:"+list);;
        assertEquals(dates, list);
    }
}
