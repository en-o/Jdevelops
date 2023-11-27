package cn.jdevelops.util.aops;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Aop 相关反射工具
 *
 * @author tn
 * @version 1
 * @date 2020/4/22 10:35
 */
public class AopReasolver {

    private static final Logger LOG = LoggerFactory.getLogger(AopReasolver.class);

    private static AopReasolver resolver;

    public static AopReasolver newInstance() {

        if (resolver == null) {
            return resolver = new AopReasolver();
        } else {
            return resolver;
        }

    }

    /**
     * 该方法的作用可以把方法上的参数绑定到注解的变量中,注解的语法#{变量名}
     * 能解析类似#{task}或者#{task.taskName}或者{task.project.projectName}
     * 需要引入 aop依赖
     * 解析注解上的值
     *
     * @param joinPoint aop
     * @param str       需要解析的字符串
     */
    public Object resolver(JoinPoint joinPoint, String str) {

        try {
            if (StringUtil.isBlank(str)) {
                return null;
            }

            Object value;
            String substring1 = null;
            String substring2;

            if (str.contains("#")) {
                substring1 = str.substring(0, str.indexOf("#"));
                substring2 = str.substring(str.indexOf("#"));
                // 如果name匹配上了#{},则把内容当作变量
                if (substring2.matches("#\\{\\D*}")) {
                    String newStr = substring2.replaceAll("#\\{", "").replaceAll("}", "");
                    // 复杂类型
                    if (newStr.contains(".")) {
                        value = complexResolver(joinPoint, newStr);
                    } else {
                        value = simpleResolver(joinPoint, newStr);
                    }
                } else { //非变量
                    value = str;
                }
            } else {
                value = str;
            }

            return StringUtil.isBlank(substring1) ? value : substring1 + value;
        } catch (Exception e) {
            LOG.error("解析参数的值失败", e);
        }
        return null;
    }


    /**
     * 解析参数的值
     * @param joinPoint JoinPoint
     * @param str 参数
     * @return 值
     */
    private Object complexResolver(JoinPoint joinPoint, String str) {

        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

            String[] names = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            String[] stars = str.split("\\.");
            for (int i = 0; i < names.length; i++) {
                if (stars[0].equals(names[i])) {
                    Object obj = args[i];
                    if (obj instanceof Map) {
                        return getValueByMap(obj, stars);
                    } else {
                        Method dmethod = obj.getClass().getDeclaredMethod(getMethodName(stars[1]), new Class[0]);
                        Object value = dmethod.invoke(args[i]);
                        return getValueByBean(value, 1, stars);
                    }

                }
            }
        } catch (Exception e) {
            LOG.error("解析参数的值失败", e);
        }

        return null;

    }


    private Object getValueByMap(Object obj, String[] strs) {
        try {
            Map map = (Map) obj;
            if (map.containsKey(strs[1])) {
                return map.get(strs[1]);
            }
            return obj;
        } catch (Exception e) {
            LOG.error("getValueByMap失败", e);
        }
        return null;
    }

    private Object getValueByBean(Object obj, int index, String[] strs) {

        try {
            if (obj != null && index < strs.length - 1) {
                Method method = obj.getClass().getDeclaredMethod(getMethodName(strs[index + 1]), (Class<?>) null);
                obj = method.invoke(obj);
                getValueByBean(obj, index + 1, strs);
            }

            return obj;

        } catch (Exception e) {
            LOG.error("getValueByBean失败", e);
            return null;
        }
    }

    private String getMethodName(String name) {
        return "get" + name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }


    private Object simpleResolver(JoinPoint joinPoint, String str) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] names = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < names.length; i++) {
            if (str.equals(names[i])) {
                return args[i];
            }
        }
        return null;
    }

}
