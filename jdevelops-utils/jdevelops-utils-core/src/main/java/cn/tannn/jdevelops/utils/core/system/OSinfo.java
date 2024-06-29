package cn.tannn.jdevelops.utils.core.system;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取OS数据
 *  <pre>
 *
 *    public static void main(String[] args) {
 *        System.out.println(OSinfo.getOSname());// 获取系统类型
 *        System.out.println(OSinfo.isWindows());// 判断是否为windows系统
 //    }
 *  </pre>
 * @author 来源于网络
 *  <a href="https://blog.csdn.net/fangchao2011/article/details/88785637">参考</a>
 */
public class OSinfo {

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final Logger LOG = LoggerFactory.getLogger(OSinfo.class);
    private static final String WIN="win";
    private static final String WINDOWS="Windows";
    private static final String LINUX="Linux";
    private static final OSinfo INSTANCE = new OSinfo();
    private static final String[] WINDOWS_COMMAND = { "ipconfig", "/all" };
    private static final String[] LINUX_COMMAND = { "/sbin/ifconfig", "-a" };
    private static final Pattern MAC_PATTERN = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
            Pattern.CASE_INSENSITIVE);
    private static String macAddressStr = null;
    private EPlatform platform;

    private OSinfo() {
    }

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMacOs() {
        return OS.contains("mac") && OS.contains("os") && !OS.contains("x");
    }

    public static boolean isMacOsX() {
        return OS.contains("mac") && OS.contains("os") && OS.contains("x");
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isOs2() {
        return OS.contains("os/2");
    }

    public static boolean isSolaris() {
        return OS.contains("solaris");
    }

    public static boolean isSunOs() {
        return OS.contains("sunos");
    }

    public static boolean ismpeix() {
        return OS.contains("mpe/ix");
    }

    public static boolean ishpux() {
        return OS.contains("hp-ux");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isos390() {
        return OS.contains("os/390");
    }

    public static boolean isfreebsd() {
        return OS.contains("freebsd");
    }

    public static boolean isIrix() {
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix() {
        return OS.contains("digital") && OS.contains("unix");
    }

    public static boolean isNetWare() {
        return OS.contains("netware");
    }

    public static boolean isosf1() {
        return OS.contains("osf1");
    }

    public static boolean isOpenVms() {
        return OS.contains("openvms");
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static EPlatform getOsName() {
        if (isAix()) {
            INSTANCE.platform = EPlatform.AIX;
        } else if (isDigitalUnix()) {
            INSTANCE.platform = EPlatform.DIGITAL_UNIX;
        } else if (isfreebsd()) {
            INSTANCE.platform = EPlatform.FREE_BSD;
        } else if (ishpux()) {
            INSTANCE.platform = EPlatform.HP_UX;
        } else if (isIrix()) {
            INSTANCE.platform = EPlatform.IRIX;
        } else if (isLinux()) {
            INSTANCE.platform = EPlatform.LINUX;
        } else if (isMacOs()) {
            INSTANCE.platform = EPlatform.MAC_OS;
        } else if (isMacOsX()) {
            INSTANCE.platform = EPlatform.MAC_OS_X;
        } else if (ismpeix()) {
            INSTANCE.platform = EPlatform.MP_EI_X;
        } else if (isNetWare()) {
            INSTANCE.platform = EPlatform.NET_WARE_411;
        } else if (isOpenVms()) {
            INSTANCE.platform = EPlatform.OPEN_VMS;
        } else if (isOs2()) {
            INSTANCE.platform = EPlatform.OS_2;
        } else if (isos390()) {
            INSTANCE.platform = EPlatform.OS390;
        } else if (isosf1()) {
            INSTANCE.platform = EPlatform.OSF1;
        } else if (isSolaris()) {
            INSTANCE.platform = EPlatform.SOLARIS;
        } else if (isSunOs()) {
            INSTANCE.platform = EPlatform.SUN_OS;
        } else if (isWindows()) {
            INSTANCE.platform = EPlatform.WINDOWS;
        } else {
            INSTANCE.platform = EPlatform.OTHERS;
        }
        return INSTANCE.platform;
    }

    /**
     *  win separator Linux 相互转换
     * @param path 地址
     * @return String
     */
    public static String win2Linux(String path) {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(WIN)){
            path = path.replace("/","\\");
        }else {
            path = path.replace("\\","/");
        }
        return path;
    }


    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     *
     * @return  String
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.isEmpty()) {
            // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            StringBuilder sb = new StringBuilder();
            try {
                List<String> macList = getMacAddressList();
                for (String amac : macList) {
                    if (!"0000000000E0".equals(amac)) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException e) {
                LOG.error("获取一个网卡地址失败", e);
            }

            macAddressStr = sb.toString();

        }

        return macAddressStr;
    }

    /**
     * 获取多个网卡地址
     *
     * @return List
     */
    public static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<>();
        final String os = System.getProperty("os.name");
        String[] command;

        if (os.startsWith(WINDOWS)) {
            command = WINDOWS_COMMAND;
        } else if (os.startsWith(LINUX)) {
            command = LINUX_COMMAND;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        // 执行命令
        final Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (String line; (line = bufReader.readLine()) != null;) {
            Matcher matcher = MAC_PATTERN.matcher(line);
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1));
                // macAddressList.add(matcher.group(1).replaceAll("[-:]",
                // ""));//去掉MAC中的“-”
            }
        }

        process.destroy();
        bufReader.close();
        return macAddressList;
    }
}
