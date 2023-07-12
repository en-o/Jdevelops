package cn.jdevelops.util.core.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 自定义类加载器
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/711:34
 */
public class JdevelopsClassLoader  extends ClassLoader{

    /**
     * 类路径
     */
    private  String classPath;

    public JdevelopsClassLoader(String  classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            byte[] bytes = loadClassData(className);
            // 调用defineClass，将字节数组数据转换为Class实例
            return defineClass(null, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加载 class 数据
     */
    private byte[] loadClassData(String className) throws IOException {
        Path path = Paths.get(classPath, className.replace('.', '/') + ".class");
        return Files.readAllBytes(path);
    }

    /**
     * 编译java -> class
     * @param javaFilePath 文件路径
     */
    public void compileJavaFile(String javaFilePath) throws InterruptedException, IOException {
        Process process = Runtime.getRuntime().exec("javac " + javaFilePath);
        int result = process.waitFor();
        if (result != 0) {
            throw new RuntimeException("Failed to compile Java file");
        }
    }
}
