package com.detabes.string.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串数字相关
 *
 * @author tn
 * @version 1
 * @ClassName StringNumber
 * @date 2020/8/11 22:07
 */
public class StringNumber {


	/**
	 * 返回 字符串 中的数字
	 *
	 * @param s 字符串
	 * @return int
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
			e.printStackTrace();
		}
		return 0;
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
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}
}
