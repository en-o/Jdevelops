package com.detabes.entity.basics.vo;

import com.detabes.map.core.bean.BeanCopier;
import com.detabes.map.core.bean.BeanCopyUtil;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author tn
 * @version 1
 * @ClassName SerializableVo
 * @description Serializable
 * @date 2020/6/28 16:46
 */
@Getter
public class SerializableVO<T> implements Serializable {

    private static final long serialVersionUID = 315654089784739497L;

    /**
     * 浅拷贝 - jpa更新用 mybatis根据情况用
     * @param source  查询出来的数据
     */
    public void copy(T source) {
        BeanCopyUtil.beanCopy(source, this);
    }

    /**
     * vo dto entity 之间的转换
     * @param clazz 需要转变的类型
     * @return clazz 的类型
     */
    public <T> T to(Class<T> clazz) {
        try {
            T tag = clazz.newInstance();
            BeanCopier.copy(this, tag);
            return tag;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    /**
     *  list
     * @param list 原数据
     * @param clazz 需要转变的类型
     * @return List<clazz>
     */
    public static <T, S extends SerializableVO> List<T> to(Collection<S> list, Class<T> clazz) {
        if (list != null && !list.isEmpty()) {
            List<T> result = new ArrayList(list.size());
            Iterator var3 = list.iterator();
            while(var3.hasNext()) {
                SerializableVO abs = (SerializableVO)var3.next();
                result.add((T) abs.to(clazz));
            }

            return result;
        } else {
            return new ArrayList();
        }
    }


    /**
     *  Iterable
     * @param iterable 原数据
     * @param clazz  需要转变的类型
     * @return List<clazz>
     */
    public static <T, S extends SerializableVO> List<T> to(Iterable<S> iterable, Class<T> clazz) {
        if (iterable !=null) {
            ArrayList result = new ArrayList();
            Iterator var3 = iterable.iterator();
            while(var3.hasNext()) {
                SerializableVO abs = (SerializableVO)var3.next();
                result.add((T) abs.to(clazz));
            }
            return result;
        } else {
            return new ArrayList();
        }
    }


    /**
     * bean转换
     * @param object 原数据
     * @param clazz 需要转变的类型
     * @return clazz
     */
    public static <T, S> T to(S object, Class<T> clazz) {
        if (object != null) {
            SerializableVO abs = (SerializableVO)object;
            return (T) abs.to(clazz);
        } else {
            return (T)clazz;
        }
    }


    /**
     * 两个空bean转换
     * @param clazzs 原来的
     * @param clazz  需要返回的
     * @return clazz
     */
    public static <T, S> T to(Class<S> clazzs, Class<T> clazz) {
        Class<? extends Class> aClass = clazzs.getClass();
        try {
            SerializableVO abs =(SerializableVO) clazzs.newInstance();
            return (T)abs.to(clazz);
        }catch (Exception e){
            return (T)clazz;
        }
    }
}
