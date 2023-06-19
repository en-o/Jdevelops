package cn.jdevelops.uitl.validation.account;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线组合) 不能使用汉字
 *   空值不处理，如果要处理请使用 @NotBlank 进行处理
 * @author tnnn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AccountValidator.class})
public @interface Account {
    /**
     * 正则: 如果默认的有错才使用 （(字母开头，允许5-16字节，允许字母数字下划线组合)）
     */
    String regular() default "^[a-zA-Z]\\w{4,15}$";

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
