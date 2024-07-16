package cn.tannn.jdevelops.utils.validation.mobile;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 手机号验证（国内）
 *  空值不处理，如果要处理请使用 @Notxxx进行处理
 * @author tann
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {

    /**
     * 正则 {@link MobileRegular}
     *
     */
    String regular() default MobileRegular.PHONE_CH;

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "请输入正确的手机号码格式";

    /**
     * Constraint要求的属性，用于分组校验和扩展，留空就好
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
