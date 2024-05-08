package cn.tannn.jdevelops.result.utils;


import cn.tannn.jdevelops.result.bean.BeanCopier;
import cn.tannn.jdevelops.result.bean.BeanCopyUtil;
import cn.tannn.jdevelops.result.exception.ServiceException;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 复制相关
 *
 * @author tn
 * @version 1
 * @date 2020/8/12 20:13
 */
public final class ListTo {


    /**
     * 复制bean
     *
     * @param tClass 返回类型
     * @param list   数据
     * @param <T>    泛型
     * @param <K>    泛型
     * @return tClass
     */
    public static <T, K> List<T> to(Class<T> tClass, Collection<K> list) {
        return list.stream().map(entity -> beanTo(tClass, entity)).collect(Collectors.toList());
    }

    /**
     * 复制bean
     * 无法处理时间
     * @param tClass 需求bean
     * @param t      被复制的bean
     * @return tClass
     */
    private static <T> T beanTo(Class<T> tClass, Object t) {
        try {
            T tag = tClass.newInstance();
            BeanCopier.copy(t, tag);
            return tag;
        } catch (Exception var3) {
            throw new ServiceException(var3);
        }
    }

    /**
     * 无法处理时间个数
     *
     * @param source 源
     * @param target 目标
     * @param <T>    t
     */
    private static <T> void beanCopy(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static Set<String> getNullPropertyNameSet(Object source) {
        return BeanCopyUtil.getNullPropertyNameSet(source);
    }


}
