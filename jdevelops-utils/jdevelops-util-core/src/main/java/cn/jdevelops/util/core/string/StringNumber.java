package cn.jdevelops.util.core.string;



import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串数字相关
 *
 * @author tn
 * @version 1
 * @date 2020/8/11 22:07
 */
public class StringNumber {

	/**
	 * 数字
	 */
	private static Pattern PATTERN_IS_INTEGER = Pattern.compile("^[-\\+]?[\\d]*$");


	/**
	 * 返回 字符串 中的数字
	 *  第一次遇到的数字串
	 * @param s 字符串
	 * @return int (报错或没有返回0)
	 */
	public static Integer getNum(String s) {
		try {
			Pattern p = compile("\\d+");
			Matcher m = p.matcher(s);
			if (m.find()) {
				//截取到数字
				return Integer.valueOf(m.group());
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取字符串中所有0-9的数字并组成字符串
	 * eg: "ad重负123十大.abv*asd23123.111" 返回 "12323123111"
	 *
	 * @param str 字符串
	 * @return java.lang.String
	 * @author lxw
	 * @date 2020/12/16 16:18
	 */
	public static String getAllNum(String str) {
		String regex = "\\d+";
		StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}


	/**
	 * 判断是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		if(Objects.isNull(str)|| str.length() == 0){
			return false;
		}
		return PATTERN_IS_INTEGER.matcher(str).matches();
	}


	/**
	 * 统计给定字符在整个字符串中的出现次数
	 * @param context 字符串
	 * @param ch 待查询的字符
	 * @return 次数
	 */
	public static int countOccurrences(String context, String ch) {
		int count = 0;
		int index = context.indexOf(ch);

		while (index != -1) {
			count++;
			index = context.indexOf(ch, index + 1);
		}
		return count;
	}
}
