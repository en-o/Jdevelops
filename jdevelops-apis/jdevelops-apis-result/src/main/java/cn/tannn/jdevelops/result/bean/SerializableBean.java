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
     * 浅拷贝
     * @param source  查询出来的数据
     */
    public void copy(B source) {
        BeanCopyUtil.beanCopy(source, this);
    }


    /**
     * 获取实体类的字段名称
     */
    public static <B>  String of(ColumnSFunction<B, ?> fn){
        return ColumnUtil.getFieldName(fn);
    }

    /**
     * 获取实体类的字段名称
     * @param toLine  是否转驼峰（默认不转） true:驼峰 。 false：正常bean字段
     */
    public static <B> String of(ColumnSFunction<B, ?> fn, Boolean toLine){
        return ColumnUtil.getFieldName(fn,toLine);
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
    public static <R, S extends SerializableBean<Object>> List<R> to(Collection<S> list, Class<R> clazz) {
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
    public static <B, S extends SerializableBean<Object>> List<B> to(Iterable<S> iterable, Class<B> clazz) {
        if (iterable !=null) {
            List<B> result = new ArrayList<>();
            for (SerializableBean<Object> abs : iterable) {
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
     * bean转换
     * @param object 原数据
     * @param clazz 需要转变的类型
     * @return clazz
     */
    public static <B,  S extends SerializableBean<Object>> B to(S object, Class<B> clazz) {
        if (object != null) {
            return object.to(clazz);
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
    public static <B,  S extends SerializableBean<Object>> B to(Class<S> clazzs, Class<B> clazz) {
        try {
            SerializableBean<Object> abs = clazzs.newInstance();
            return abs.to(clazz);
        }catch (Exception e){
            return null;
        }
    }


}
