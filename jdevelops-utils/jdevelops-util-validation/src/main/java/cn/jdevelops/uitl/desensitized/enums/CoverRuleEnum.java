package cn.jdevelops.uitl.desensitized.enums;

import cn.jdevelops.uitl.desensitized.util.CoverUtil;

import java.util.function.Function;

/**
 * 敏感值遮掩规则 ，邮箱，手机，身份证等
 * @author tnnn
 */
public enum CoverRuleEnum {

    /**
     * 中文姓名遮掩
     */
    CHINESE_NAME(CoverUtil::chineseName),

    /**
     * 身份证遮掩
     */
    ID_CARD(s -> CoverUtil.idCardNum(s, 3, 4)),

    /**
     * 固定电话遮掩
     */
    FIXED_PHONE(CoverUtil::fixedPhone),

    /**
     * 手机号遮掩
     */
    MOBILE_PHONE(CoverUtil::mobilePhone),

    /**
     * 地址遮掩
     */
    ADDRESS(s -> CoverUtil.address(s, 8)),

    /**
     * 电子邮箱遮掩
     */
    EMAIL(CoverUtil::email),

    /**
     * 密码遮掩
     */
    PASSWORD(CoverUtil::password)

    ;


    /**
     * 枚举的值： 函数式接口
     */
    private final Function<String, String> rules;

    public Function<String, String> rules() {
        return rules;
    }


    CoverRuleEnum(Function<String, String> rules) {
        this.rules = rules;
    }
}
