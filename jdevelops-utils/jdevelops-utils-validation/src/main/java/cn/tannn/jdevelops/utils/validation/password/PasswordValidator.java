package cn.tannn.jdevelops.utils.validation.password;


import cn.tannn.jdevelops.utils.validation.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 密码强度校验注解校验器
 * @author tan
  */
public class PasswordValidator implements ConstraintValidator<Password, CharSequence> {

    /**
     * 身份证号正则
     */
    private  Pattern pattern;

    /**
     * 在验证开始前调用注解里的方法，从而获取到一些注解里的参数
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(Password constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regular());
    }

    /**
     * 判断参数是否合法
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (StrUtil.hasText(value)) {
            // 验证
            return StrUtil.verifyRegular(pattern,value);
        }
        return true;
    }

}
