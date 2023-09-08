package cn.jdevelops.sboot.authentication.jredis.util;

import java.util.List;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/7 18:03
 */
public final class ListUtil {
    public ListUtil() {
        throw new AssertionError("No cn.jdevelops.sboot.authentication.jredis.util.ListUtil instances for you!");
    }


    /**
     * 判断 values 中的数据是否在 list中都存在
     * @param list list
     * @param values values
     * @return true  list的数据中有values的数据， false: values所需要的数据在list中没有发现
     */
    public static boolean verifyList(List<String> list, String[] values) {
        for (String value : values) {
            if(null == value || value.trim().length() == 0){
                continue;
            }
           if(list.contains(value)){
               return true;
           }
        }
        return false;
    }
}
