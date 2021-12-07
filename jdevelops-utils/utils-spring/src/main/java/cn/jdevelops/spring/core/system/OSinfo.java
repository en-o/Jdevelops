package cn.jdevelops.spring.core.system;

import cn.jdevelops.spring.enums.EPlatform;

/**
 * 获取OS数据
 *
 * @link : https://blog.csdn.net/fangchao2011/article/details/88785637
 */
public class OSinfo {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static OSinfo _instance = new OSinfo();

    private EPlatform platform;

    private OSinfo() {
    }

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMacOS() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && !OS.contains("x");
    }

    public static boolean isMacOSX() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isOS2() {
        return OS.contains("os/2");
    }

    public static boolean isSolaris() {
        return OS.contains("solaris");
    }

    public static boolean isSunOS() {
        return OS.contains("sunos");
    }

    public static boolean isMPEiX() {
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX() {
        return OS.contains("hp-ux");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isOS390() {
        return OS.contains("os/390");
    }

    public static boolean isFreeBSD() {
        return OS.contains("freebsd");
    }

    public static boolean isIrix() {
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix() {
        return OS.contains("digital") && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.contains("netware");
    }

    public static boolean isOSF1() {
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS() {
        return OS.contains("openvms");
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static EPlatform getOSname() {
        if (isAix()) {
            _instance.platform = EPlatform.AIX;
        } else if (isDigitalUnix()) {
            _instance.platform = EPlatform.Digital_Unix;
        } else if (isFreeBSD()) {
            _instance.platform = EPlatform.FreeBSD;
        } else if (isHPUX()) {
            _instance.platform = EPlatform.HP_UX;
        } else if (isIrix()) {
            _instance.platform = EPlatform.Irix;
        } else if (isLinux()) {
            _instance.platform = EPlatform.Linux;
        } else if (isMacOS()) {
            _instance.platform = EPlatform.Mac_OS;
        } else if (isMacOSX()) {
            _instance.platform = EPlatform.Mac_OS_X;
        } else if (isMPEiX()) {
            _instance.platform = EPlatform.MPEiX;
        } else if (isNetWare()) {
            _instance.platform = EPlatform.NetWare_411;
        } else if (isOpenVMS()) {
            _instance.platform = EPlatform.OpenVMS;
        } else if (isOS2()) {
            _instance.platform = EPlatform.OS2;
        } else if (isOS390()) {
            _instance.platform = EPlatform.OS390;
        } else if (isOSF1()) {
            _instance.platform = EPlatform.OSF1;
        } else if (isSolaris()) {
            _instance.platform = EPlatform.Solaris;
        } else if (isSunOS()) {
            _instance.platform = EPlatform.SunOS;
        } else if (isWindows()) {
            _instance.platform = EPlatform.Windows;
        } else {
            _instance.platform = EPlatform.Others;
        }
        return _instance.platform;
    }

//    public static void main(String[] args) {
//        System.out.println(OSinfo.getOSname());// 获取系统类型
//        System.out.println(OSinfo.isWindows());// 判断是否为windows系统
//    }

}
