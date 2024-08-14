package cn.tannn.jdevelops.jpa.table;

import org.hibernate.boot.Metadata;
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
 * <p> 按字母顺序对列进行排序
 *
 * @author tn
 * @date 2024/7/24 上午10:05
 * @see https://robertniestroj.hashnode.dev/ordering-columns-in-a-table-in-jpahibernate
 */
@Configuration
public class AlphabeticalColumnOrderingStrategy
        extends ColumnOrderingStrategyLegacy
        implements HibernatePropertiesCustomizer {

    // 自定义排序
    @Override
    public List<Column> orderTableColumns(Table table, Metadata metadata) {
        return table.getColumns().stream()
                .sorted(Comparator.comparing(Column::getName))
                .toList();
    }

    // 将自定义类注册为排序策略
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.COLUMN_ORDERING_STRATEGY, this);
    }

}
