package cn.jdevelops.util.time.enums;



/**
 * 时间格式常量
 *
 * @author tn
 * @date 2020/4/9 11:02
 */

public enum TimeFormatEnum {

	/**
	 * 默认格式  yyyy-MM-dd HH:mm:ss
	 */
	DEFAULT_FORMAT_DATETIME(TimeFormat.DEFAULT_FORMAT_DATETIME, "年月日时分秒"),


	/**
	 * 格式  yyyy-MM-dd HH:mm:ss.SSS  年月日时分秒毫秒
	 */
	FORMAT_MSEC_DATETIME(TimeFormat.FORMAT_MSEC_DATETIME,"年月日时分秒毫秒"),

	/**
	 * 格式  yyyy-MM-dd HH:mm
	 */
	NORM_FORMAT_DATETIME_MINUTE(TimeFormat.NORM_FORMAT_DATETIME_MINUTE, "年月日时分"),

	/**
	 * 格式  yyyy-MM-dd HH
	 */
	NORM_FORMAT_DATETIME_HOUR(TimeFormat.NORM_FORMAT_DATETIME_HOUR, "年月日时"),

	/**
	 * 格式  yyyy-MM-dd
	 */
	NORM_FORMAT_DATETIME_DAY(TimeFormat.NORM_FORMAT_DATETIME_DAY, "年月日"),

	/**
	 * 格式  yyyy-MM
	 */
	NORM_FORMAT_DATETIME_MONTH(TimeFormat.NORM_FORMAT_DATETIME_MONTH, "年月"),

	/**
	 * 格式  yyyy
	 */
	NORM_FORMAT_DATETIME_YEAR(TimeFormat.NORM_FORMAT_DATETIME_YEAR, "年"),

	/**
	 * 格式  HH:mm:ss
	 */
	NORM_FORMAT_TIME_SECOND(TimeFormat.NORM_FORMAT_TIME_SECOND, "时分秒"),

	/**
	 * 格式  HH:mm
	 */
	NORM_FORMAT_TIME_MINUTE(TimeFormat.NORM_FORMAT_TIME_MINUTE, "时分"),

	/**
	 * 格式  MM-dd HH:mm:ss
	 */
	NORM_FORMAT_DATETIME_SIMPLE_SECOND(TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_SECOND, "月日时分秒"),
	/**
	 * 格式  MM-dd HH:mm
	 */
	NORM_FORMAT_DATETIME_SIMPLE_MINUTE(TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_MINUTE, "月日时分"),
	/**
	 * 格式  MM-dd HH
	 */
	NORM_FORMAT_DATETIME_SIMPLE_HOUR(TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_HOUR, "月日时"),
	/**
	 * 格式  MM-dd
	 */
	NORM_FORMAT_DATETIME_SIMPLE_DAY(TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_DAY, "月日"),
	/**
	 * 格式  yyyy-MM-dd'T'HH:mm:ss'Z
	 */
	UTC_FORMAT_DATETIME(TimeFormat.UTC_FORMAT_DATETIME, "yyyy-MM-dd'T'HH:mm:ss'Z"),
	/**
	 * 格式  yyyyMMddHHmmss
	 */
	PURE_FORMAT_DATETIME_SECOND(TimeFormat.PURE_FORMAT_DATETIME_SECOND, "年月日时分秒"),
	/**
	 * 格式  yyyyMMddHHmmssSSS
	 */
	PURE_FORMAT_DATETIME_MSEC(TimeFormat.PURE_FORMAT_DATETIME_MSEC, "年月日时分秒毫秒"),
	/**
	 * 格式  MMddHHmmss
	 */
	PURE_FORMAT_DATETIME_MINUTE(TimeFormat.PURE_FORMAT_DATETIME_MINUTE, "年月日时分"),
	/**
	 * 格式  yyyyMMddHH
	 */
	PURE_FORMAT_DATETIME_HOUR(TimeFormat.PURE_FORMAT_DATETIME_HOUR, "年月日时"),
	/**
	 * 格式  yyyyMMdd
	 */
	PURE_FORMAT_DATETIME_DAY(TimeFormat.PURE_FORMAT_DATETIME_DAY, "年月日"),
	/**
	 * 格式  yyyyMM
	 */
	PURE_FORMAT_DATETIME_MONTH(TimeFormat.PURE_FORMAT_DATETIME_MONTH, "年月"),

