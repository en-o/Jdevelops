package cn.jdevelops.data.es.annotation;


import cn.jdevelops.data.es.annotation.basic.EsFieldBasic;
import cn.jdevelops.data.es.annotation.basic.EsFieldMultiType;

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
