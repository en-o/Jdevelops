package cn.tannn.jdevelops.utils.core.map;


import cn.tannn.jdevelops.utils.core.json.GsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.tannn.jdevelops.utils.core.map.MapSortUtil.sortByValueAscending;
import static cn.tannn.jdevelops.utils.core.map.MapSortUtil.sortByValueDescending;


/**
 * map工具类
 *
 * @author tn
 * @date 2020/4/9 15:12
 */
public class MapUtil {

    private static final Pattern PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
    private static final String CONTAINS_KEY_EMPTY = "empty";

    private static final Logger LOG = LoggerFactory.getLogger(MapUtil.class);

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return {Map}
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>(16);
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            LOG.error("实体对象转成Map失败", e);
        }
        return map;
    }

    /**
     * map转换为bean
     *
     * @param map       map
     * @param beanClass beanClass
     * @return T 返回类型
     * @throws Exception Exception
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        T obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }


    /**
     * Map转实体
     *
     * @param map map
     * @param obj obj
     * @return Object
     */
    public static Object transMap2Bean(Map<String, Object> map, Object obj) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            LOG.error("Map转实体失败", e);
            return null;
        }
        return obj;
    }

    /**
     * 实体转Map
     *
     * @param obj obj
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
            if (obj != null && params.containsKey("empty")) {
                params = (Map<String, Object>) obj;
            }
        } catch (Exception e) {
            LOG.error("实体转Map失败", e);
        }
        return params;
    }

    /**
     * map.
     *
     * @param <K>      the type parameter
     * @param <V>      the type parameter
     * @param supplier supplier
     * @return String string
     */
    public static <K, V> String toMap(final Supplier<MultiValueMap<K, V>> supplier) {
        return GsonUtils.getInstance().toJson(supplier.get().toSingleValueMap());
    }

    /**
     * Init query params map.
     *
     * @param query the query
     * @return the map
     */
    public static Map<String, String> initQueryParams(final String query) {
        final Map<String, String> queryParams = new LinkedHashMap<>();
        if (!StringUtils.isEmpty(query)) {
            final Matcher matcher = PATTERN.matcher(query);
            while (matcher.find()) {
                String name = decodeQueryParam(matcher.group(1));
                String eq = matcher.group(2);
                String value = matcher.group(3);
                value = !StringUtils.isEmpty(value) ? decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null);
                queryParams.put(name, value);
            }
        }
        return queryParams;
    }


    /**
     * 实体转Map
     *
     * @param obj 实体
     * @return MultiValueMap
     */
    @SuppressWarnings("unchecked")
    public static MultiValueMap<String, Object> beanToLinkedMultiValueMap(Object obj) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.add(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
            if (obj != null && params.containsKey(CONTAINS_KEY_EMPTY)) {
                params = (MultiValueMap<String, Object>) obj;
            }
        } catch (Exception e) {
            LOG.error("实体转Map失败", e);
        }
        return params;
    }


    /**
     * 实体转Map
     *
     * @param obj 实体
     * @return LinkedHashMap
     */
    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, Object> beanToLinkedHashMap(Object obj) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
            if (obj != null && params.containsKey(CONTAINS_KEY_EMPTY)) {
                params = (LinkedHashMap<String, Object>) obj;
            }
        } catch (Exception e) {
            LOG.error("实体转Map失败", e);
        }
        return params;
    }

    /**
     * 把list 计算重复元素并生成bena返回
     *
     * @param list 数据
     * @param type 1 升序  2降序
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author tn
     * @date 2020/4/22 14:55
     */

    public static Map<String, Object> getRepetitionListElementNum(List<String> list, Integer type) {
        //判断重复次数最多的一个
        Map<String, Object> beanToMap = new HashMap<>(list.size());
        try {
            Map<String, Object> chongfu = new HashMap<>(list.size());
            for (String string : list) {
                if (null == string) {
                    continue;
                }
                string = string.toLowerCase();
                int i = 1;
                for (String string2 : list) {
                    string2 = string2.toLowerCase();
                    if (string.equals(string2)) {
                        chongfu.put(string, i);
                        i++;
                    }
                }
            }
            Object dataMap = new Object();
            if (1 == type) {
                dataMap = sortByValueAscending(chongfu);
            } else if (2 == type) {
                dataMap = sortByValueDescending(chongfu);
            }

            beanToMap = beanToMap(dataMap);
        } catch (Exception e) {
            LOG.error("把list 计算重复元素并生成bena返回失败", e);
        }
        return beanToMap;
    }


    /**
     * 获取map第一位
     *
     * @param map map
     * @return Map
     */
    public static Map<String, Object> getfirstMapElement(Map<String, Object> map) {
        Map.Entry<String, Object> next = map.entrySet().iterator().next();
        Map<String, Object> beanToMap = null;
        try {
            beanToMap = beanToMap(next);
        } catch (Exception e) {
            LOG.error("获取map第一位失败", e);
        }
        return beanToMap;
    }


    /**
     * 获取map最后
     *
     * @param map map
     * @return Map
     */
    public static Map<String, Object> getlastMapElement(Map<String, Object> map) {
        Map<String, Object> beanToMap = null;
        try {
            Field tail = map.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            Object object = tail.get(map);
            beanToMap = beanToMap(object);
        } catch (Exception e) {
            LOG.error("获取map最后位失败", e);
        }
        return beanToMap;
    }


    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null != value && !"".equals(value)) {
                        map.put(key, value);
                    }
                }

            }
        } catch (Exception e) {
            LOG.error("利用Introspector和PropertyDescriptor 将Bean --> Map失败", e);
        }
        return map;

    }


    /**
     * 从map中查询想要的map项，根据key
     */
    public static <T> Map<String, T> parseMapForFilter(Map<String, T> map, String filters) {
        if (map == null) {
            return Collections.emptyMap();
        } else {
            map = map.entrySet().stream()
                    .filter(e -> checkKey(e.getKey(), filters))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
        }
        return map;
    }

    /**
     * 通过indexof匹配想要查询的字符
     */
    private static boolean checkKey(String key, String filters) {
        return key.contains(filters);
    }


    /**
     * Decode query param string.
     *
     * @param value the value
     * @return the string
     */
    public static String decodeQueryParam(final String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据value值获取到对应的一个key值
     * ps: 此方法用于 k,v 都不允许重复的情况下
     *
     * @param map   map
     * @param value value
     * @return key
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        K key = null;
        for (Iterator<K> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            K getKey = iterator.next();
            if (map.get(getKey).equals(value)) {
                key = getKey;
            }
        }
        return key;
    }


    /**
     * 判断map中的某个值是否存在
     *
     * @param map   map
     * @param value value
     * @return true 存在
     */
    public static <K, V> boolean valueExist(Map<K, V> map, V value) {
        Collection<V> values = map.values();
        for (Object v : values) {
            if (value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
