package cn.jdevelops.spring.enums;

/**
 * 系统名
 * @link : https://blog.csdn.net/fangchao2011/article/details/88785637
 */
public enum EPlatform {
    Any("any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");

    EPlatform(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return description;
    }

    private String description;
}