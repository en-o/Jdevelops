package com.detabes.retrun.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.detabes.map.core.map.MapUtil;
import com.detabes.string.StringFormat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;


/**
 * @author tn
 * @version 1
 * @ClassName JpaAop
 * @description  修改方法的返回值
 * @date 2020/4/30 2:32
 */

@Aspect
@Component
@Lazy(false)
public class ReturnToCamlCaseAop {

    /**
     * 设置jpa注解为切入点
     *环绕通知：灵活自由的在目标方法中切入代码
     *@paramjoinPoint
     */
    @Around(value="@annotation(com.detabes.retrun.annotation.ReturnToCamlCase)")
    public Object doAfterReturning(ProceedingJoinPoint pjp) throws Throwable {
        //指定方法 获取返回值
        Object rvt = pjp.proceed();
        if(rvt!=null){
            if(rvt instanceof Map){
                Map<String, Object> beanToMap = MapUtil.beanToMap(rvt);
                beanToMap.forEach((str,value) ->{
                    System.err.println(str+":"+value);});
            }else if(rvt instanceof List){
                List<Map<String, Object>> tmap = new ArrayList<>();
                for (Map item : (List<Map>)rvt) {
                    Map<String, Object> newMap = new HashMap<>(item);
                    Iterator<String> iter = item.keySet().iterator();//通过迭代器获取key，vale
                    while(iter.hasNext()) {
                        String key=iter.next();
                        String newkey = StringFormat.toCamelCase(key.toString(), true);
                        newMap.put(newkey,newMap.remove(key));
                    }
                    tmap.add(newMap);
                }
                //改变返回值
//                return changeMap(tmap,new Rvo());
                return tmap;
            }

        }
        return rvt;
    }


    /**
     *@Author: lmz
     *@Company: Peter
     *@Date: 2020/4/29  13:29
     *@Description:  将 List<Map<String,Object>> 转为对应的实体对象
     @param list list
     *@Return: java.util.List<T>
     */
    public static <T> List<T> changeMap(List<Map<String, Object>> list, T t){
        List<T> list1=new ArrayList<>();
        list.forEach(map -> {
            T o = JSON.parseObject(JSON.toJSONString(map,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue),(Type) t.getClass());
            list1.add(o);
        });


        return  list1;
    }


}
