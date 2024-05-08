package cn.tannn.jdevelops.result.bean;


import cn.tannn.jdevelops.result.exception.ServiceException;
import cn.tannn.jdevelops.result.utils.ListTo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author tn
 * @version 1
 * @date 2020/6/28 16:46
 */

public class SerializableBean<B> implements Serializable {

    private static final long serialVersionUID = 315654089784739497L;

    /**
     * 将传入的source非空属性拷贝到当前bean下  - 只会忽略 null,不会忽略 ""
     * @param source 数据源
     */
    public void copy(B source) {
        BeanCopyUtil.beanCopy(source, this);
    }


    /**
     * vo dto entity 之间的转换
     * @param clazz 需要转变的类型
     * @return clazz 的类型
     */
    public <R> R to(Class<R> clazz) {
        try {
            R tag = clazz.newInstance();
            BeanCopier.copy(this, tag);
            return tag;
        } catch (Exception var3) {
            throw new ServiceException(var3);
        }
    }

    /**
     *  list
     * @param list 原数据
     * @param clazz 需要转变的类型
     * @return List<clazz>
     */
    public static <R, S> List<R> to(Collection<S> list, Class<R> clazz) {
        if (list != null && !list.isEmpty()) {
            return ListTo.to(clazz, list);
        } else {
            return new ArrayList<>();
        }
    }


    /**
     *  Iterable
     * @param iterable 原数据
     * @param clazz  需要转变的类型
     * @return List<clazz>
     */
    public static <B, SB, S extends SerializableBean<SB>> List<B> to(Iterable<S> iterable, Class<B> clazz) {
        if (iterable != null) {
            List<B> result = new ArrayList<>();
            for (SerializableBean<SB> abs : iterable) {
                result.add(abs.to(clazz));
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * bean转换
     * @param object 原数据
     * @param clazz 需要转变的类型
     * @return clazz
     */
    public static <B, S> B to2(S object, Class<B> clazz) {
        if (object != null) {
            return BeanCopier.copy(object, clazz);
        } else {
            return null;
        }
    }

    /**
     * 两个空bean转换
     * @param clazzs 原来的
     * @param clazz  需要返回的
     * @return clazz
     */
    public static <B,SB,  S extends SerializableBean<SB>> B to(Class<S> clazzs, Class<B> clazz) {
        try {
            SerializableBean<SB> abs = clazzs.newInstance();
            return abs.to(clazz);
        }catch (Exception e){
            return null;
        }
    }


}
