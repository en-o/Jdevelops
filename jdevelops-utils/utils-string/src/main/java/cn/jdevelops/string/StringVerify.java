package cn.jdevelops.string;

import cn.jdevelops.enums.number.NumEnum;
import cn.jdevelops.enums.sql.SqlStrFilterEnum;
import cn.jdevelops.enums.string.StringEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * 验证相关
 *
 * @author tn
 * @version 1
 * @date 2020/8/11 22:01
 */
@Slf4j
public class StringVerify {


	private static final Map<String, Object> PHONE_REGULAR = new HashMap<>(6);
	private static final String REGX = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)" +
			"|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";

	static {
		PHONE_REGULAR.put("zh-CN", "^((\\+|00)86)?1([358][0-9]|4[579]|6[67]|7[01235678]|9[189])[0-9]{8}$");
		PHONE_REGULAR.put("en-HK", "^(\\+?852[-\\s]?)?[456789]\\d{3}[-\\s]?\\d{4}$");
		PHONE_REGULAR.put("zh-TW", "^(\\+?886\\-?|0)?9\\d{8}$");
		PHONE_REGULAR.put("en-MO", "^(\\+?853[-\\s]?)?[6]\\d{3}[-\\s]?\\d{4}$");
	}


	/**
	 * 用户名验证  -- 好像用问题 (待仔细验证)
	 * <pre>
	 * 只能包含汉字、英文、“_”和数字 且在2-10位
	 * 不能以数字开头
	 * </pre>
	 *
	 * @param name 用户名
	 * @return fasle：错误格式 /  true: 正确格式
	 */
	public static boolean checkNickName(String name) {
//        必须是3-10位字母、数字、下划线（这里字母、数字、下划线是指任意组合，没有必须三类均包含）
		String regExp = "[\\u4e00-\\u9fa5]*|\\w*|\\d*|_*";
		if (name != null) {
			if (name.length() >= NumEnum.TWO.getNum() && name.length() <= NumEnum.TEN.getNum()) {
				return name.matches(regExp);
			} else {
				return false;
			}
		} else {
			return false;
		}

	}


	/**
	 * 昵称格式：限16个字符，支持中英文、数字、减号或下划线
	 *
	 * @param nickName 昵称
	 * @return false：错误格式 / true: 正确格式
	 */
	public static boolean checkLoinName(String nickName) {
		String regStr = "^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,16}$";
		return nickName.matches(regStr);
	}


	/**
	 * 密码验证
	 * <pre>
	 * 密码必须是包含大写字母、小写字母、数字、特殊符号（不是字母，数字，下划线，汉字的字符）的8位以上组合
	 * 至少8个字符，至少1个大写字母，1个小写字母和1个数字
	 * </pre>
	 *
	 * @param pwd 密码
	 * @param is  is = 2  :密码必须是包含大写字母、小写字母、数字、特殊符号（不是字母，数字，下划线，汉字的字符）的8位以上组合
	 *            其余默认  :至少8个字符，至少1个大写字母，1个小写字母和1个数字
	 * @return false：错误格式 / true: 正确格式
	 */
	public static boolean checkPwd(String pwd, Integer is) {
		String regExp;
		if (is.equals(NumEnum.TWO.getNum())) {
			regExp = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
		} else {
			regExp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
		}

		return StringUtils.isNotBlank(pwd) && pwd.matches(regExp);
	}

	/**
	 * 手机号验证
	 *
	 * @param phone 手机号
	 * @param city  zh-CN（null时默认） || en-HK || zh-TW  ||  en-MO
	 * @return false：错误格式 /  true: 正确格式
	 */
	public static boolean checkPhone(String phone, String city) {
		if (StringUtils.isNotBlank(phone)) {
			if (StringUtils.isBlank(city)) {
				city = "zh-CN";
			}
			Pattern pattern = compile(PHONE_REGULAR.get(city).toString());
			Matcher matcher = pattern.matcher(phone);
			return matcher.matches();
		}
		return false;
	}


	/**
	 * 对常见的sql注入攻击进行拦截
	 *
	 * @param sInput 输入值
	 * @return true 表示参数不存在SQL注入风险 / false 表示参数存在SQL注入风险
	 */
	public static Boolean sqlStrFilter(String sInput) {
		if (StringUtils.isBlank(sInput) || StringEnum.NULL_STRING.getStr().equals(sInput)) {
			return false;
		}
		sInput = sInput.toUpperCase();

		if (sInput.contains(SqlStrFilterEnum.DELETE.getStr())
				|| sInput.contains(SqlStrFilterEnum.ASCII.getStr())
				|| sInput.contains(SqlStrFilterEnum.UPDATE.getStr())
				|| sInput.contains(SqlStrFilterEnum.SELECT.getStr())
				|| sInput.contains(SqlStrFilterEnum.RSQUO.getStr())
				|| sInput.contains(SqlStrFilterEnum.SUBSTR.getStr())
				|| sInput.contains(SqlStrFilterEnum.COUNT.getStr())
				|| sInput.contains(SqlStrFilterEnum.OR.getStr())
				|| sInput.contains(SqlStrFilterEnum.AND.getStr())
				|| sInput.contains(SqlStrFilterEnum.DROP.getStr())
				|| sInput.contains(SqlStrFilterEnum.EXECUTE.getStr())
				|| sInput.contains(SqlStrFilterEnum.EXEC.getStr())
				|| sInput.contains(SqlStrFilterEnum.TRUNCATE.getStr())
				|| sInput.contains(SqlStrFilterEnum.INTO.getStr())
				|| sInput.contains(SqlStrFilterEnum.DECLARE.getStr())
				|| sInput.contains(SqlStrFilterEnum.MASTER.getStr())) {
			log.error("该参数存在SQL注入风险：sInput=" + sInput);
			return false;
		}
		if (isIllegalStr(sInput)) {
			return false;
		}
		log.info("通过sql检测");
		return true;
	}

	/**
	 * 对非法字符进行检测
	 *
	 * @param sInput 字符
	 * @return true表示参数包含非法字符 / false表示参数不包含非法字符
	 */
	public static boolean isIllegalStr(String sInput) {

		if (StringUtils.isBlank(sInput) || StringEnum.NULL_STRING.getStr().equals(sInput)) {
			return false;
		}
		sInput = sInput.trim();
		Pattern compile = compile(REGX, CASE_INSENSITIVE);
		Matcher matcher = compile.matcher(sInput);
		log.info("通过字符串检测");
		return matcher.find();
	}

	/**
	 * 不是空的字符串
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("null")      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 *  </pre>
	 *
	 * @param string 字符串
	 * @return 返回真假
	 */
	public static boolean isNotEmptyString(String string) {
		return StringUtils.isNotBlank(string) && !StringEnum.NULL_STRING.getStr().equals(string);
	}

	/**
	 * 是空的字符串
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("null")      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param string 字符串
	 * @return 返回真假
	 */
	public static boolean isEmptyString(String string) {
		return StringUtils.isBlank(string) || StringEnum.NULL_STRING.getStr().equals(string);
	}
}
