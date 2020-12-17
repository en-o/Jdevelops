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
	 *
	 * @param format 格式
	 * @return {String}
	 */
	public static String margeMysqlFormat(String format) {
		switch (format) {
			case TimeFormat.MYSQL_FORMAT_DATETIME_SECOND:
				format = TimeFormat.DEFAULT_FORMAT_DATETIME;
				break;
			case TimeFormat.MYSQL_FORMAT_DATETIME_DAY:
				format = TimeFormat.NORM_FORMAT_DATETIME_DAY;
				break;
			case TimeFormat.MYSQL_FORMAT_DATETIME_MONTH:
				format = TimeFormat.NORM_FORMAT_DATETIME_MONTH;
				break;
			case TimeFormat.MYSQL_FORMAT_DATETIME_YEAR:
				format = TimeFormat.NORM_FORMAT_DATETIME_YEAR;
				break;
			case TimeFormat.MYSQL_FORMAT_DATETIME_SIMPLE_DAY:
				format = TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_SECOND;
				break;
			default:
				break;
		}
		return format;
	}
}
