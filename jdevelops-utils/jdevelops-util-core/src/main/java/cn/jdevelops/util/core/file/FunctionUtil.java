package cn.jdevelops.util.core.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
     *
     * @param functionName 方法（方法的参数类型一定要明确不能使用object）
     * @param args         方法参数
     * @return 值
     */
    public static <T> T executeDeclaredMethod(Class<?> clazz, String functionName, Object... args) {
        try {
            Method method;
            if (args != null) {
                // 获取方法对象
                Class<?>[] type = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Map) {
                        type[i] = Map.class;
                    } else if (args[i] instanceof List) {
                        type[i] = List.class;
                    } else {
                        type[i] = args[i].getClass();
                    }

                }
                method = clazz.getDeclaredMethod(functionName, type);
            } else {
                method = clazz.getDeclaredMethod(functionName);
            }
            // 设置方法的可访问性
            method.setAccessible(true);
            // 创建类的实例对象（如果是静态方法，可以省略此步骤）
            Object instance = clazz.newInstance();
            // 调用方法
            return (T) method.invoke(instance, args);
        } catch (Exception e) {
            LOG.error("执行关键词函数方法失败", e);
        }
        return null;
    }


    /**
     * 根据 方法名 执行方法
     *
     * @param javaFilePath java文件的根目录 e.g. H:\test\dist
     * @param javaFileName java文件名，包含扩展名 e.g. HelloWorld.java
     * @param className    类名 e.g. HelloWorld
     * @param functionName 方法（方法的参数类型一定要明确不能使用object）
     * @param args         方法参数
     * @return 值
     */
    public static <T> T executeDeclaredMethod(String javaFilePath, String javaFileName, String className, String functionName, Object... args) throws IOException, InterruptedException, ClassNotFoundException {

        // 创建自定义类加载器
        JdevelopsClassLoader classLoader = new JdevelopsClassLoader(javaFilePath);
        // 编译Java文件
        classLoader.compileJavaFile(javaFilePath + "\\" + javaFileName);
        // 加载类
        Class<?> loadedClass = classLoader.findClass(className);

        try {
            Method method;
            if (args != null) {
                // 获取方法对象
                Class<?>[] type = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Map) {
                        type[i] = Map.class;
                    } else if (args[i] instanceof List) {
                        type[i] = List.class;
                    } else {
                        type[i] = args[i].getClass();
                    }

                }
                method = loadedClass.getDeclaredMethod(functionName, type);
            } else {
                method = loadedClass.getDeclaredMethod(functionName);
            }
            // 设置方法的可访问性
            method.setAccessible(true);
            // 创建类的实例对象（如果是静态方法，可以省略此步骤）
            Object instance = loadedClass.newInstance();
            // 调用方法
            return (T) method.invoke(instance, args);
        } catch (Exception e) {
            LOG.error("执行关键词函数方法失败", e);
        }
        return null;
    }
}
