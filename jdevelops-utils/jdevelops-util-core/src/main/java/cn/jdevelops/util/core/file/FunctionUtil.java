package cn.jdevelops.util.core.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author tan
 * @date 2023/7/513:10
 */

public class FunctionUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionUtil.class);

    /**
     * 根据 方法名 执行方法
     * @param functionName 方法（方法的参数类型一定要明确不能使用object）
     * @param args 方法参数
     * @return 值
     */
    public static <T> T executeDeclaredMethod(Class<?> clazz, String functionName, Object... args){
       try {
           Method method;
           if(args != null){
               // 获取方法对象
               Class<?>[] type = new Class[args.length];
               for (int i = 0; i < args.length; i++) {
                   if(args[i] instanceof Map){
                       type[i] = Map.class;
                   } else if (args[i] instanceof List) {
                       type[i] = List.class;
                   } else {
                       type[i] = args[i].getClass();
                   }

               }
               method = clazz.getDeclaredMethod(functionName,type);
           }else {
               method = clazz.getDeclaredMethod(functionName);
           }
           // 设置方法的可访问性
           method.setAccessible(true);
           // 创建类的实例对象（如果是静态方法，可以省略此步骤）
           Object instance = clazz.newInstance();
           // 调用方法
           return (T) method.invoke(instance, args);
       }catch (Exception e){
           LOG.error("执行关键词函数方法失败",e);
       }
       return null;
    }
}
