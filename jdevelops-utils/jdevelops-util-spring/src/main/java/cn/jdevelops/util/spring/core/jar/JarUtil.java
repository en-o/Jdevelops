package cn.jdevelops.util.spring.core.jar;


import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.SecureUtil;
import cn.jdevelops.util.http.MacUtil;
import cn.jdevelops.util.spring.constant.JarConstant;
import cn.jdevelops.util.spring.core.system.OSinfo;
import cn.jdevelops.util.spring.entity.JarAddFile;
import cn.jdevelops.util.spring.enums.EPlatform;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.springframework.util.ClassUtils.*;

/**
 * jar包相关工具类
 * @author tn
 * @date 2021-01-11 10:17
 */
public class JarUtil {


    static String property = System.getProperty("user.home");

    /**
     * 设置不允许启动的系统平台
     * 默认全部都可以
     *
     * @param os 1：windows 2：Linux 3: mac
     */
    public static void bindingMachine(List<EPlatform> os) throws Exception {
        // 当前系统
        EPlatform oSname = OSinfo.getOsName();
        if (os.contains(oSname)) {
            throw new RuntimeException(JarConstant.SYSTEM_NO_USE_MESSAGE);
        }
        bindingMachine();
    }


    /**
     * jar绑定本机运行（复制到其他机器上就无法运行了） - 测试通过了linux（windows还无法实现）
     * 启动后都配置读不到，直接在jar中生产文件。
     * 因为第二次启动就不会出现读不到的问题！
     *
     * @return String
     */
    public static String bindingMachine() throws Exception {
        String jarPath = JarUtil.getJarPath();
        String macAddress = MacUtil.getMacAddress();
        //文件读取
        try {
            // 查询项目中是否有这个文件
            String str = ResourceUtil.readUtf8Str(JarConstant.SYSTEM_MAC_FILE);
            if (null != str && str.length() > 0) {
                if (SecureUtil.md5(macAddress).equals(str)) {
                    return JarConstant.JAR_ERROPR_MESSAGE;
                }
            }
        } catch (Exception e) {
            if (null != macAddress && macAddress.trim().length() > 0) {
                //创建文件
                List<JarAddFile> jarAddFiles = new ArrayList<>();

                FileWriter writer = new FileWriter(property + JarConstant.SYSTEM_MAC_FILE_PATH);
                File file = writer.write(SecureUtil.md5(macAddress));
                //判断是部署启动
                String isJarUp = Objects.requireNonNull(JarUtil.class.getResource("AstrictJarApplication.class")).toString();
                String substring = isJarUp.substring(0, isJarUp.indexOf(":"));
                if (JarConstant.JAR.equalsIgnoreCase(substring)) {
                    JarAddFile jarAddFile = new JarAddFile();
                    jarAddFile.setFilesToAdd(file);
                    jarAddFile.setFilesToAddRelativePath(MacUtil.win2Linux("BOOT-INF/classes/"));
                    jarAddFiles.add(jarAddFile);
                    JarUtil.updateJarFile(new File(jarPath), true, jarAddFiles);
                }
                return JarConstant.JAR_SUCCESS_MESSAGE;
            }
        }
        throw new RuntimeException(JarConstant.NO_PERMISSION_MESSAGE);
    }


    /**
     * jar运行后网jar中新增文件
     *
     * @param srcJarFile jar地址
     * @param update     是否是更新
     * @param filesToAdd 添加的文件
     * @throws IOException e
     * @see <a href="https://cloud.tencent.com/developer/ask/137977">感谢</a>
     */
    public static void updateJarFile(File srcJarFile, boolean update, List<JarAddFile> filesToAdd) throws IOException {

        File tmpJarFile = File.createTempFile(JarConstant.TEMP_JAR, ".tmp");
        JarFile jarFile = new JarFile(srcJarFile);
        boolean jarUpdated = false;
        List<String> fileNames = new ArrayList<>();

        try {
            JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tmpJarFile));
            try {
                // Added the new files to the jar.
                for (int i = 0; i < filesToAdd.size(); i++) {
                    File file = filesToAdd.get(i).getFilesToAdd();
                    String fileRelativePath = filesToAdd.get(i).getFilesToAddRelativePath();
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        JarEntry entry = new JarEntry(fileRelativePath + file.getName());
                        fileNames.add(entry.getName());
                        tempJarOutputStream.putNextEntry(entry);
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            tempJarOutputStream.write(buffer, 0, bytesRead);
                        }
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
                    String[] fileNameArray = fileNames
                            .toArray(new String[0]);
                    Arrays.sort(fileNameArray);
                    if (Arrays.binarySearch(fileNameArray, entry.getName()) < 0) {
                        InputStream entryInputStream = jarFile
                                .getInputStream(entry);
                        tempJarOutputStream.putNextEntry(entry);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
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
     *
     * @return String
     */
    public static String getJarPath() {
        String path = getDefaultClassLoader().getResource("").getPath();
        String os = System.getProperty("os.name");
        if (path.contains(JarConstant.GAN_TAN)) {
            path = path.substring(0, path.indexOf("!"));
        }
        String substring = path.substring(0, path.indexOf(":"));
        if (JarConstant.JAR.equalsIgnoreCase(substring)) {
            path = path.substring(path.indexOf(JarConstant.MAO_HAO) + 1);
        }
        if (JarConstant.FILE.equalsIgnoreCase(substring)) {
            path = path.substring(path.indexOf(JarConstant.MAO_HAO) + 1);
        }
        if (os.toLowerCase().startsWith(JarConstant.WIN)) {
            path = path.substring(1).replace(JarConstant.XIE_GANG, "\\");
        }
        return path;
    }
}
