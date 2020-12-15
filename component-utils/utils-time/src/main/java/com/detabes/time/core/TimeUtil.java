package com.detabes.time.core;

import com.detabes.time.constant.TimeFormat;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间类
 * 其中用了  SimpleDateFormat 这个的都不建议用 会有并发问题
 * @author tn
 */
public class TimeUtil {

	/**
	 * 天
	 */
	static long nd = 1000 * 24 * 60 * 60;
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
	 * @return [年，月，日，周几，12小时制小时，二十小时制小时，分，秒]
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
	 * @param dBegin 开始时间
	 * @param dEnd 结束时间
	 * @return {List}
	 */
	public static List<String> findDates(Date dBegin, Date dEnd) {
		List<String> lDate = new ArrayList<>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		lDate.add(sd.format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(sd.format(calBegin.getTime()));
		}
		return lDate;
	}


	/**
	 * 判断段两个时间的先后
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return {long}
	 */
	public static long result(Date beginDate, Date endDate) {
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();
		if(diff<0) {
			return -1;
		}
		//计算相差多少天
		return diff / nd;
	}




	/**
	 * 某段时间相差几天几时几分几秒
	 * @param beginDate 起始时间
	 * @param endDate 结束时间
	 * @param type 1、天，2、时；3、分；4，秒
	 * @return {String}
	 */
	public static String getDatePoor(Date beginDate, Date endDate,int type) {

		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();

		String str;
		
		//计算相差多少天
		long day = diff / nd;
		str = day+"天";
		if(1 == type) {
			return str;
		}
		// 计算差多少小时
		long hour = diff % nd / nh;
		str += (hour<10?("0"+hour):hour)+"小时";
		if(2 == type) {
			return str;
		}
		
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		str += (min<10?("0"+min):min)+"分";
		if(3 == type) {
			return str;
		}
		
		//计算差多少秒
		long sec = diff % nd % nh % nm / ns;
		str += (sec<10?("0"+sec):sec)+"秒";
		return str;
	}
	/**
	 * 某段时间相差几天几时几分几秒的数字
	 * @param beginDate 起始时间
	 * @param endDate 结束时间
	 * @param type 1、天，2、时；3、分；4，秒
	 * @return {long}
	 */
	public static Long getDatePoor1(Date beginDate, Date endDate,int type) {

		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - beginDate.getTime();

		//计算相差多少天
		long day = diff / nd;
		if(type==1) {
			return day;
		}
		// 计算差多少小时
		long hour = diff / nh;
		if(type==2) {
			return hour;
		}
		
		// 计算差多少分钟
		long min = diff/ nm;
		if(type==3) {
			return min;
		}
		//计算差多少秒
		return diff/ ns;
	}




	/**
	 * 获取某年第一天日期
	 * @param year 指定年份
	 * @param strFormat 指定时间格式
	 * @return {String}   yy-mm-dd 00:00:00
	 */
	@Deprecated
	public static String getYearFirst(int year,TimeFormat strFormat){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return new DateTime(currYearFirst).toString(strFormat.toString());
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 指定年份
	 * @param strFormat 指定时间格式
	 * @return {String}   yyyy-MM-dd HH:mm:ss 返回 yy-mm-dd 23:59:59
	 */
	@Deprecated
	public static String getYearLast(int year,String strFormat){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		String format8 = new SimpleDateFormat(strFormat).format(currYearLast);
		if(TimeFormat.DEFAULT_FORMAT.equals(strFormat)){
			 format8 = new SimpleDateFormat(TimeFormat.NORM_DATE_FORMAT).format(currYearLast)+" 23:59:59";
		}
		return format8;
	}




	/**
	 * date2比date1多的天数
	 * @param date1 date1
	 * @param date2 date2
	 * @return int
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		//同一年
		if(year1 != year2){
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++){
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

			return timeDistance + (day2-day1) ;
		}
		//不同年
		else {
			return day2-day1;
		}
	}


	/**
	 *  获取当前时间的前 后N小时
	 * @param hour    +后 -前 N小时 (1-24之间)
	 * @param format  返回格式
	 * @return String
	 */
	public static String getTimeBeginAfterTime(final Integer hour, final TimeFormat format){
		return DateTime.now().plusHours(hour).toString(format.toString());
	}

	/**
	 *  *  获取指定时间的前 后N小时
	 * @param time    +后 -前 N小时 (1-24之间)
	 * @param inFormat  指定的时间的格式
	 * @param outFormat 时间返回格式
	 * @return  String
	 */
	public static String getTimeBeginAfterTime( final String time,
												final Integer hour,
												final TimeFormat inFormat,
												final TimeFormat outFormat){
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
		return  DateTime.now().toLocalDateTime();
	}





	/***
	 *  比较时间先后顺序，a<b,return true
	 *@date 2020/6/5
	 *@param  aTime （HH:mm）, bTime（HH:mm）
	 *@return java.lang.Boolean
	 */
	public static Boolean checkTimeBefore(String aTime, String bTime) throws ParseException {

		if(aTime.length() != bTime.length() || aTime.length() != 5){
			throw new NullPointerException("时间格式不正确");
		}else {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			Date asd = df.parse(aTime);
			Date bsd = df.parse(bTime);
			return asd.before(bsd);
		}

	}


	/**
	 * 格式化  20200110 类型时间为  2020-01-10
	 * @param dateString dateString
	 * @return String
	 */
	public static String yyyyMmdd2YyyyMmDd(String dateString){
			org.joda.time.format.DateTimeFormatter yyyyMmDd= DateTimeFormat.forPattern("yyyyMMdd");
		DateTime dateTime = DateTime.parse(dateString, yyyyMmDd);
		return dateTime.toString("yyyy-MM-dd");

	}


	/**
	 *
	 * 去除字符串时间的"-",返回格式"yyMMdd"
	 * @param time time
	 * @return Integer
	 */
	public static Integer transferTime(String time) {
		return Integer.parseInt(time.replaceAll("-", ""));
	}


	/**
	 *  LocalDateTime 转 Date
	 * @param localDateTime java.time.LocalDateTime
	 * @return Date
	 */
	public static Date localDateTimeToDate(java.time.LocalDateTime localDateTime){
		return  Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
	}

	/**
	 *  LocalDate 转 Date
	 * @param localDate LocalDate
	 * @return Date
	 */
	public static Date localDateToDate(LocalDate localDate){
		return  Date.from( localDate.atStartOfDay().atZone( ZoneId.systemDefault()).toInstant());
	}

	/**
	 *   Date 转 LocalDate
	 * @param date date
	 * @return LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date){
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 *   Date 转 LocalDateTime
	 * @param date Date
	 * @return java.time.LocalDateTime
	 */
	public static java.time.LocalDateTime dateToLocalDateTime(Date date){
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}


	/**
	 *  Timestamp  转 LocalDateTime
	 * @param timestamp Timestamp
	 * @return java.time.LocalDateTime
	 */
	public static java.time.LocalDateTime localDateTimeToDate(Timestamp timestamp){
		return  timestamp.toLocalDateTime();
	}

	/**
	 *  LocalDateTime 转 yyyy-MM-dd HH:mm:ss
	 * @param localDateTime java.time.LocalDateTime
	 * @return {yyyy-MM-dd HH:mm:ss}
	 */
	public static String localDateTimeToString(java.time.LocalDateTime localDateTime){
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}


}
