package cn.jdevelops.util.time.enums;



/**
 * 时间格式常量
 *
 * @author tn
 * @date 2020/4/9 11:02
 */
public interface TimeFormat {

	/**
	 * 默认格式  yyyy-MM-dd HH:mm:ss  年月日时分秒
	 */
	String DEFAULT_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式  yyyy-MM-dd HH:mm:ss.SSS  年月日时分秒毫秒
	 */
	String FORMAT_MSEC_DATETIME = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 格式  yyyy-MM-dd HH:mm  年月日时分
	 */
	String NORM_FORMAT_DATETIME_MINUTE = "yyyy-MM-dd HH:mm";

	/**
	 * 格式  yyyy-MM-dd HH  年月日时
	 */
	String NORM_FORMAT_DATETIME_HOUR = "yyyy-MM-dd HH";

	/**
	 * 格式  yyyy-MM-dd  年月日
	 */
	String NORM_FORMAT_DATETIME_DAY = "yyyy-MM-dd";

	/**
	 * 格式  yyyy-MM  年月
	 */
	String NORM_FORMAT_DATETIME_MONTH = "yyyy-MM";

	/**
	 * 格式  yyyy 年
	 */
	String NORM_FORMAT_DATETIME_YEAR = "yyyy";

	/**
	 * 格式  HH:mm:ss  时分秒
	 */
	String NORM_FORMAT_TIME_SECOND = "HH:mm:ss";

	/**
	 * 格式  HH:mm  时分
	 */
	String NORM_FORMAT_TIME_MINUTE = "HH:mm";

	/**
	 * 格式  MM-dd HH:mm:ss 月日时分秒
	 */
	String NORM_FORMAT_DATETIME_SIMPLE_SECOND = "MM-dd HH:mm:ss";
	/**
	 * 格式  MM-dd HH:mm 月日时分
	 */
	String NORM_FORMAT_DATETIME_SIMPLE_MINUTE = "MM-dd HH:mm";
	/**
	 * 格式  MM-dd HH 月日时
	 */
	String NORM_FORMAT_DATETIME_SIMPLE_HOUR = "MM-dd HH";
	/**
	 * 格式 MM-dd 月日
	 */
	String NORM_FORMAT_DATETIME_SIMPLE_DAY = "MM-dd";
	/**
	 * 格式  yyyy-MM-dd'T'HH:mm:ss'Z
	 */
	String UTC_FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/**
	 * 年月日时分秒  yyyyMMddHHmmss
	 */
	String PURE_FORMAT_DATETIME_SECOND = "yyyyMMddHHmmss";
	/**
	 * 年月日时分秒毫秒  yyyyMMddHHmmssSSS
	 */
	String PURE_FORMAT_DATETIME_MSEC = "yyyyMMddHHmmssSSS";
	/**
	 * 格式  MMddHHmmss 年月日时分
	 */
	String PURE_FORMAT_DATETIME_MINUTE = "yyyyMMddHHmm";
	/**
	 * 格式  yyyyMMddHH 年月日时
	 */
	String PURE_FORMAT_DATETIME_HOUR = "yyyyMMddHH";
	/**
	 * 格式  yyyyMMdd 年月日
	 */
	String PURE_FORMAT_DATETIME_DAY = "yyyyMMdd";
	/**
	 * 格式  yyyyMM 年月
	 */
	String PURE_FORMAT_DATETIME_MONTH = "yyyyMM";

	/**
	 * 格式  yyyy/MM/dd HH:mm:ss 年月日时分秒
	 */
	String EN_FORMAT_DATETIME_SECOND = "yyyy/MM/dd HH:mm:ss";
	/**
	 * 格式  yyyy/MM/dd HH:mm 年月日时分
	 */
	String EN_FORMAT_DATETIME_MINUTE = "yyyy/MM/dd HH:mm";
	/**
	 * 格式  yyyy/MM/dd HH 年月日时
	 */
	String EN_FORMAT_DATETIME_HOUR = "yyyy/MM/dd HH";
	/**
	 * 格式  yyyy/MM/dd 年月日
	 */
	String EN_FORMAT_DATETIME_DAY = "yyyy/MM/dd";
	/**
	 * 格式  yyyy/MM 年月
	 */
	String EN_FORMAT_DATETIME_MONTH = "yyyy/MM";
	/**
	 * 格式  MM/dd HH:mm:ss 月日时分秒
	 */
	String EN_FORMAT_DATETIME_SIMPLE_SECOND = "MM/dd HH:mm:ss";
	/**
	 * 格式  MM/dd HH:mm 月日时分
	 */
	String EN_FORMAT_DATETIME_SIMPLE_MINUTE = "MM/dd HH:mm";

	/**
	 * 格式  MM/dd HH:mm 月日时
	 */
	String EN_FORMAT_DATETIME_SIMPLE_HOUR = "MM/dd HH:mm";

	/**
	 * 格式  MM/dd 月日
	 */
	String EN_FORMAT_DATETIME_SIMPLE_DAY = "MM/dd";

	/**
	 * 格式  yyyy年MM月dd日 HH:mm:ss 年月日时分秒
	 */
	String CN_FORMAT_DATETIME_SECOND = "yyyy年MM月dd日 HH:mm:ss";
	/**
	 * 格式  yyyy年MM月dd日 HH:mm 年月日时分
	 */
	String CN_FORMAT_DATETIME_MINUTE = "yyyy年MM月dd日 HH:mm";
	/**
	 * 格式  yyyy年MM月dd日 年月日
	 */
	String CN_FORMAT_DATETIME_DAY = "yyyy年MM月dd日";
	/**
	 * 格式  yyyy年MM月 年月
	 */
	String CN_FORMAT_DATETIME_MONTH = "yyyy年MM月";

	/**
	 * 格式  MM月dd日 月日
	 */
	String CN_FORMAT_DATETIME_SIMPLE_DAY = "MM月dd日";
	/**
	 * 格式  MM月dd日 HH:mm 月日时分
	 */
	String CN_FORMAT_DATETIME_SIMPLE_MINUTE = "MM月dd日 HH:mm";
	/**
	 * 格式  MM月dd日 HH:mm:ss 月日时分秒
	 */
	String CN_FORMAT_DATETIME_SIMPLE_SECOND = "MM月dd日 HH:mm:ss";

	/**
	 * 格式  yyyy.MM.dd 年月日
	 */
	String SPECIAL_FORMAT_DATETIME_DAY = "yyyy.MM.dd";
	/**
	 * 格式  MM.dd 月日
	 */
	String SPECIAL_FORMAT_DATETIME_SIMPLE_DAY = "MM.dd";

	/**
	 * 格式  公元 2019-01-01 +0800 星期二 10:39:06:863 下午
	 */
	String AD_FORMAT_DATETIME_SECOND = "G y-MM-dd Z E HH:mm:ss:SSS a";
	/**
	 * 格式  EEE, dd MMM yyyy HH:mm:ss z
	 */
	String HTTP_FORMAT_DATETIME_SECOND = "EEE, dd MMM yyyy HH:mm:ss z";
	/**
	 * 格式  EEE MMM dd HH:mm:ss zzz yyyy
	 */
	String JDK_FORMAT_DATETIME = "EEE MMM dd HH:mm:ss zzz yyyy";


}
