package cn.tannn.jdevelops.jpa.select;

import cn.hutool.core.util.ReflectUtil;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectIgnoreField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLConnect;
import cn.tannn.jdevelops.jpa.select.criteria.ExtendSpecification;
import cn.tannn.jdevelops.jpa.select.criteria.Restrictions;
import cn.tannn.jdevelops.jpa.select.criteria.SimpleExpression;
import cn.tannn.jdevelops.jpa.utils.IObjects;
import cn.tannn.jdevelops.jpa.utils.JpaUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 自定义 建议用 {@link ExtendSpecification}
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/11 下午3:54
 */
@Deprecated
public class CustomSpecification {
    /**
     * 组装查询条件
     *
     * @param bean 条件实体
     * @param <T>  返回实体
     * @param <B>  条件实体
     * @return JPAUtilExpandCriteria
     */
    private static <T, B> ExtendSpecification<T> selectWheres(B bean) {
        ExtendSpecification<T> jpaSelect = new ExtendSpecification<>();
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 字段被忽略
            JpaSelectIgnoreField ignoreField = field.getAnnotation(JpaSelectIgnoreField.class);
            if (Objects.nonNull(ignoreField)) {
                continue;
            }
            JpaSelectOperator selectOperator = field.getAnnotation(JpaSelectOperator.class);
            Object fieldValue = ReflectUtil.getFieldValue(bean, field);
            if (Objects.nonNull(selectOperator)) {
                // 使用自定义的名字
                if (!IObjects.isBlank(selectOperator.fieldName())) {
                    fieldName = selectOperator.fieldName();
                }
                SimpleExpression simpleExpression = JpaUtils.jpaSelectOperatorSwitch(selectOperator, fieldName, fieldValue);
                if (Objects.equals(selectOperator.connect(), SQLConnect.OR)) {
                    jpaSelect.or(simpleExpression);
                } else {
                    jpaSelect.add(simpleExpression);
                }
            } else {
                // 没有注解所有属性都要处理成条件
                jpaSelect.add(Restrictions.eq(fieldName, fieldValue, true, true));
            }
        }
        return jpaSelect;
    }



}
