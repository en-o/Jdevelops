package com.detabes.map.core.map;

import com.detabes.map.core.bean.BeanCopier;
import com.detabes.map.core.bean.BeanCopyUtil;

/**
 * @author tn
 * @version 1
 * @ClassName MapCopy
 * @description map复制
 * @date 2020/12/22 12:33
 */
public class MapCopy {
    /**
     * 复制bean
     * @param tClass 需求bean
     * @param t 被复制的bean
     * @return tClass
     */
    public static <T> T to(Class<T> tClass,Object t){
        try {
            T tag = tClass.newInstance();
            BeanCopier.copy(t, tag);
            /* 无法处理时间 */
//           BeanCopyUtil.beanCopy(t, tag);
            return tag;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}
