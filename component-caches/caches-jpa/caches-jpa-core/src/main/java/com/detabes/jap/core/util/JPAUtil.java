package com.detabes.jap.core.util;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * <p>
 *
 *         Set<String> requestDataAnd = new HashSet<>();
 *         Set<String> requestDataOr = new HashSet<>();
 *         Set<String> requestDataLike = new HashSet<>();
 *         Set<String> requestDataBetween = new HashSet<>();
 *         requestDataAnd.add("sex");
 *         requestDataLike.add("nickname");
 *         requestDataLike.add("remark");
 * //        requestDataBetween.add("nickname");
 * //        Specification beanByDynamic = new JpaUtils.Builder(new UserBean().setNickname("张")).setLikeAnd(requestDataLike).findBeanByDynamic();
 *
 * //        Specification beanByDynamic = new JpaUtils.Builder(new UserBean().setNickname("2,3")).setBetweenAnd(requestDataBetween).findBeanByDynamic();
 *         Specification beanByDynamic = new JpaUtils.Builder(new UserBean().setSex("1").setNickname("张").setRemark("测"))
 *                 .setAnd(requestDataAnd)
 * //                .setLikeAnd(requestDataLike)
 *                 .setLikeOr(requestDataLike)
 *                 .findBeanByDynamic();
 *         return userDao.findAll(beanByDynamic);
 * </p>
 *
 * @author tn
 * @version 1
 * @ClassName JPAUtil
 * @description jpa工具
 * @date 2020/6/17 15:27
 */
public class JPAUtil<T> {


    private final T bean;
    private final Set<String> and;
    private final Set<String> or;
    private final Set<String> likeOr;
    private final Set<String> likeAnd;
    private final Set<String> betweenOr;
    private final Set<String> betweenAnd;

    /**
     * 建造者模式
     * @param builder
     */
    private JPAUtil(Builder builder){
        this.bean= (T) builder.bean;
        this.and=builder.and;
        this.or=builder.or;
        this.likeOr = builder.likeOr;
        this.likeAnd = builder.likeAnd;
        this.betweenOr = builder.betweenOr;
        this.betweenAnd = builder.betweenAnd;
    }

    @Setter
    public static class Builder<T>{

        List<Predicate> predicatesAnd ;
        List<Predicate> predicatesOr;
//        List<Predicate> predicatesAnd = new ArrayList<>();
//        List<Predicate> predicatesOr = new ArrayList<>();
        /**
         * 必选
         */
        @Setter(AccessLevel.NONE)
        private T bean;
        //都是可选字段
        private  Set<String> and;
        private  Set<String> or;
        private  Set<String> likeOr;
        private  Set<String> likeAnd;
        private  Set<String> betweenOr;
        private  Set<String> betweenAnd;

        public Builder(T bean) {
            this.bean = bean;
        }
        public JPAUtil build(){
            return new JPAUtil(this);
        }


