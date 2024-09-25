package cn.tannn.jdevelops.annotations.web.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 谁生成的jwt [前端系统平台]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2023/10/31 9:32
 */
public interface PlatformConstant {

    /**
     * 通用平台使用
     */
    String COMMON="COMMON";

    /**
     * web-admin 平台使用
     */
    String WEB_ADMIN="WEB_ADMIN";

    /**
     * web-H5 平台使用
     */
    String WEB_H5="WEB_H5";

    /**
     * 小程序 平台使用
     */
    String APPLET="APPLET";

    /**
     * 大屏 平台使用
     */
    String  DASHBOARD="DASHBOARD";

    /**
     * 所有的枚举，如果前端这个数据而且还加入了新的参数,那就 all().add(*);
     * @return PlatformConstant
     */
    static List<String>  all(){
        List<String> all = new ArrayList<String>();
        all.add(DASHBOARD);
        all.add(APPLET);
        all.add(WEB_H5);
        all.add(WEB_ADMIN);
        all.add(COMMON);
        return all;
    }

}
