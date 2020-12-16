package com.detabes.map.core.map;


import com.detabes.enums.number.NumEnum;
import com.detabes.enums.string.StringEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.detabes.map.core.map.MapSortUtil.sortByValueAscending;
import static com.detabes.map.core.map.MapSortUtil.sortByValueDescending;


/**
 * @author tn
 * @date  2020/4/9 15:12
 * @description  map工具类 继承了  cn.hutool.core.map.MapUtil
 */
public class MapUtil  {
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
            e.printStackTrace();
        }
        return map;
    }

    /**
     * map转换为bean
     * @return T 返回类型
     * @param map map
     * @param beanClass beanClass
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
     * @param map map
     * @param obj obj
     * @return Object
     */
    public  static Object transMap2Bean(Map<String, Object> map, Object obj){
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
            e.printStackTrace();
            return null;
        }
        return obj;
    }
    /**
     * 实体转Map
     * @param obj obj
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
            if(obj!=null&&params.containsKey(StringEnum.EMPTY_STRING.getStr()) ){
                params = (Map<String, Object>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     *
     *  把list 计算重复元素并生成bena返回
     * @author tn
     * @date  2020/4/22 14:55
     * @description
     * @param list  数据
     * @param type  1 升序  2降序
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */

    public static Map<String, Object> getRepetitionListElementNum(List<String> list, Integer type){
        List<String>  ztypesplitTemp = list;
        //判断重复次数最多的一个
        Map<String, Object> beanToMap = new HashMap<>(list.size());
        try {
            Map<String, Object> chongfu = new HashMap<>(list.size());
            for (String string : list) {
                if(null==string) {
                    continue;
                }
                string = string.toLowerCase();
                int i = 1;
                for (String string2 : ztypesplitTemp) {
                    string2 = string2.toLowerCase();
                    if(string.equals(string2)) {
                        chongfu.put(string,i);
                        i++;
                    }
                }
            }
            Object dataMap = new Object();
            if(1 == type) {
                dataMap= sortByValueAscending(chongfu);
            }else if(NumEnum.TWO.getNum().equals(type)) {
                dataMap= sortByValueDescending(chongfu);
            }

            beanToMap = beanToMap(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanToMap;
    }



    /**
     * 获取map第一位
     * @param map map
     * @return Map
     */
    public static Map<String, Object> getfirstMapElement(Map<String, Object> map) {
        Map.Entry<String, Object> next = map.entrySet().iterator().next();
        Map<String, Object> beanToMap = null;
        try {
            beanToMap = beanToMap(next);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanToMap;
    }


    /**
     * 获取map最后
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
            e.printStackTrace();
        }
        return beanToMap;
    }


    /** Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>(16);
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
                    if(null !=value && !"".equals(value)) {
                        map.put(key, value);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;

    }

    /**
     * map拆解 拼接成 xx=xx&xx=xx
     * @param map map
     * @return String
     */
    public static String mapOrderStr(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());

        Collections.sort(list, Comparator.comparing(Map.Entry::getKey));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey() + "=" + mapping.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }


    /**
     * 从map中查询想要的map项，根据key
     */
    public static<T> Map<String, T> parseMapForFilter(Map<String, T> map, String filters) {
        if (map == null) {
            return null;
        } else {
            map = map.entrySet().stream()
                    .filter((e) -> checkKey(e.getKey(),filters))
                    .collect(Collectors.toMap(
                            (e) -> (String) e.getKey(),
                            (e) -> e.getValue()
                    ));
        }
        return map;
    }

    /**
     * 通过indexof匹配想要查询的字符
     */
    private static boolean checkKey(String key, String filters) {
        if (key.indexOf(filters)>-1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * map 转 str
     * @param map
     * @return
     */
    public static String map2Str(Map<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> mapping : list) {
            sb.append(mapping.getKey() + "=" + mapping.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