	/**
	 * 格式  yyyy/MM/dd HH:mm:ss
	 */
	EN_FORMAT_DATETIME_SECOND(TimeFormat.EN_FORMAT_DATETIME_SECOND, "年月日时分秒"),
	/**
	 * 格式  yyyy/MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_MINUTE(TimeFormat.EN_FORMAT_DATETIME_MINUTE, "年月日时分"),
	/**
	 * 格式  yyyy/MM/dd HH
	 */
	EN_FORMAT_DATETIME_HOUR(TimeFormat.EN_FORMAT_DATETIME_HOUR, "年月日时"),
	/**
	 * 格式  yyyy/MM/dd
	 */
	EN_FORMAT_DATETIME_DAY(TimeFormat.EN_FORMAT_DATETIME_DAY, "年月日"),
	/**
	 * 格式  yyyy/MM
	 */
	EN_FORMAT_DATETIME_MONTH(TimeFormat.EN_FORMAT_DATETIME_MONTH, "年月"),
	/**
	 * 格式  MM/dd HH:mm:ss
	 */
	EN_FORMAT_DATETIME_SIMPLE_SECOND(TimeFormat.EN_FORMAT_DATETIME_SIMPLE_SECOND, "月日时分秒"),
	/**
	 * 格式  MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_SIMPLE_MINUTE(TimeFormat.EN_FORMAT_DATETIME_SIMPLE_MINUTE, "月日时分"),

	/**
	 * 格式  MM/dd HH:mm
	 */
	EN_FORMAT_DATETIME_SIMPLE_HOUR(TimeFormat.EN_FORMAT_DATETIME_SIMPLE_HOUR, "月日时"),

	/**
	 * 格式  MM/dd
	 */
	EN_FORMAT_DATETIME_SIMPLE_DAY(TimeFormat.EN_FORMAT_DATETIME_SIMPLE_DAY, "月日"),

	/**
	 * 格式  yyyy年MM月dd日 HH:mm:ss
	 */
	CN_FORMAT_DATETIME_SECOND(TimeFormat.CN_FORMAT_DATETIME_SECOND, "年月日时分秒"),
	/**
	 * 格式  yyyy年MM月dd日 HH:mm
	 */
	CN_FORMAT_DATETIME_MINUTE(TimeFormat.CN_FORMAT_DATETIME_MINUTE, "年月日时分"),
	/**
	 * 格式  yyyy年MM月dd日
	 */
	CN_FORMAT_DATETIME_DAY(TimeFormat.CN_FORMAT_DATETIME_DAY, "年月日"),
	/**
	 * 格式  yyyy年MM月
	 */
	CN_FORMAT_DATETIME_MONTH(TimeFormat.CN_FORMAT_DATETIME_MONTH, "年月"),

	/**
	 * 格式  MM月dd日
	 */
	CN_FORMAT_DATETIME_SIMPLE_DAY(TimeFormat.CN_FORMAT_DATETIME_SIMPLE_DAY, "月日"),
	/**
	 * 格式  MM月dd日 HH:mm
	 */
	CN_FORMAT_DATETIME_SIMPLE_MINUTE(TimeFormat.CN_FORMAT_DATETIME_SIMPLE_MINUTE, "月日时分"),
	/**
	 * 格式  MM月dd日 HH:mm:ss
	 */
	CN_FORMAT_DATETIME_SIMPLE_SECOND(TimeFormat.CN_FORMAT_DATETIME_SIMPLE_SECOND, "月日时分秒"),

	/**
	 * 格式  yyyy.MM.dd
	 */
	SPECIAL_FORMAT_DATETIME_DAY(TimeFormat.SPECIAL_FORMAT_DATETIME_DAY, "年月日"),
	/**
	 * 格式  MM.dd
	 */
	SPECIAL_FORMAT_DATETIME_SIMPLE_DAY(TimeFormat.SPECIAL_FORMAT_DATETIME_SIMPLE_DAY, "月日"),

	/**
	 * 格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午
	 */
	AD_FORMAT_DATETIME_SECOND(TimeFormat.AD_FORMAT_DATETIME_SECOND, "公元 2019-01-01 +0800 星期二 10:39:06:863 下午 "),
	/**
	 * 格式  EEE, dd MMM yyyy HH:mm:ss z
	 */
	HTTP_FORMAT_DATETIME_SECOND(TimeFormat.HTTP_FORMAT_DATETIME_SECOND, "EEE, dd MMM yyyy HH:mm:ss z"),
	/**
	 * 格式  EEE MMM dd HH:mm:ss zzz yyyy
	 */
	JDK_FORMAT_DATETIME(TimeFormat.JDK_FORMAT_DATETIME, "EEE MMM dd HH:mm:ss zzz yyyy"),

	;

	private final String format;
	private final String remark;

	TimeFormatEnum(String format, String remark) {
		this.format = format;
		this.remark = remark;
	}

	public String getFormat() {
		return format;
	}

	public String getRemark() {
		return remark;
	}
}
