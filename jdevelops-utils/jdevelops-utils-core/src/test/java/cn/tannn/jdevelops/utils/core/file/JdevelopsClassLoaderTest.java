package cn.tannn.jdevelops.utils.core.file;//package cn.jdevelops.util.core.file;
//
//import junit.framework.TestCase;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//public class JdevelopsClassLoaderTest extends TestCase {
//
//    public void testClassLoader() throws IOException, InterruptedException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        String javaFilePath = "H:\\test\\dist";
//        String javaFile = "HelloWorld.java";
//        String className = "HelloWorld";
//        // 创建自定义类加载器
//        JdevelopsClassLoader classLoader = new JdevelopsClassLoader(javaFilePath);
//        // 编译Java文件
//        classLoader.compileJavaFile(javaFilePath+"\\"+javaFile);
//        // 加载类
//        Class<?> loadedClass = classLoader.findClass(className);
//        System.out.println("此class的类加载器为：" + loadedClass.getClassLoader());
//        System.out.println("此class的类加载器父类为：" + loadedClass.getClassLoader().getParent());
//
//
//        assertEquals(FunctionUtil.executeDeclaredMethod(loadedClass, "test"), "function");
//        assertEquals(FunctionUtil.executeDeclaredMethod(loadedClass, "test2", "hi"), "function String => hi");
//        assertEquals(FunctionUtil.executeDeclaredMethod(loadedClass, "test3", 1), "function Integer => 1");
//
//        // 以下是执行main方法
//        Object o = loadedClass.newInstance();
//        Method[] declaredMethods = loadedClass.getDeclaredMethods();
//        for (Method declaredMethod : declaredMethods) {
//            System.out.println(declaredMethod.getName());
//            if("main".equals(declaredMethod.getName())){
//                System.out.println("执行main方法");
//                declaredMethod.invoke(o, new String[1]);
//            }
//        }
//    }
//
//}
