package cn.jdevelops.util.core.string.enums;


import org.apache.commons.lang3.StringUtils;

/**
 *  脱敏字段具体枚举
 *
 * @author l
 */

public enum FiltrationCodeEnum {
	/**
	 * 银行卡号
	 */
	BANK_CARD("bankCard", "银行卡号"),

	/**
	 * 固定电话
	 */
	FIXED_PHONE("fixedPhone", "固定电话"),

	/**
	 * 住址数字
	 */
	ADDRESS("address", "住址数字"),

	/**
	 * 中文姓名
	 */
	CHINESE_NAME("chineseName", "中文姓名"),

	/**
	 * 邮箱
	 */
	EMAIL("email", "邮箱"),

	/**
	 * 证件号
	 */
	MASK_IDCARD("maskIdCard", "证件号"),

	/**
	 * 手机号
	 */
	MASK_PHONE("maskPhone", "手机号"),


	;
	/**
	 * 方法
	 */
	private final String method;

	/**
	 * 方法介绍
	 */
	private final String methodName;

	/**
	 * 获取名称
	 * @param method method
	 * @return java.lang.String
	 * @author lxw
	 * @date 2021/10/21 14:12
	 */
	public static String getNodeName(String method) {
		FiltrationCodeEnum[] values = FiltrationCodeEnum.values();
		for (FiltrationCodeEnum value : values) {
			if (StringUtils.equals(method, value.getMethod())) {
				return value.getMethodName();
			}
		}
		return null;
	}

	FiltrationCodeEnum(String method, String methodName) {
		this.method = method;
		this.methodName = methodName;
	}

	public String getMethod() {
		return method;
	}

	public String getMethodName() {
		return methodName;
	}
}
