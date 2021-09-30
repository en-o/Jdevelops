package com.detabes.time.core;


import com.detabes.constant.time.TimeFormat;
import com.detabes.enums.number.NumEnum;
import com.detabes.enums.time.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间类
 * 其中用了  SimpleDateFormat 这个的都不建议用 会有并发问题
 *
 * @author tn
 */
public class TimeUtil {

	/**
	 * 天
	 */
	static long nd = 1000 * 60 * 60 * 24;
	/**
	 * 时
	 */
	static long nh = 1000 * 60 * 60;
	/**
	 * 分
	 */
	static long nm = 1000 * 60;
	/**
	 * 秒
	 */
	static long ns = 1000;


	/**
	 * 当前时间的数字拆分数字集合
	 *
	 * @param date 时间
	 * @return [年，月，日，周几，12小时制小时，二十四小时制小时，分，秒]
	 */
	public static List<Integer> resultNumbers(Date date) {
		List<Integer> l = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		l.add(c.get(Calendar.YEAR));
		l.add(c.get(Calendar.MONTH) + 1);
		l.add(c.get(Calendar.DATE));
		l.add(c.get(Calendar.DAY_OF_WEEK) - 1);
		l.add(c.get(Calendar.HOUR));
		l.add(c.get(Calendar.HOUR_OF_DAY));
		l.add(c.get(Calendar.MINUTE));
		l.add(c.get(Calendar.SECOND));
		return l;
	}

	/**
	 * 某个时间段内所有的日期
	 *
	 * @param beginDate 开始时间
	 * @param endDate   结束时间
	 * @return {List}
	 */
	public static List<String> findDates(Date beginDate, Date endDate) {
		List<String> dateList = new ArrayList<>();
		String begin = new DateTime(beginDate).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
		dateList.add(begin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(beginDate);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(endDate);
		// 测试此日期是否在指定日期之后
		while (endDate.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			String end = new DateTime(calBegin.getTime()).toString(TimeFormat.NORM_FORMAT_DATETIME_DAY);
			dateList.add(end);
		}
		return dateList;
	}

	/**
	 * 判断段两个时间的先后
	 *
	 * @param beginDate 开始时间
	 * @param endDate   结束时间
	 * @return {long}  beginDate<endDate 返回 endDate 大多少天，反正直接返回-1
	 */
	public static long result(Date beginDate, Date endDate) {
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		if (diff < 0) {
			return -1;
		}
		//计算相差多少天
		return diff / nd;
	}

	/**
	 * 某段时间相差几天几时几分几秒
	 *
	 * @param beginDate 起始时间
	 * @param endDate   结束时间
	 * @param type      1、天，2、时；3、分；4，秒
	 * @return {String}
	 */
	public static String getDatePoor2String(Date beginDate, Date endDate, int type) {

		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();

		String str;

		//计算相差多少天
		long day = diff / nd;
		str = day + "天";
		if (1 == type) {
			return str;
		}
		// 计算差多少小时
		long hour = diff % nd / nh;
		str += (hour < 10 ? ("0" + hour) : hour) + "小时";
		if (NumEnum.TWO.getNum() == type) {
			return str;
		}

		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		str += (min < 10 ? ("0" + min) : min) + "分";
		if (NumEnum.THREE.getNum() == type) {
			return str;
		}

		//计算差多少秒
		long sec = diff % nd % nh % nm / ns;
		str += (sec < 10 ? ("0" + sec) : sec) + "秒";
		return str;
	}

	/**
	 * 某段时间相差几天几时几分几秒的数字
	 *
	 * @param beginDate 起始时间
	 * @param endDate   结束时间
	 * @param type      1、天，2、时；3、分；4，秒
	 * @return {long}
	 */
	public static long getDatePoor2Number(Date beginDate, Date endDate, int type) {

		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();

		//计算相差多少天
		long day = diff / nd;
		if (type == 1) {
			return day;
		}
		// 计算差多少小时
		long hour = diff / nh;
		if (type == NumEnum.TWO.getNum()) {
			return hour;
		}

		// 计算差多少分钟
		long min = diff / nm;
		if (type == NumEnum.THREE.getNum()) {
			return min;
		}
		//计算差多少秒
		return diff / ns;
	}


	/**
	 * 获取指定年份的第一天日期
	 *
	 * @param year           指定年份
	 * @param timeFormatEnum 指定时间格式
	 * @return {String}   yy-mm-dd 00:00:00
	 */
	@Deprecated
	public static String getYearFirst(int year, TimeFormatEnum timeFormatEnum) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return new DateTime(currYearFirst).toString(timeFormatEnum.getFormat());
	}

