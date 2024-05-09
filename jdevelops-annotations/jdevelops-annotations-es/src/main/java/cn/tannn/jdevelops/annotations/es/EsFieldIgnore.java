package cn.tannn.jdevelops.annotations.es;



import java.lang.annotation.*;

/**
 * 忽略 es 索引字段 [不在加入mappings中]
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EsFieldIgnore {

}
