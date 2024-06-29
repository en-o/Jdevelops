package cn.tannn.jdevelops.utils.validation.password;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证密码强度校验
 *   空值不处理，如果要处理请使用 @NotBlank 进行处理
 * @author tnnn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {
    /**
     * 正则: 如果默认的有错才使用 （(字母开头，允许6-16字节，允许字母数字下划线组合)）
     */
    String regular() default "^\\S*(?=\\S{6,})(?=\\S*\\d)(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*[!@#$%^&*? ])\\S*$";

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