	/**
	 * 获取指定年份最后一天日期
	 *
	 * @param year           指定年份
	 * @param timeFormatEnum 指定返回时间格式
	 * @return {String}   timeFormatEnum
	 */
	@Deprecated
	public static String getYearLast(int year, TimeFormatEnum timeFormatEnum) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		String format8 = new DateTime(currYearLast).toString(timeFormatEnum.getFormat());
		if (TimeFormat.DEFAULT_FORMAT_DATETIME.equals(timeFormatEnum.getFormat())) {
			format8 = new DateTime(currYearLast).withMillisOfDay(0).plusHours(23).plusMinutes(59).plusSeconds(59).toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
		}
		return format8;
	}


	/**
	 * 获取当前时间的前 后N小时
	 *
	 * @param hour   +后 -前 N小时 (1-24之间)
	 * @param format 返回格式
	 * @return String
	 */
	public static String getTimeBeginAfterTime(final Integer hour, final TimeFormat format) {
		return DateTime.now().plusHours(hour).toString(format.toString());
	}

	/**
	 * *  获取指定时间的前 后N小时
	 *
	 * @param time      +后 -前 N小时 (1-24之间)
	 * @param inFormat  指定的时间的格式
	 * @param outFormat 时间返回格式
	 * @return String
	 */
	public static String getTimeBeginAfterTime(final String time,
											   final Integer hour,
											   final TimeFormat inFormat,
											   final TimeFormat outFormat) {
		org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(inFormat.toString());
		DateTime dateTime = DateTime.parse(time, dateTimeFormatter);
		return dateTime.plusHours(hour).toString(outFormat.toString());
	}

	/**
	 * 获取时间  LocalDateTime
	 *
	 * @return {LocalDateTime}
	 */
	public static LocalDateTime getNowTime() {
		return DateTime.now().toLocalDateTime();
	}

	/***
	 *  比较时间先后顺序，a<b,return true
	 *@date 2020/6/5
	 *@param  aTime （HH:mm）, bTime（HH:mm）
	 *@return java.lang.Boolean
	 */
	public static Boolean checkTimeBefore(String aTime, String bTime) throws ParseException {

		if (aTime.length() != bTime.length() || aTime.length() != NumEnum.FIVE.getNum()) {
			throw new NullPointerException("时间格式不正确");
		} else {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			Date asd = df.parse(aTime);
			Date bsd = df.parse(bTime);
			return asd.before(bsd);
		}

	}


	/**
	 * 格式化  20200110 类型时间为  2020-01-10
	 *
	 * @param dateString dateString
	 * @return String
	 */
	public static String yyyyMmdd2YyyyMmDd(String dateString) {
		org.joda.time.format.DateTimeFormatter yyyyMmDd = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime dateTime = DateTime.parse(dateString, yyyyMmDd);
		return dateTime.toString("yyyy-MM-dd");

	}


	/**
	 * 去除字符串时间的"-",返回格式"yyMMdd"
	 *
	 * @param time time
	 * @return Integer
	 */
	public static Integer transferTime(String time) {
		return Integer.parseInt(time.replaceAll("-", ""));
	}


	/**
	 * LocalDateTime 转 Date
	 *
	 * @param localDateTime java.time.LocalDateTime
	 * @return Date
	 */
	public static Date localDateTimeToDate(java.time.LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * LocalDate 转 Date
	 *
	 * @param localDate LocalDate
	 * @return Date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Date 转 LocalDate
	 *
	 * @param date date
	 * @return LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Date 转 LocalDateTime
	 *
	 * @param date Date
	 * @return java.time.LocalDateTime
	 */
	public static java.time.LocalDateTime dateToLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}


	/**
	 * Timestamp  转 LocalDateTime
	 *
	 * @param timestamp Timestamp
	 * @return java.time.LocalDateTime
	 */
	public static java.time.LocalDateTime localDateTimeToDate(Timestamp timestamp) {
		return timestamp.toLocalDateTime();
	}

	/**
	 * LocalDateTime 转 yyyy-MM-dd HH:mm:ss
	 *
	 * @param localDateTime java.time.LocalDateTime
	 * @return {yyyy-MM-dd HH:mm:ss}
	 */
	public static String localDateTimeToString(java.time.LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 计算时间差
	 *
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param beginTimeFormat 开始时间格式
	 * @param endTimeFormat   结束时间格式
	 * @param returnTime      输出类型 0：天，1:小时，2:分钟，3：秒，4：毫秒
	 */
	public static Long timeDifference(String beginTime, TimeFormatEnum beginTimeFormat,
									  String endTime, TimeFormatEnum endTimeFormat,
									  Integer returnTime) {

		org.joda.time.format.DateTimeFormatter beginFormatter = DateTimeFormat.forPattern(beginTimeFormat.getFormat());
		org.joda.time.format.DateTimeFormatter endTFormatter = DateTimeFormat.forPattern(endTimeFormat.getFormat());
		DateTime begin = DateTime.parse(beginTime, beginFormatter);
		DateTime end = DateTime.parse(endTime, endTFormatter);
//        计算区间毫秒数
		Duration etime = new Duration(begin, end);
		switch (returnTime) {
			case 0:
				return etime.getStandardDays();
			case 1:
				return etime.getStandardHours();
			case 2:
				return etime.getStandardMinutes();
			case 3:
				return etime.getStandardSeconds();
			default:
				return etime.getMillis();
		}
	}


	/**
	 * 格式化时间字符传为 dateTime
	 *
	 * @param time       时间
	 * @param timeFormat 时间类型
	 * @return DateTime
	 */
	public static DateTime formatStr(String time, TimeFormatEnum timeFormat) {
		org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(timeFormat.getFormat());
		return DateTime.parse(time, dateTimeFormatter);
	}

	/**
	 * 时间字符串格式转另外一个格式
	 * @param time 时间字符串
	 * @param timeFormat 格式
	 * @param newTimeFormat 需要转换的新格式
	 * @return String
	 */
	public static String formatStr2Str(String time, TimeFormatEnum timeFormat,
										 TimeFormatEnum newTimeFormat) {
		DateTime dateTime = formatStr(time, timeFormat);
		return dateTime.toString(newTimeFormat.getFormat());
	}

	/**
	 * 时间字符串格式转 yyyy-MM-dd HH:mm:ss 格式的字符串
	 * @param time 时间字符串
	 * @param timeFormat 格式
	 * @return String
	 */
	public static String formatStr2StrByDefault(String time, TimeFormatEnum timeFormat) {
		DateTime dateTime = formatStr(time, timeFormat);
		return dateTime.toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
	}

	/**
	 * 得到指定时间的默认格式
	 *
	 * @param date 时间
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String time2DefaultFormat(Date date) {
		return new DateTime(date).toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
	}

	/**
	 * 得到当前时间的默认格式
	 *
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String time2DefaultFormat() {
		return DateTime.now().toString(TimeFormat.DEFAULT_FORMAT_DATETIME);
	}


	/**
	 * 获取指定月份的以周分组的结果集
	 *
	 * @param times 指定 月份  eg：2021-05
	 * @return Map
	 * @throws ParseException
	 */
	public static Map<Integer, List<String>> getMonthForWeek(String times) throws ParseException {
		Map<Integer, List<String>> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c_begin = new GregorianCalendar();
		Calendar c_end = new GregorianCalendar();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] weeks = dfs.getWeekdays();
		c_begin.setTime(sdf.parse(times + "-01"));
		c_end.setTime(sdf.parse(getLastDayOfMonth(times)));
//        c_begin.set(2019, 3, 2); //Calendar的月从0-11，所以4月是3.
//        c_end.set(2019, 3, 29); //Calendar的月从0-11，所以5月是4.

		int count = 1;
		int count2 = 1;
        /*
          cal1.add(Calendar.DAY_OF_MONTH,1);
          cal1.add(Calendar.DAY_OF_YEAR,1);
          cal1.add(Calendar.DATE,1);
          就单纯的add操作结果都一样，因为都是将日期+1,区别就是在月的日期中加1还是年的日期中加1
          但是Calendar设置DAY_OF_MONTH和DAY_OF_YEAR的目的不是用来+1
          将日期加1，这通过cal1.add(Calendar.DATE,1)就可以实现
          DAY_OF_MONTH的主要作用是cal.get(DAY_OF_MONTH)，用来获得这一天在是这个月的第多少天
          Calendar.DAY_OF_YEAR的主要作用是cal.get(DAY_OF_YEAR)，用来获得这一天在是这个年的第多少天。
          DAY_OF_WEEK，用来获得当前日期是一周的第几天
         */

		c_end.add(Calendar.DAY_OF_YEAR, 1);  //结束日期下滚一天是为了包含最后一天
		ArrayList<String> objects = new ArrayList<>();
		while (c_begin.before(c_end)) {
			if (count == count2) {
				objects = new ArrayList<>();
				count2++;
			}
			objects.add(new java.sql.Date(c_begin.getTime().getTime()).toString());
//            System.out.println("第" + count + "周  日期：" + new java.sql.Date(c_begin.getTime().getTime()) + "," + weeks[c_begin.get(Calendar.DAY_OF_WEEK)]);
			if (c_begin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				map.put(count, objects);
				count++;
			}
			c_begin.add(Calendar.DAY_OF_YEAR, 1);

		}
		map.put(count, objects);
		return map;
	}


	/**
	 * 月最后一天
	 *
	 * @param yearMonth yyyy-MM
	 * @return String
	 */
	public static String getLastDayOfMonth(String yearMonth) {
		int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
		int month = Integer.parseInt(yearMonth.split("-")[1]); //月
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		// cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.MONTH, month); //设置当前月的上一个月
		// 获取某月最大天数
		//int lastDay = cal.getActualMaximum(Calendar.DATE);
		int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
		// 设置日历中月份的最大天数
		//cal.set(Calendar.DAY_OF_MONTH, lastDay);
		cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * 计算时间相差天数
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return 天数
	 */
	public static int differDay(org.joda.time.LocalDate begin, org.joda.time.LocalDate end){
		// 验证是否为工作日且为  当前时间3天后
		return Days.daysBetween( begin ,end).getDays();
	}

	/**
	 * 计算时间相差天数
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return int
	 */
	@Deprecated
	public static int differentDays(Date begin, Date end) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(begin);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(end);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		//同一年
		if (year1 != year2) {
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				//闰年
				//不是闰年
				if (((i % 4) == 0) && ((i % 100) != 0)) {
					timeDistance += 366;
				} else if ((i % 400) == 0) {
					timeDistance += 366;
				} else {
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);
		}
		//不同年
		else {
			return day2 - day1;
		}
	}

}