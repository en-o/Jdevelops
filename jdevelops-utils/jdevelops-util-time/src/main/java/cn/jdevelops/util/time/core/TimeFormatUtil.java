package cn.jdevelops.util.time.core;


import cn.jdevelops.util.time.enums.SqlTimeFormat;
import cn.jdevelops.util.time.enums.TimeFormat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * MysqlTime
 * @author tn
 * @version 1
 * @date 2020/12/15 11:24
 */
public class TimeFormatUtil {



	/**
	 * 获取所有java用的时间格式字符串
	 *
	 * @return 所有时间格式
	 * @throws IllegalAccessException 非法
	 */
	static List<String> getJavaTimeFormat() throws IllegalAccessException {
		List<String> list = new ArrayList<>();
		Field[] fields = TimeFormat.class.getFields();
		for (Field f : fields) {
			Object value = f.get(TimeFormat.class);
			list.add(value.toString());
		}
		return list;
	}

	/**
	 * 获取所有sql用的时间格式字符串
	 *
	 * @return 所有时间格式
	 * @throws IllegalAccessException 非法
	 */
	static List<String> getSqlTimeFormat() throws IllegalAccessException {
		List<String> list = new ArrayList<>();
		Field[] fields = SqlTimeFormat.class.getFields();
		for (Field f : fields) {
			Object value = f.get(SqlTimeFormat.class);
			list.add(value.toString());
		}
		return list;
	}


	/**
	 * 获取所有mysql用的时间格式字符串
	 *
	 * @return 所有时间格式
	 * @throws IllegalAccessException 非法
	 */
	static List<String> getMySqlFormat() throws IllegalAccessException {
		List<String> list = new ArrayList<>();
		Field[] fields = SqlTimeFormat.class.getFields();
		for (Field f : fields) {
			String name = f.getName();
			if(name.startsWith("MYSQL_")){
				Object value = f.get(SqlTimeFormat.class);
				list.add(value.toString());
			}
		}
		return list;
	}

	/**
	 * 将mysql的时间格式转换成java的
	 *
	 * @param format 格式
	 * @return {String}
	 */
	public static String mysql2Java(String format) {
		switch (format) {
			case SqlTimeFormat.MYSQL_FORMAT_DATETIME_SECOND:
				format = TimeFormat.DEFAULT_FORMAT_DATETIME;
				break;
			case SqlTimeFormat.MYSQL_FORMAT_DATETIME_DAY:
				format = TimeFormat.NORM_FORMAT_DATETIME_DAY;
				break;
			case SqlTimeFormat.MYSQL_FORMAT_DATETIME_MONTH:
				format = TimeFormat.NORM_FORMAT_DATETIME_MONTH;
				break;
			case SqlTimeFormat.MYSQL_FORMAT_DATETIME_YEAR:
				format = TimeFormat.NORM_FORMAT_DATETIME_YEAR;
				break;
			case SqlTimeFormat.MYSQL_FORMAT_DATETIME_SIMPLE_DAY:
				format = TimeFormat.NORM_FORMAT_DATETIME_SIMPLE_SECOND;
				break;
			default:
				break;
		}
		return format;
	}
}
