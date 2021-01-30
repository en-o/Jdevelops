package com.detabes.enums.time;


import lombok.AllArgsConstructor;
import lombok.Getter;

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
	DEFAULT_FORMAT_DATETIME("yyyy-MM-dd HH:mm:ss", "年月日时分秒"),


	/**
	 * 格式  yyyy-MM-dd HH:mm:ss.SSS  年月日时分秒毫秒
	 */
	FORMAT_MSEC_DATETIME("yyyy-MM-dd HH:mm:ss.SSS","年月日时分秒毫秒"),

	/**
	 * 格式  yyyy-MM-dd HH:mm
	 */
	NORM_FORMAT_DATETIME_MINUTE("yyyy-MM-dd HH:mm", "年月日时分"),

	/**
	 * 格式  yyyy-MM-dd HH
	 */
	NORM_FORMAT_DATETIME_HOUR("yyyy-MM-dd HH", "年月日时"),

	/**
	 * 格式  yyyy-MM-dd
	 */
	NORM_FORMAT_DATETIME_DAY("yyyy-MM-dd", "年月日"),

	/**
	 * 格式  yyyy-MM
	 */
	NORM_FORMAT_DATETIME_MONTH("yyyy-MM", "年月"),

	/**
	 * 格式  yyyy
	 */
	NORM_FORMAT_DATETIME_YEAR("yyyy", "年"),

	/**
	 * 格式  HH:mm:ss
	 */
	NORM_FORMAT_TIME_SECOND("HH:mm:ss", "时分秒"),

	/**
	 * 格式  HH:mm
	 */
	NORM_FORMAT_TIME_MINUTE("HH:mm", "时分"),

	/**
	 * 格式  MM-dd HH:mm:ss
	 */
	NORM_FORMAT_DATETIME_SIMPLE_SECOND("MM-dd HH:mm:ss", "月日时分秒"),
	/**
	 * 格式  MM-dd HH:mm
	 */
	NORM_FORMAT_DATETIME_SIMPLE_MINUTE("MM-dd HH:mm", "月日时分"),
	/**
	 * 格式  MM-dd HH
	 */
	NORM_FORMAT_DATETIME_SIMPLE_HOUR("MM-dd HH", "月日时"),
	/**
	 * 格式  MM-dd
	 */
	NORM_FORMAT_DATETIME_SIMPLE_DAY("MM-dd", "月日"),
	/**
	 * 格式  yyyy-MM-dd'T'HH:mm:ss'Z
	 */
	UTC_FORMAT_DATETIME("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss'Z"),
	/**
	 * 格式  yyyyMMddHHmmss
	 */
	PURE_FORMAT_DATETIME_SECOND("yyyyMMddHHmmss", "年月日时分秒"),
	/**
	 * 格式  MMddHHmmss
	 */
	PURE_FORMAT_DATETIME_MINUTE("yyyyMMddHHmm", "年月日时分"),
	/**
	 * 格式  yyyyMMddHH
	 */
	PURE_FORMAT_DATETIME_HOUR("yyyyMMddHH", "年月日时"),
	/**
	 * 格式  yyyyMMdd
	 */
	PURE_FORMAT_DATETIME_DAY("yyyyMMdd", "年月日"),
	/**
	 * 格式  yyyyMM
	 */
	PURE_FORMAT_DATETIME_MONTH("yyyyMM", "年月"),

	/**
	 * 格式  yyyy/MM/dd HH:mm:ss
	 */
	EN_FORMAT_DATETIME_SECOND("yyyy/MM/dd HH:mm:ss", "年月日时分秒"),
	/**
	 * 格式  yyyy/MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_MINUTE("yyyy/MM/dd HH:mm", "年月日时分"),
	/**
	 * 格式  yyyy/MM/dd HH
	 */
	EN_FORMAT_DATETIME_HOUR("yyyy/MM/dd HH", "年月日时"),
	/**
	 * 格式  yyyy/MM/dd
	 */
	EN_FORMAT_DATETIME_DAY("yyyy/MM/dd", "年月日"),
	/**
	 * 格式  yyyy/MM
	 */
	EN_FORMAT_DATETIME_MONTH("yyyy/MM", "年月"),
	/**
	 * 格式  MM/dd HH:mm:ss
	 */
	EN_FORMAT_DATETIME_SIMPLE_SECOND("MM/dd HH:mm:ss", "月日时分秒"),
	/**
	 * 格式  MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_SIMPLE_MINUTE("MM/dd HH:mm", "月日时分"),

	/**
	 * 格式  MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_SIMPLE_HOUR("MM/dd HH:mm", "月日时"),

	/**
	 * 格式  MM/dd
	 */
	EN_FORMAT_DATETIME_SIMPLE_DAY("MM/dd", "月日"),

	/**
	 * 格式  yyyy年MM月dd日 HH:mm:ss
	 */
	CN_FORMAT_DATETIME_SECOND("yyyy年MM月dd日 HH:mm:ss", "年月日时分秒"),
	/**
	 * 格式  yyyy年MM月dd日 HH:mm
	 */
	CN_FORMAT_DATETIME_MINUTE("yyyy年MM月dd日 HH:mm", "年月日时分"),
	/**
	 * 格式  yyyy年MM月dd日
	 */
	CN_FORMAT_DATETIME_DAY("yyyy年MM月dd日", "年月日"),
	/**
	 * 格式  yyyy年MM月
	 */
	CN_FORMAT_DATETIME_MONTH("yyyy年MM月", "年月"),

	/**
	 * 格式  MM月dd日
	 */
	CN_FORMAT_DATETIME_SIMPLE_DAY("MM月dd日", "月日"),
	/**
	 * 格式  MM月dd日 HH:mm
	 */
	CN_FORMAT_DATETIME_SIMPLE_MINUTE("MM月dd日 HH:mm", "月日时分"),
	/**
	 * 格式  MM月dd日 HH:mm:ss
	 */
	CN_FORMAT_DATETIME_SIMPLE_SECOND("MM月dd日 HH:mm:ss", "月日时分秒"),

	/**
	 * 格式  yyyy.MM.dd
	 */
	SPECIAL_FORMAT_DATETIME_DAY("yyyy.MM.dd", "年月日"),
	/**
	 * 格式  MM.dd
	 */
	SPECIAL_FORMAT_DATETIME_SIMPLE_DAY("MM.dd", "月日"),

	/**
	 * 格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午
	 */
	AD_FORMAT_DATETIME_SECOND("G y-MM-dd Z E HH:mm:ss:SSS a", "公元 2019-01-01 +0800 星期二 10:39:06:863 下午 "),
	/**
	 * 格式  EEE, dd MMM yyyy HH:mm:ss z
	 */
	HTTP_FORMAT_DATETIME_SECOND("EEE, dd MMM yyyy HH:mm:ss z", "EEE, dd MMM yyyy HH:mm:ss z"),
	/**
	 * 格式  EEE MMM dd HH:mm:ss zzz yyyy
	 */
	JDK_FORMAT_DATETIME("EEE MMM dd HH:mm:ss zzz yyyy", "EEE MMM dd HH:mm:ss zzz yyyy"),

	/**
	 * 格式  mysql %Y
	 */
	MYSQL_FORMAT_DATETIME_YEAR("%Y", "年"),
	/**
	 * 格式  mysql %Y-%m
	 */
	MYSQL_FORMAT_DATETIME_MONTH("%Y-%m", "年月"),
	/**
	 * 格式  mysql %Y-%m-%d
	 */
	MYSQL_FORMAT_DATETIME_DAY("%Y-%m-%d", "年月日"),
	/**
	 * 格式  mysql %Y-%m-%d %T
	 */
	MYSQL_FORMAT_DATETIME_SECOND("%Y-%m-%d %T", "年月日时分秒"),
	/**
	 * 格式  mysql %T
	 */
	MYSQL_FORMAT_DATETIME_SIMPLE_DAY("%T", "时分秒"),
	;

	private String format;
	private String remark;
}
