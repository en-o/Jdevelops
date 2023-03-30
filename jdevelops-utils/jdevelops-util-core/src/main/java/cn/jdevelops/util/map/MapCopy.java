package cn.jdevelops.util.map;


import cn.jdevelops.util.map.bean.BeanCopier;

/**
 * map复制
 * @author tn
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
            return tag;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}
