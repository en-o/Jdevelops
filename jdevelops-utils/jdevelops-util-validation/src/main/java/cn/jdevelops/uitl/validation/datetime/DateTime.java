package cn.jdevelops.uitl.validation.datetime;



import javax.validation.Constraint;
import javax.validation.Payload;
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
     * 正则: 如果默认的有错才使用 （(字母开头，允许5-16字节，允许字母数字下划线组合)）
     */
    String regular() default "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "时间格式不合法，正确格式为yyyy-MM-dd HH:mm:ss";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
