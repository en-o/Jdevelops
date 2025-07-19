package cn.tannn.jdevelops.utils.core.map;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试用的比对类
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/6/15 12:08
 */
public class ComparatorUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ComparatorUtil.class);

    /**
     * 比较两个对象的差异并打印
     * @param before 修改前的对象
     * @param after 修改后的对象
     */
    public static Map<String, Pair<Object,Object>> compareAndPrintDifference(Object before, Object after) {
        HashMap<String, Pair<Object, Object>> pairHashMap = new HashMap<>();
        if (before == null || after == null) {
            LOG.warn("比较对象不能为空");
            return pairHashMap;
        }

        if (!before.getClass().equals(after.getClass())) {
            LOG.warn("只能比较相同类型的对象");
            return pairHashMap;
        }

        try {
            for (Field field : before.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object beforeValue = field.get(before);
                Object afterValue = field.get(after);

                if (beforeValue != null && afterValue != null && !beforeValue.equals(afterValue)) {
                    LOG.info("字段: {}, 修改前: {}, 修改后: {}",
                            field.getName(), beforeValue, afterValue);
                    pairHashMap.put(field.getName(), Pair.of(beforeValue, afterValue));
                }
            }
        } catch (IllegalAccessException e) {
            LOG.error("比较对象时发生错误: {}", e.getMessage(), e);
        }
        return pairHashMap;
    }
}
