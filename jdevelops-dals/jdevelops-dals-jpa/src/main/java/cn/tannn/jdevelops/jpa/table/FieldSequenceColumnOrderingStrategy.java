package cn.tannn.jdevelops.jpa.table;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategy;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyLegacy;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表生成时对列的顺序自定义
 * <p> 按字段顺序对列进行排序（继承类在最后，ID除外
 * <p> - orderTableColumns对表的列进行排序。如果列没有排序，可能返回null。
 * <p> - orderConstraintColumns 对约束的列排序,如果列没有排序。可能返回null。
 * <p> - orderUserDefinedTypeColumns对用户定义类型的列排序。如果列没有排序，可能返回null。
 * <p> - orderTemporaryTableColumns 对临时表的列进行排序。
 *
 * @author tn
 * @date 2024/7/24 上午10:05
 */
@Configuration
public class FieldSequenceColumnOrderingStrategy extends ColumnOrderingStrategyLegacy
        implements HibernatePropertiesCustomizer {


    // 自定义排序
    @Override
    public List<Column> orderTableColumns(Table table, Metadata metadata) {
        // 获取对应实体类
        MetadataImplementor metadataImplementor = (MetadataImplementor) metadata;
        PersistentClass persistentClass = metadataImplementor.getEntityBinding(table.getName());
        if (persistentClass == null) {
            return null; // 不是持久化类，返回null
        }

        // 获取实体类的字段顺序
        Class<?> entityClass = persistentClass.getMappedClass();
        Field[] fields = entityClass.getDeclaredFields();

        // 创建字段顺序映射
        Map<String, Integer> fieldOrderMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            fieldOrderMap.put(fields[i].getName(), i);
        }

        // 按照字段顺序映射排序列
        return table.getColumns().stream()
                .map(Column.class::cast)
                .sorted((col1, col2) -> {
                    Integer order1 = fieldOrderMap.get(col1.getName());
                    Integer order2 = fieldOrderMap.get(col2.getName());
                    if (order1 == null || order2 == null) {
                        return 0; // 保持原顺序
                    }
                    return order1.compareTo(order2);
                })
                .collect(Collectors.toList());
    }

    // 将自定义类注册为排序策略
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.COLUMN_ORDERING_STRATEGY, this);
    }

}
