package cn.jdevelop.map.core.map;

import java.util.*;

/**
 * @author tn
 * @version 1
 * @date 2020/12/15 11:56
 */
public class MapSortUtil {

    /**
     * Map升序 以值排序
     * @param map map
     * @return map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        Map<K, V> result = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    /**
     * 降序以值排序
     * @param map map
     * @return map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort((o1, o2) -> {
            int compare = (o1.getValue()).compareTo(o2.getValue());
            return -compare;
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
