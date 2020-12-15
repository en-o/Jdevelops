package com.detabes.time.core;


import com.detabes.constant.time.TimeFormat;

/**
 * @author tn
 * @version 1
 * @ClassName MysqlTime
 * @description
 * @date 2020/12/15 11:24
 */
public class MysqlTime {
    /**
     * 将mysql的时间格式转换成java的
     * @param format 格式
     * @return {String}
     */
    public static String margeMysqlFormat(String format){
        switch (format){
            case TimeFormat.MYSQL_DEFAULT_FORMAT:
                format = TimeFormat.DEFAULT_FORMAT;
                break;
            case TimeFormat.MYSQL_NORM_DATE_FORMAT:
                format = TimeFormat.NORM_DATE_FORMAT;
                break;
            case TimeFormat.MYSQL_SIMPLE_DATE_MONTH_FORMAT:
                format = TimeFormat.SIMPLE_DATE_MONTH_FORMAT;
                break;
            case TimeFormat.MYSQL_YEAR_DATE_FORMAT:
                format = TimeFormat.YEAR_DATE_FORMAT;
                break;
            case TimeFormat.MYSQL_HHMMSS_FORMAT:
                format = TimeFormat.SIMPLE_DATETIME_SECOND_FORMAT;
                break;
            default:
                break;
        }
        return format;
    }
}
