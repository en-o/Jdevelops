package com.detabes.enums.time;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间格式常量
 *
 * @author tn
 * @date 2020/4/9 11:02
 */
@Getter
@AllArgsConstructor
public enum TimeFormatEnum {

	/**
	 * 默认格式  yyyy-MM-dd HH:mm:ss
	 */
	DEFAULT_FORMAT("yyyy-MM-dd HH:mm:ss", "默认格式  yyyy-MM-dd HH:mm:ss"),

	/**
	 * 格式  yyyy-MM-dd
	 */
	NORM_DATE_FORMAT("yyyy-MM-dd", "格式  yyyy-MM-dd"),

	/**
	 * 格式  yyyyMMdd
	 */
	PURE_DATE_FORMAT("yyyyMMdd", "格式  yyyyMMdd"),

	/**
	 * 格式  yyyyMM
	 */
	YM_DATE_FORMAT("yyyyMM", "格式  yyyyMM"),
	/**
	 * 格式  yyyy-MM-dd HH:mm
	 */
	NORM_DATETIME_MINUTE_FORMAT("yyyy-MM-dd HH:mm", "格式  yyyy-MM-dd HH:mm"),
	/**
	 * 格式  yyyyMMddHHmmss
	 */
	PURE_DATETIME_FORMAT("yyyyMMddHHmmss", "格式  yyyyMMddHHmmss"),
	/**
	 * 格式  MMddHHmmss
	 */
	NOYEAR_DATETIME_FORMAT("MMddHHmmss", "格式  MMddHHmmss"),
	/**
	 * 格式  yyyyMMddHH
	 */
	YMDH_DATE_FORMAT("yyyyMMddHH", "格式  yyyyMMddHH"),
	/**
	 * 格式  yyyy
	 */
	YEAR_DATE_FORMAT("yyyy", "格式  yyyy"),

	/**
	 * 格式  yyyy/MM/dd
	 */
	EN_DATE_FORMAT("yyyy/MM/dd", "格式  yyyy/MM/dd"),
	/**
	 * 格式  yyyy/MM
	 */
	EN_DATE_MONTH_FORMAT("yyyy/MM", "格式  yyyy/MM "),
	/**
	 * 格式  yyyy/MM/dd HH:mm
	 */
	EN_DATETIME_MINUTE_FORMAT("yyyy/MM/dd HH:mm", "格式  yyyy/MM/dd HH:mm"),
	/**
	 * 格式  yyyy/MM/dd HH:mm:ss
	 */
	EN_DATETIME_FORMAT("yyyy/MM/dd HH:mm:ss", "格式  yyyy/MM/dd HH:mm:ss"),
	/**
	 * 格式  yyyy年MM月dd日
	 */
	CN_DATE_FORMAT("yyyy年MM月dd日", "格式  yyyy年MM月dd日"),
	/**
	 * 格式  yyyy年MM月
	 */
	CN_DATE_MONTH_FORMAT("yyyy年MM月", "格式  yyyy年MM月"),
	/**
	 * 格式  yyyy年MM月dd日 HH:mm
	 */
	CN_DATETIME_MINUTE_FORMAT("yyyy年MM月dd日 HH:mm", "格式  yyyy年MM月dd日 HH:mm"),
	/**
	 * 格式  yyyy年MM月dd日 HH:mm:ss
	 */
	CN_DATETIME_FORMAT("yyyy年MM月dd日 HH:mm:ss", "格式  yyyy年MM月dd日 HH:mm:ss"),

	/**
	 * 格式  yyyy-MM
	 */
	SIMPLE_DATE_MONTH_FORMAT("yyyy-MM", "格式  yyyy-MM"),
	/**
	 * 格式  MM-dd
	 */
	SIMPLE_DATE_DAY_FORMAT("MM-dd", "格式  MM-dd"),
	/**
	 * 格式  MM-dd HH:mm
	 */
	SIMPLE_DATETIME_MINUTE_FORMAT("MM-dd HH:mm", "格式  MM-dd HH:mm "),
	/**
	 * 格式  MM-dd HH:mm:ss
	 */
	SIMPLE_DATETIME_SECOND_FORMAT("MM-dd HH:mm:ss", "格式  MM-dd HH:mm:ss"),

