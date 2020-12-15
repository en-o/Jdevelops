package com.detabes.list.core;


import com.detabes.list.number.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 书籍相关-业务定制
 * @author tn
 * @version 1
 * @ClassName ListBook
 * @description 书籍相关-业务定制
 * @date 2020/8/12 19:23
 */
public class ListBook {

    /**
     * 中图分类中测试成功
     *  <pre>
     * eg: maps = ListUtil.mergeListMap(pendingData, "ptime", "ptimeNum","proportion");
     *       map格式例子：
     *          "proportion": 0.11,
     *          "ptimeNum": 182,
     *          "ptime": "2018"
     *  eqKey 相等  addingkey 相加（合并 eqKey 相同数据）
     *  </pre>
     * @param listMap 原始数据
     * @param eqKey 用于比较的键
     * @param addingkey 用于相加的键 数量
     * @param  ratioKey 占比键名
     * @return {List}
     */
    public static List<Map<String,Object>> mergeListMap(List<Map<String, Object>> listMap, String eqKey, String addingkey, String ratioKey){
        try {
            Map<String, Map<String, Object>> result = new HashMap<>(listMap.size());
            List<Map<String, Object>>  allList = new ArrayList<>();
            for(Map<String, Object> map : listMap){
                String id = map.get(eqKey).toString();
                long value = Long.parseLong(map.get(addingkey).toString());
                //存在即相加
                if(result.containsKey(id)){
                    long temp = Long.parseLong(result.get(id).get(addingkey).toString());
                    value += temp;
                    result.get(id).put(addingkey, value);
                    continue;
                }
                result.put(id, map);
                allList.add(map);
            }
            //书籍总数
            int bookNuM = 0;
            for(Map<String, Object> m : allList){
                bookNuM += Integer.parseInt(m.get(addingkey).toString());
            }
            int index = 0;
            for(Map<String, Object> m : allList){
                Double doubleValue = Double.parseDouble(m.get(addingkey).toString());
                //增量
                BigDecimal div = NumberUtil.div(doubleValue, bookNuM);
                BigDecimal mul = NumberUtil.mul(div, 100);
                //保留小数
                BigDecimal round = NumberUtil.round(mul, 2);
                allList.get(index).put(ratioKey,round);
                index++;
            }

            allList.sort((o1, o2) -> {
                String name1 = (String) o1.get(eqKey);
                String name2 = (String) o2.get(eqKey);
                return name2.compareTo(name1);
            });

            return  allList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
