package cn.tannn.jdevelops.utils.validation.account;

/**
 * 正则
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/24 下午3:29
 */
public class AccountRegular {

    /**
     * 内置规则 - 不能使用汉字
     */
    public static final String ACCOUNT_REGULAR = "^[a-zA-Z]\\w{4,15}$";
    /**
     * 内置规则提示
     */
    public static final String ACCOUNT_MESSAGE = "账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个";

    /**
     * 邮箱账户
     */
    public static final String ACCOUNT_EMAIL = "^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";


}
