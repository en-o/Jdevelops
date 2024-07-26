package cn.tannn.jdevelops.jpa.table;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 注入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/24 上午10:36
 */
public class ImportColumnOrderSpringIoc {

//    /**
//     * 字母排序（不启用
//     */
//    @Bean
//    @ConditionalOnProperty(
//            value = "jdevelops.jpa.column.order",
//            havingValue = "alphabet")
//    public AlphabeticalColumnOrderingStrategy alphabeticalColumnOrderingStrategy() {
//        return new AlphabeticalColumnOrderingStrategy();
//    }
//
//    /**
//     * 字段顺序排序 (默认启用
//     */
//    @Bean
//    @ConditionalOnProperty(
//            value = "jdevelops.jpa.column.order",
//            havingValue = "field", matchIfMissing = true)
//    public FieldSequenceColumnOrderingStrategy fieldSequenceColumnOrderingStrategy() {
//        return new FieldSequenceColumnOrderingStrategy();
//    }
}
