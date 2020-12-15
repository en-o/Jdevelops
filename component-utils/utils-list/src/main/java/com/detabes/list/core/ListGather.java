package com.detabes.list.core;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 集合相关
 * @author tn
 * @version 1
 * @ClassName ListGather
 * @description 集合相关
 * @date 2020/8/12 20:17
 */
public class ListGather {

    /**
     * 获取 set集合的 交集
     * @param set1 集合1
     * @param set2 集合2
     * @param <T> 泛型
     * @return 返回交集

     */
    public static<T> Set<T> getSetIntersection(Set<T> set1, Set<T> set2){
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    /**
     * 获取String 集合 中的最大值最小值
     * @param listStr list
     * @param maxMin 1小 其余大(包含null)
     * @return 返回数值
     */
    public static Integer getListStringForMaxMin(List<String> listStr, Integer maxMin){
        try{
            Comparator<String> cmp = (o1, o2) -> {
                if(maxMin!=null&&maxMin==1){
                    return Integer.valueOf(o2).compareTo(Integer.valueOf(o1));
                }else{
                    return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                }

            };
            return Integer.parseInt(Collections.max(listStr, cmp));
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    /**
     *  两个集合的差集
     *   left 跟 right 除去共有部分，剩下的数据
     * @param left list<Bean> 1
     * @param right list<Bean> 1
     * @param key 用于判断的key 比如根据实体的 id值 取两个lis
     * @return  List
     */
    public static<Bean> List<Bean> differenceBothway (List<Bean> left, List<Bean> right, String key){
        List<Bean> res = new ArrayList<>();
        List<Bean> beansLeft = differenceOneway(left, right, key);
        List<Bean> beansRight = differenceOneway(right, left , key);
       if(null!=beansLeft&&beansLeft.size()>0) {
           res.addAll(beansLeft);
       }
       if(null!=beansRight&&beansRight.size()>0) {
           res.addAll(beansRight);
       }
       return res;
    }


    /**
     *  right 中 left 没有的数据
     * @param left list<Bean> 1
     * @param right list<Bean> 1
     * @param key 用于判断的key 比如根据实体的 id值 取两个lis
     * @return List
     */
    public static<Bean> List<Bean> differenceOneway(List<Bean> left, List<Bean> right, String key){
        if (left == null){
            return right;
        }
        if (right == null){
            return left;
        }
        //使用LinkedList方便插入和删除
        List<Bean> res = new LinkedList<>(left);
        List<String> set = new ArrayList<>();
        //key值 存放到set
        for(Bean item : right){
            Map<String, Object> itemT = object2Map(item);
            set.add(itemT.get(key).toString());
        }
        //迭代器遍历listA
        Iterator<Bean> iter = res.iterator();
        while(iter.hasNext()){
            Bean item = iter.next();
            Map<String, Object> itemT = object2Map(item);
            //如果set中包含id则remove
            if(set.contains(itemT.get(key).toString())){
                iter.remove();
            }
        }
        return res;
    }

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return {Map}
     */
    private static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object values = field.get(obj);
                if(values!=null){
                    map.put(field.getName(), values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
