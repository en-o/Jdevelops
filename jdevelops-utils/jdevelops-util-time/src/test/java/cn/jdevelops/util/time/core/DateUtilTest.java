package cn.jdevelops.util.time.core;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase {

    public void testToday() {
        // 2023-07-20
        System.out.println(DateUtil.today());
    }

    public void testYesterday() {
        // 2023-07-19T13:41:29.357+08:00
        System.out.println(DateUtil.yesterday());
    }

    public void testTomorrow() {
        // 2023-07-21T13:41:58.836+08:00
        System.out.println(DateUtil.tomorrow());
    }

    public void testLastWeek() {
        // 2023-07-13T13:42:27.262+08:00
        System.out.println(DateUtil.lastWeek());
    }

    public void testNextWeek() {
        // 2023-07-27T13:42:59.154+08:00
        System.out.println(DateUtil.nextWeek());
    }

    public void testLastMonth() {
        // 2023-06-20T13:43:33.780+08:00
        System.out.println(DateUtil.lastMonth());
    }

    public void testNextMonth() {
        // 2023-08-20T13:44:02.819+08:00
        System.out.println(DateUtil.nextMonth());
    }

    public void testLastYear() {
        // 2022-07-20T13:44:15.782+08:00
        System.out.println(DateUtil.lastYear());
    }

    public void testNextYears() {
        // 2024-07-20T13:44:37.265+08:00
        System.out.println(DateUtil.nextYears());
        System.out.println(DateUtil.nextYears());
    }
}
