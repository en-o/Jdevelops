package com.detabes.time.core;

import com.detabes.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.Date;

/**
 * 时间 验证
 *
 * @author tn
 * @version 1
 * @date 2021-10-25 15:13
 */
public class TimeVerifyUtil {


    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear() {
        DateTime time = new DateTime();
        org.joda.time.DateTime.Property year = time.year();
        return year.isLeap();
    }


    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear(Date date) {
        DateTime time = new DateTime(date);
        org.joda.time.DateTime.Property year = time.year();
        return year.isLeap();
    }

    /**
     * 验证闰年
     * @return true 闰年
     */
    public static boolean verifyLeapYear(int year) {
        Date date = TimeConvertUtil.str2Date(String.valueOf(year), TimeFormatEnum.NORM_FORMAT_DATETIME_YEAR);
        DateTime time = new DateTime(date);
        org.joda.time.DateTime.Property yearleap = time.year();
        return yearleap.isLeap();
    }


}
