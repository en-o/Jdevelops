package com.detabes.map.core.bean;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 作者 tn
 * @version 创建时间：2018年11月13日
 * 类说明   复制bean
 */
public class BeanCopyUtil {


    /**
     *source中的非空属性复制到target中
     *
     *   public void copy(UserMsgSubscribeBean source) {
     *     BeanCopyUtil.beanCopy(source, this);
     *   }
     *
     *          Bean getIdDate = regulationDao.findOne(ParamBean.getId());//跟根据ID获取需要更新的数据的 原始数据
     * 			getIdDate.systemRuleBeanCopy(ParamBean);//将新数据中非空字段 克隆到原始数据中 实现更新
     * 			regulationDao.saveAndFlush(getIdDate);//保存克隆之后的数据  且 saveAndFlush立即生效
     *
     * @param source 源
     * @param target 目标
     */
    @SuppressWarnings("AlibabaRemoveCommentedCode")
    public static <T> void beanCopy(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * source中的非空属性复制到target中，但是忽略指定的属性，也就是说有些属性是不可修改的（个人业务需要）
     * @param source  source
     * @param target target
     * @param ignoreProperties ignoreProperties
     * @param <T> t
     */
    public static <T> void beanCopyWithIngore(T source, T target, String... ignoreProperties) {
        String[] pns = getNullAndIgnorePropertyNames(source, ignoreProperties);
        BeanUtils.copyProperties(source, target, pns);
    }
    public static String[] getNullAndIgnorePropertyNames(Object source, String... ignoreProperties) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        emptyNames.addAll(Arrays.asList(ignoreProperties));
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
    public static String[] getNullPropertyNames(Object source) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
    public static Set<String> getNullPropertyNameSet(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }



    /**
     * 复制bean
     * @param tClass 需求bean
     * @param t 被复制的bean
     * @return tClass
     */
    public static <T> T to(Class<T> tClass,Object t){
        try {
            T tag = tClass.newInstance();
            BeanCopyUtil.beanCopy(t, tag);
            return tag;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}
