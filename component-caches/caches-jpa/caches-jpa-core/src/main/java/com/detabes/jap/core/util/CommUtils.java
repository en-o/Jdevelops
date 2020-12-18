package com.detabes.jap.core.util;

import com.detabes.enums.string.StringEnum;

import java.lang.reflect.Method;

/**
 * @author tn
 * @version 1
 * @ClassName CommUtils
 * @description Jpa项目里的工具类
 * @date 2020/6/28 23:16
 */
public class CommUtils {

	/**
	 * 根据字段名称获取对象的属性
	 *
	 * @param fieldName
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValueByName(String fieldName, Object target) throws Exception {
		if (isBlank(fieldName)) {
			fieldName = "id";
		}

		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		Method method = target.getClass().getMethod(getter, new Class[0]);
		Object e = method.invoke(target, new Object[0]);
		return e;
	}

	public static boolean isBlank(final CharSequence idFieldName) {
		int strLen;
		if (idFieldName == null || StringEnum.NULL_STRING.getStr().equals(idFieldName) || (strLen = idFieldName.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(idFieldName.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
