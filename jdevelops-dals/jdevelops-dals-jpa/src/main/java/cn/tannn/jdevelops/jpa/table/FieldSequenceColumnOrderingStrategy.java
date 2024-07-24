package cn.tannn.jdevelops.jpa.table;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategy;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyLegacy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 表生成时对列的顺序自定义
 * <p> 按字段顺序对列进行排序（继承类在最后，ID除外
 * <p> - orderTableColumns对表的列进行排序。如果列没有排序，可能返回null。
 * <p> - orderConstraintColumns 对约束的列排序,如果列没有排序。可能返回null。
 * <p> - orderUserDefinedTypeColumns对用户定义类型的列排序。如果列没有排序，可能返回null。
 * <p> - orderTemporaryTableColumns 对临时表的列进行排序。
 * @author tn
 * @date 2024/7/24 上午10:05
 */
@Configuration
public class FieldSequenceColumnOrderingStrategy extends ColumnOrderingStrategyLegacy
        implements HibernatePropertiesCustomizer {



    @Override
    public List<Column> orderTableColumns(Table table, Metadata metadata) {
        return table.getColumns().stream()
                .sorted(Comparator.comparing(Column::getTypeIndex))
                .toList();
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.COLUMN_ORDERING_STRATEGY, this);
    }

}
