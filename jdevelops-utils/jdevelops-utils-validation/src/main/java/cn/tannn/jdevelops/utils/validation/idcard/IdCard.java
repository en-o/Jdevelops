package cn.tannn.jdevelops.utils.validation.idcard;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证身份证(大陆)
 *   空值不处理，如果要处理请使用 @Notxxx进行处理
 * @author tnnn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IdCardValidator.class})
public @interface IdCard {

    /**
     * 正则 {@link IdCardRegular}
     */
    String regular() default IdCardRegular.CN_ZH;

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "请输入正确的身份证号";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
