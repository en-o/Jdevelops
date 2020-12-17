package com.detabes.constant.time;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * 格式  mysql %Y  年
	 */
	String MYSQL_FORMAT_DATETIME_YEAR = "%Y";
	/**
	 * 格式  mysql %Y-%m  年月
	 */
	String MYSQL_FORMAT_DATETIME_MONTH = "%Y-%m";
	/**
	 * 格式  mysql %Y-%m-%d 年月日
	 */
	String MYSQL_FORMAT_DATETIME_DAY = "%Y-%m-%d";
	/**
	 * 年月日时分秒  mysql %Y-%m-%d %T
	 */
	String MYSQL_FORMAT_DATETIME_SECOND = "%Y-%m-%d %T";
	/**
	 * 时分秒 mysql %T
	 */
	String MYSQL_FORMAT_DATETIME_SIMPLE_DAY = "%T";

	/**
	 * 获取方法里 所有的 时间格式
	 *
	 * @return 所有时间格式
	 * @throws IllegalAccessException 非法
	 */
	static List<String> getAllFormat() throws IllegalAccessException {
		List<String> list = new ArrayList<>();
		Field[] fields = TimeFormat.class.getFields();
		for (Field f : fields) {
			//获取属性f.getName()
			//获取属性值f
			Object value = f.get(TimeFormat.class);
			list.add(value.toString());
		}
		return list;
	}
}
