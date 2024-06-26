package cn.tannn.jdevelops.annotations.jpa;


import java.lang.annotation.*;

/**
 * jpa查询条件- 处理空值 （配合 EnhanceSpecification 使用）
 *
 * @author tn
 * @version 1
 * @date 2021-12-08 15:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaSelectNullField {


    /**
     * 空值验证 <br/>
     *
     * true: 空值不作为查询参数 <br/>
     * false: 需要查询为空的数据
     */
    boolean ignoreNull() default true;

    /**
     * ignoreNull = true（项目需要我这里是true,一般建议置为false） 有效  <br/>
     * true: 不允许为 [null,""," "]  <br/>
     * false: 不允许为 null
     */
    boolean ignoreNullEnhance() default true;
}
