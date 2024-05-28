package cn.tannn.jdevelops.utils.jwt.constant;

import java.util.List;

/**
 * 谁生成的jwt [前端系统平台]
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/10/31 9:32
 */
public enum PlatformConstant {

    /**
     * 通用平台使用
     */
    COMMON,

    /**
     * web-admin 平台使用
     */
    WEB_ADMIN,

    /**
     * web-H5 平台使用
     */
    WEB_H5,

    /**
     * 小程序 平台使用
     */
    APPLET;


    /**
     * 自定义 contains 方法
     *
     * <p>我用 list自己的 contains 出现 test能过 实际不过来 [问题发现了,从jwt中那数据的时候对象好像有点问题导致的，问题处理了但是这里不改了]</p>
     *
     * @param otherEnum PlatformConstants
     * @return true 当前对象 在 [otherEnum] 里
     */
    public boolean contains(List<PlatformConstant> otherEnum) {
        for (PlatformConstant constant : otherEnum) {
            if(constant.name().equalsIgnoreCase(this.name())){
                return true;
            }
        }
        return false;
    }
}
