package cn.jdevelops.retrun.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cn.jdevelops.retrun.annotation.JpaReturn;
import cn.jdevelops.string.StringFormat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;


/**
 * 修改方法的返回值
 * @author tn
 * @version 1
 * @date 2020/4/30 2:32
 */

@Aspect
@Component
@Lazy(false)
public class JpaReturnAop {

    /**
     * 设置jpa注解为切入点
     *环绕通知：灵活自由的在目标方法中切入代码
     *@param pjp pjp
     */
    @Around(value="@annotation(cn.jdevelops.retrun.annotation.JpaReturn)")
    public Object doAfterReturning(ProceedingJoinPoint pjp) throws Throwable {
        //指定方法 获取返回值
        Object rvt = pjp.proceed();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*reBean 返回值类型*/
        JpaReturn reBean = method.getAnnotation(JpaReturn.class);
        if(rvt!=null){
            if(rvt instanceof Map){
                Map<String, Object> map = new HashMap<>((Map<String, Object>)rvt);
                return JSON.parseObject(JSON.toJSONString(map,
                        SerializerFeature.WriteNullStringAsEmpty,
                        SerializerFeature.WriteNullNumberAsZero,
                        SerializerFeature.WriteMapNullValue), (Type) reBean.reBean());
            }else if(rvt instanceof List){
                List<Map<String, Object>> tmap = new ArrayList<>();
                for (Map<String, Object> item : (List<Map<String, Object>>)rvt) {
                    Map<String, Object> newMap = new HashMap<>(item);
                    Iterator<String> iter = item.keySet().iterator();//通过迭代器获取key，vale
                    while(iter.hasNext()) {
                        String key=iter.next();
                        String newkey = StringFormat.toCamelCase(key, true);
                        newMap.put(newkey,newMap.remove(key));
                    }
                    tmap.add(newMap);
                }
                if(reBean != null){
                    //改变返回值
                return changeMap(tmap,reBean.reBean());
                }
                return tmap;
            }

        }
        return rvt;
    }


    /**
     * @author lmz
     * @param list list
     * @return t
     */
    public static <T> List<T> changeMap(List<Map<String, Object>> list, T t){
        List<T> list1=new ArrayList<>();
        list.forEach(map -> {
            T o = JSON.parseObject(JSON.toJSONString(map,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteMapNullValue), (Type) t);
            list1.add(o);
        });


        return  list1;
    }


}
