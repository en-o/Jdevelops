package cn.tannn.jdevelops.utils.desensitized.annotation;

import cn.tannn.jdevelops.utils.desensitized.enums.CoverRuleEnum;
import cn.tannn.jdevelops.utils.desensitized.serializer.CoverSerialize;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 特殊返回值进行遮掩处理   eg: 手机号中间加*
 *   RUNTIME在class字节码文件中存在，在运行时可以通过反射获取到
 *   TYPE : 接口、类、枚举
 *   FIELD: 字段、枚举的常量
 *  @author tnnn
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = CoverSerialize.class)
public @interface Cover {

    /**
     * 处理敏感的规则
     */
    CoverRuleEnum rule();

}
