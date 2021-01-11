package com.detabes.spring.core.jar;


import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.SecureUtil;
import com.detabes.http.core.MacUtil;
import com.detabes.spring.entity.JarAddFile;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author tn
 * @ClassName JarUtil
 * @description jar包相关工具类
 * @date 2021-01-11 10:17
 */
public class JarUtil {


    static String property = System.getProperty("user.home");


    /**
     *  jar绑定本机运行（复制到其他机器上就无法运行了） - 测试通过了linux（windows还无法实现）
     *  启动后都配置读不到，直接在jar中生产文件。
     *  因为第二次启动就不会出现读不到的问题！
     * @return
     * @throws IOException
     */
    public static String bindingMachine() throws Exception {
        String jarPath = JarUtil.getJarPath();
        String macAddress = MacUtil.getMacAddress();
        //文件读取
        try {
            // 查询项目中是否有这个文件
            String str = ResourceUtil.readUtf8Str("test.txt");
            if (null != str && str.length() > 0) {
                if (SecureUtil.md5(macAddress).equals(str)) {
                    return "jar授权失败!";
                }
            }
        } catch (Exception e) {
            if (null != macAddress && macAddress.trim().length() > 0) {
                //创建文件
                List<JarAddFile> jarAddFiles = new ArrayList<>();

                FileWriter writer = new FileWriter(property + "/test.txt");
                File file = writer.write(SecureUtil.md5(macAddress));
                //判断是部署启动
                String isJarUp = JarUtil.class.getResource("AstrictJarApplication.class").toString();
                String substring = isJarUp.substring(0, isJarUp.indexOf(":"));
                if ("jar".equalsIgnoreCase(substring)) {
                    JarAddFile jarAddFile = new JarAddFile();
                    jarAddFile.setFilesToAdd(file);
                    jarAddFile.setFilesToAddRelativePath(MacUtil.win2Linux("BOOT-INF/classes/"));
                    jarAddFiles.add(jarAddFile);
                    JarUtil.updateJarFile(new File(jarPath), true, jarAddFiles);
                }
                return "jar授权成功!";
            }
        }
        throw new RuntimeException("当前jar未经授权!");
    }


    /**
     *  jar运行后网jar中新增文件
     *
     * @param srcJarFile jar地址
     * @param update 是否是更新
     * @param filesToAdd 添加的文件
     * @throws IOException
     */
    public static void updateJarFile(File srcJarFile, boolean update, List<JarAddFile> filesToAdd) throws IOException {

        File tmpJarFile = File.createTempFile("tempJar", ".tmp");
        JarFile jarFile = new JarFile(srcJarFile);
        boolean jarUpdated = false;
        List<String> fileNames = new ArrayList<String>();

        try {
            JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tmpJarFile));
            try {
                // Added the new files to the jar.
                for (int i = 0; i < filesToAdd.size(); i++) {
                    File file = filesToAdd.get(i).getFilesToAdd();
                    String fileRelativePath = filesToAdd.get(i).getFilesToAddRelativePath();
                    FileInputStream fis = new FileInputStream(file);
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        JarEntry entry = new JarEntry(fileRelativePath+file.getName());
                        fileNames.add(entry.getName());
                        tempJarOutputStream.putNextEntry(entry);
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            tempJarOutputStream.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        fis.close();
                    }
                }

                // Copy original jar file to the temporary one.
                Enumeration<?> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) jarEntries.nextElement();
                    /*
                     * Ignore classes from the original jar which are being
                     * replaced
                     */
                    String[] fileNameArray = (String[]) fileNames
                            .toArray(new String[0]);
                    Arrays.sort(fileNameArray);
                    if (Arrays.binarySearch(fileNameArray, entry.getName()) < 0) {
                        InputStream entryInputStream = jarFile
                                .getInputStream(entry);
                        tempJarOutputStream.putNextEntry(entry);
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = entryInputStream.read(buffer)) != -1) {
                            tempJarOutputStream.write(buffer, 0, bytesRead);
                        }
                    } else if (!update) {
                        throw new IOException(
                                "Jar Update Aborted: Entry "
                                        + entry.getName()
                                        + " could not be added to the jar"
                                        + " file because it already exists and the update parameter was false");
                    }
                }

                jarUpdated = true;
            } catch (Exception ex) {
                System.err.println("Unable to update jar file");
                tempJarOutputStream.putNextEntry(new JarEntry("stub"));
            } finally {
                tempJarOutputStream.close();
            }

        } finally {
            jarFile.close();
            if (!jarUpdated) {
                tmpJarFile.delete();
            }
        }
        if (jarUpdated) {
            srcJarFile.delete();
            tmpJarFile.renameTo(srcJarFile);
        }

    }

    /**
     * 获取jar的路径 （war包没测试）
     * @return
     */
    public static String getJarPath() {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String os = System.getProperty("os.name");
        if(path.contains("!")) {
            path = path.substring(0,path.indexOf("!"));
        }
        String substring = path.substring(0,path.indexOf(":"));
        if("jar".equalsIgnoreCase(substring)){
            path = path.substring(path.indexOf(":")+1);
        }
        if ("file".equalsIgnoreCase(substring)) {
            path = path.substring(path.indexOf(":")+1);
        }
        if(os.toLowerCase().startsWith("win")){
            path = path.substring(1).replace("/","\\");
        }
        return path;
    }
}
