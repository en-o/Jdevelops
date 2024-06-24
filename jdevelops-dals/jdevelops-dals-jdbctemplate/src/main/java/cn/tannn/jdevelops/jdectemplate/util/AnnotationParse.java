package cn.tannn.jdevelops.jdectemplate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aop  解析注解字段
 *
 * @author tn
 * @version 1
 * @date 2020/4/22 10:35
 */
public class AnnotationParse {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationParse.class);


    private static AnnotationParse resolver;

    public static AnnotationParse newInstance() {

        if (resolver == null) {
            return resolver = new AnnotationParse();
        } else {
            return resolver;
        }

    }

    /**
     * 该方法的作用可以把方法上的参数绑定到注解的变量中,注解的语法#{变量名}
     * 能解析类似#{task}或者#{task.taskName}
     * 需要引入 aop依赖
     * 解析注解上的值
     *
     * @param method 执行的方法
     * @param args   方法里的参数
     * @param str    需要解析的字符串
     */
    public Object resolver(Method method, Object[] args, String str) {

        if (Objects.isNull(str) || str.length() <= 0) {
            return null;
        }
        String valueStre = str;

        if (str.contains("#")) {
            //抽取 #{xxx}
            String regex = "#\\{([^}]*)\\}";
            Pattern comp = Pattern.compile(regex);
            Matcher mat = comp.matcher(str);
            while (mat.find()) {
                String group = mat.group();
                // 如果name匹配上了#{},则把内容当作变量
                if (group.matches("#\\{\\D*}")) {
                    String newStr = group.replaceAll("#\\{", "").replaceAll("}", "");
                    // 复杂类型
                    if (newStr.contains(".")) {
                        try {
                            Object complexResolver = complexResolver(method, args, newStr);
                            if (Objects.nonNull(complexResolver)) {
                                valueStre = valueStre.replace(group, complexResolver.toString());
                            }
                        } catch (Exception e) {
                            LOG.error("需要解析的字符串失败", e);
                        }
                    } else {
                        Object simpleResolver = simpleResolver(method, args, newStr);
                        if (Objects.nonNull(simpleResolver)) {
                            valueStre = valueStre.replace(group, simpleResolver.toString());
                        }
                    }
                }
            }
        }
        return valueStre;
    }


    /**
     * 获取 #{xxx.xx}的值
     *
     * @param method 执行的方法
     * @param args   方法里的参数
     * @param str    需要至的字段名
     * @return #{xxx.xx}的值（从方法参数中来
     * @throws Exception Exception
     */
    private Object complexResolver(Method method, Object[] args, String str) throws Exception {

        Parameter[] parameters = method.getParameters();
        String[] stars = str.split("\\.");
        for (int i = 0; i < parameters.length; i++) {
            if (stars[0].equals(parameters[i].getName())) {
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

    /**
     * 获取 #{xxx.xx}的值
     *
     * @param obj   参数对象
     * @param index 1（默认
     * @param strs  #{xxx.xx}
     * @return 值
     */
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

    /**
     * 获取get方法
     *
     * @param name 字段名
     * @return getName
     */
    private String getMethodName(String name) {
        return "get" + name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }


    /**
     * 获取 #{xxx}的值
     *
     * @param method 执行的方法
     * @param args   方法里的参数
     * @param str    需要至的字段名
     * @return #{xxx}的值
     */
    private Object simpleResolver(Method method, Object[] args, String str) {
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (str.equals(parameters[i].getName())) {
                return args[i];
            }
        }
        return null;
    }


}