        public Specification<T> findBeanByDynamic() {
            //一切错误都踹了出去 ， 想知道结果为什么哪不对，请查看控制层输出的sql 或 进来打断点
            try {
                if(!isObjectEmpty(bean)) {
                    //实体类
                    Map<String, Object> entity = object2Map(bean);
                    @SuppressWarnings("serial")
                    Specification<T> spf =  new Specification<T>() {
                        @Override
                        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            //解决分页查询的冗余条件
                            predicatesAnd = new ArrayList<>();
                            predicatesOr = new ArrayList<>();
                            //获取 搜索条件  key 一定要和传进来的实体key一致
                            Iterator<String> entityIterator = entity.keySet().iterator();
                            while(entityIterator.hasNext()){
                                String ekey=entityIterator.next();//键
                                Object evalue = entity.get(ekey);//值
                                if(!isObjectEmpty(evalue)&&!"serialVersionUID".equals(ekey)) {
                                    disposeLisk(ekey,evalue,root,cb);
                                    disposeBetween(ekey,evalue,root,cb);
                                    disposeOr(ekey,evalue,root,cb);
                                    disposeAnd(ekey,evalue,root,cb);
                                }
                            }
                            Predicate predicateAnds = null;
                            Predicate predicateOrs = null;
                            if(!predicatesAnd.isEmpty()) {//and
                                predicateAnds = cb.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));
                                predicateAnds = cb.and(predicateAnds);
                            }
                            if(!predicatesOr.isEmpty()) {//or
                                predicateOrs = cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()]));
                                predicateOrs = cb.and(predicateOrs);
                            }
                            if(predicateOrs!=null&&predicateAnds!=null) {
                                return query.where(predicateAnds,predicateOrs).getRestriction();
                            }else {
                                if(predicateAnds != null ){
                                    return query.where(predicateAnds).getRestriction();
                                }else if(predicateOrs != null) {
                                    return query.where(predicateOrs).getRestriction();
                                }else {
                                    return null;
                                }
                            }
                        }
                    };
                    return (Specification<T>) spf;
                }else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        /**
         * 处理like
         *  like 需要 like 查询的键名集合
         * @param ekey   实体类的键
         * @param evalue 实体类的值（里面是 条件 参数）
         * @param root Root<T>
         * @param cb  CriteriaBuilder
         * @return
         */
        protected void disposeLisk(String ekey,Object evalue,Root<T> root, CriteriaBuilder cb){
            if(evalue instanceof String && ((String) evalue).contains(",")) {
                String[] split = (evalue+"").split(",");//分割
                if( likeOr!=null && likeOr.contains(ekey)) {
                    predicatesAnd.add(cb.like(root.get(ekey).as(String.class),"%"+split[0]+"%"));
                    predicatesAnd.add(cb.like(root.get(ekey).as(String.class),"%"+split[1]+"%"));
                }else if(likeAnd!=null && likeAnd.contains(ekey)){
                    predicatesOr.add(cb.like(root.get(ekey).as(String.class),"%"+split[0]+"%"));
                    predicatesOr.add(cb.like(root.get(ekey).as(String.class),"%"+split[1]+"%"));
                }
            }else{
                if( likeOr!=null && likeOr.contains(ekey)) {
                    predicatesOr.add(cb.like(root.get(ekey).as(String.class),"%"+evalue+"%"));
                }else if(likeAnd!=null && likeAnd.contains(ekey)){
                    predicatesAnd.add(cb.like(root.get(ekey).as(String.class),"%"+evalue+"%"));
                }
            }

        }

        /**
         * 处理 Between
         *  Between 需要 Between 查询的键名集合
         * @param ekey   实体类的键
         * @param evalue 实体类的值（里面是 条件 参数）
         * @param root Root<T>
         * @param cb  CriteriaBuilder
         * @return
         */
        protected void disposeBetween(String ekey,Object evalue,Root<T> root, CriteriaBuilder cb){

            if(evalue instanceof String && ((String) evalue).contains(",")) {
                String[] split = (evalue+"").split(",");//分割
                if(betweenOr!=null && betweenOr.contains(ekey)){
                    predicatesOr.add(cb.between(root.get(ekey).as(String.class),split[0], split[1]));
                }else if(betweenAnd!=null && betweenAnd.contains(ekey)){
                    predicatesAnd.add(cb.between(root.get(ekey).as(String.class),split[0], split[1]));
                }
            }
        }

        /**
         * 处理 and
         *  and 需要 and 查询的键名集合
         * @param ekey   实体类的键
         * @param evalue 实体类的值（里面是 条件 参数）
         * @param root Root<T>
         * @param cb  CriteriaBuilder
         * @return
         */
        protected void disposeAnd(String ekey,Object evalue,Root<T> root, CriteriaBuilder cb){
            if( and!=null && and.contains(ekey)) {
                if(evalue instanceof String && ((String) evalue).contains(",")) {
                    String[] split = (evalue+"").split(",");//分割
                    predicatesAnd.add(cb.equal(root.get(ekey).as(String.class),split[0]));
                    predicatesAnd.add(cb.equal(root.get(ekey).as(String.class),split[1]));
                }else {
                    predicatesAnd.add(cb.equal(root.get(ekey).as(String.class),evalue));
                }
            }
        }


        /**
         * 处理 or
         *  or 需要 or 查询的键名集合
         * @param ekey   实体类的键
         * @param evalue 实体类的值（里面是 条件 参数）
         * @param root Root<T>
         * @param cb  CriteriaBuilder
         * @return
         */
        protected void disposeOr(String ekey, Object evalue, Root<T> root, CriteriaBuilder cb){
            if(or!=null && or.contains(ekey)){
                if(evalue instanceof String && ((String) evalue).contains(",")) {
                    String[] split = (evalue+"").split(",");//分割
                    predicatesOr.add(cb.equal(root.get(ekey).as(String.class),split[0]));
                    predicatesOr.add(cb.equal(root.get(ekey).as(String.class),split[1]));
                }else {
                    predicatesOr.add(cb.equal(root.get(ekey).as(String.class),evalue));
                }
            }
        }

    }



    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return {Map}
     */
    private static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>(16);
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }



    private static boolean isObjectEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return isStrEmpty((CharSequence)obj);
        } else if (obj instanceof Map) {
            return isMapEmpty((Map)obj);
        } else if (obj instanceof Iterable) {
            return isIterEmpty((Iterable)obj);
        } else if (obj instanceof Iterator) {
            return isIterEmpty2((Iterator)obj);
        } else {
            return isArray(obj) ? isArrayEmpty(obj) : false;
        }
    }

    private static boolean isStrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private static boolean isMapEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    private static boolean isIterEmpty(Iterable<?> iterable) {
        return null == iterable || isIterEmpty2(iterable.iterator());
    }
    private static boolean isIterEmpty2(Iterator<?> iterator) {
        return null == iterator || !iterator.hasNext();
    }
    private static boolean isArray(Object obj) {
        return null == obj ? false : obj.getClass().isArray();
    }
    private static boolean isArrayEmpty(Object array) {
        if (null == array) {
            return true;
        } else if (isArray(array)) {
            return 0 == Array.getLength(array);
        } else {
//            throw new Exception("Object to provide is not a Array !");
            return false;
        }
    }
}
