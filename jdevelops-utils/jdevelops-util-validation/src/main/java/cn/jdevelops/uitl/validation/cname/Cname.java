package cn.jdevelops.uitl.validation.cname;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证中文姓名（少数民族需要断开的用点，eg:阿不都热依木.阿布都热依木）
 *   空值不处理，如果要处理请使用 @NotBlank 进行处理
 * @author tnnn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {CnameValidator.class})
public @interface Cname {
    /**
     * 正则: 如果默认的有错才使用 （最少2位数，最大16位）
     */
    String regular() default "^(?:[\\u4e00-\\u9fa5·]{2,16}$)";

    /**
     * 校验不通过返回的提示信息
     */
    String message() default "请输入正确的中文姓名";

    /**
     * 分组(Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<?>[] groups() default {};

    /**
     * 负载 (Constraint要求的属性，用于分组校验和扩展，留空就好)
     */
    Class<? extends Payload>[] payload() default {};
}
