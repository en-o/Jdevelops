package com.detabes.time.core;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.Date;

/**
 * 时间比较
 *
 * @author tn
 * @version 1
 * @date 2021-10-25 15:13
 */
public class TimeContrastUtil {

    /**
     * 验证 开始时间 大于等于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于(等于)  ， false 小于
     */
    public static boolean beginGteEnd(Date begin, Date end) {
        DateTime beginTime = new DateTime(begin);
        DateTime endTime = new DateTime(end);
        int seconds = Seconds.secondsBetween(beginTime, endTime).getSeconds();
        if (seconds <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 验证 开始时间 大于 结束时间
     * @param begin 开始
     * @param end   结束
     * @return true 大于 ， false 小于（等于）
     */
    public static boolean beginGtEnd(Date begin, Date end) {
        DateTime beginTime = new DateTime(begin);
        DateTime endTime = new DateTime(end);
        int seconds = Seconds.secondsBetween(beginTime, endTime).getSeconds();
        if (seconds < 0) {
            return true;
        }
        return false;
    }
}
