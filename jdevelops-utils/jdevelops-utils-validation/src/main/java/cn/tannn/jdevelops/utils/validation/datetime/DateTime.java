package cn.tannn.jdevelops.utils.validation.datetime;



import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证时间格式（默认yyyy-MM-dd HH:mm:ss）
 *   空值不处理，如果要处理请使用 @NotBlank 进行处理
 * @author tnnn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateTimeValidator.class})
public @interface DateTime {

    /**
     * 正则 {@link DateTimeRegular}
     */
    String regular() default DateTimeRegular.yyyy_MM_dd_HH_mm_ss;

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "时间格式不合法";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