	/**
	 * 格式  HH:mm
	 */
	SIMPLE_TIME_MINUTE_FORMAT("HH:mm", "格式  HH:mm"),
	/**
	 * 格式  HH:mm:ss
	 */
	NORM_TIME_FORMAT("HH:mm:ss", "格式  HH:mm:ss"),
	/**
	 * 格式  MM/dd
	 */
	EN_SIMPLE_DATE_DAY_FORMAT("MM/dd", "格式  MM/dd"),
	/**
	 * 格式  MM/dd HH:mm
	 */
	EN_SIMPLE_DATETIME_MINUTE_FORMAT("MM/dd HH:mm", "格式  MM/dd HH:mm"),
	/**
	 * 格式  MM/dd HH:mm:ss
	 */
	EN_SIMPLE_DATETIME_SECOND_FORMAT("MM/dd HH:mm:ss", "格式  MM/dd HH:mm:ss"),


	/**
	 * 格式  MM月dd日
	 */
	CN_SIMPLE_DATE_DAY_FORMAT("MM月dd日", "格式  MM月dd日"),
	/**
	 * 格式  MM月dd日 HH:mm
	 */
	CN_SIMPLE_DATETIME_MINUTE_FORMAT("MM月dd日 HH:mm", "格式  MM月dd日 HH:mm"),
	/**
	 * 格式  MM月dd日 HH:mm:ss
	 */
	CN_SIMPLE_DATETIME_SECOND_FORMAT("MM月dd日 HH:mm:ss", "格式  MM月dd日 HH:mm:ss"),
	/**
	 * 格式  yyyy.MM.dd
	 */
	SPECIAL_SIMPLE_DATE_FORMAT("yyyy.MM.dd", "格式  yyyy.MM.dd"),
	/**
	 * 格式  MM.dd
	 */
	SPECIAL_SIMPLE_DATE_DAY_FORMAT("MM.dd", "格式  MM.dd"),

	/**
	 * 格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午
	 */
	AD_DATETIME_FORMAT("G y-MM-dd Z E HH:mm:ss:SSS a", "格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午 "),
	/**
	 * 格式  EEE, dd MMM yyyy HH:mm:ss z
	 */
	HTTP_DATETIME_FORMAT("EEE, dd MMM yyyy HH:mm:ss z", "格式  EEE, dd MMM yyyy HH:mm:ss z"),
	/**
	 * 格式  EEE MMM dd HH:mm:ss zzz yyyy
	 */
	JDK_DATETIME_FORMAT("EEE MMM dd HH:mm:ss zzz yyyy", "格式  EEE MMM dd HH:mm:ss zzz yyyy"),
	/**
	 * 格式  yyyy-MM-dd'T'HH:mm:ss'Z
	 */
	UTC_FORMAT("yyyy-MM-dd'T'HH:mm:ss'Z'", "格式  yyyy-MM-dd'T'HH:mm:ss'Z"),


	/**
	 * 格式  mysql %Y
	 */
	MYSQL_YEAR_DATE_FORMAT("%Y", "格式  mysql %Y"),
	/**
	 * 格式  mysql %Y-%m
	 */
	MYSQL_SIMPLE_DATE_MONTH_FORMAT("%Y-%m", "格式  mysql %Y-%m"),
	/**
	 * 格式  mysql %Y-%m-%d
	 */
	MYSQL_NORM_DATE_FORMAT("%Y-%m-%d", "格式  mysql %Y-%m-%d"),
	/**
	 * 格式  mysql %Y-%m-%d %T
	 */
	MYSQL_DEFAULT_FORMAT("%Y-%m-%d %T", "格式  mysql %Y-%m-%d %T"),
	/**
	 * 格式  mysql %T
	 */
	MYSQL_HHMMSS_FORMAT("%T", "格式  mysql %T"),
	;

	private String format;
	private String remark;
}
