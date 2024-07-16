package cn.tannn.jdevelops.utils.validation.idcard;


import cn.tannn.jdevelops.utils.validation.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 身份证号注解校验器
 * @author tnnn
 */
public class IdCardValidator implements ConstraintValidator<IdCard, CharSequence> {

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
    public void initialize(IdCard constraintAnnotation) {
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
