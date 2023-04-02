package cn.jdevelops.util.spring.enums;

/**
 * 系统名
 * @author 来源于网络
 * <a href="https://blog.csdn.net/fangchao2011/article/details/88785637">参考</a>
 */
public enum EPlatform {

    /* any */
    ANY("any"),

    /* Linux */
    LINUX("Linux"),

    /* Mac OS */
    MAC_OS("Mac OS"),

    /* Mac OS X */
    MAC_OS_X("Mac OS X"),

    /* Windows */
    WINDOWS("Windows"),

    /* OS/2 */
    OS_2("OS/2"),

    /* Solaris */
    SOLARIS("Solaris"),

    /* SunOS */
    SUN_OS("SunOS"),

    /* MPE/iX */
    MP_EI_X("MPE/iX"),

    /* HP-UX */
    HP_UX("HP-UX"),

    /* AIX */
    AIX("AIX"),

    /* OS/390 */
    OS390("OS/390"),

    /* FreeBSD */
    FREE_BSD("FreeBSD"),

    /* Irix */
    IRIX("Irix"),

    /* Digital Unix */
    DIGITAL_UNIX("Digital Unix"),

    /* NetWare */
    NET_WARE_411("NetWare"),

    /* OSF1 */
    OSF1("OSF1"),

    /* OpenVMS */
    OPEN_VMS("OpenVMS"),

    /* Others */
    OTHERS("Others");


    EPlatform(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * 描述
     */
    private final String description;
}
