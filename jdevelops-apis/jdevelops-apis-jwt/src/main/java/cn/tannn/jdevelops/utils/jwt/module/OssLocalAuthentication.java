package cn.tannn.jdevelops.utils.jwt.module;

import java.util.StringJoiner;

/**
 * 本地上次文件访问鉴权
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/18 07:06
 */
public class OssLocalAuthentication {

    /**
     *  是否开启  local oss file access authentication
     *  默认false
     */
    private boolean enable = false;

    /**
     * 鉴权的 cookie key
     */
    private String ossLocalJwtKey;

    /**
     * CheckSpecialPath 验证 local oss 用的变量 (下面是默认的可以更改，表示 下面两个变量存在数据值才会对 local oss 文件进行鉴权
     *   String localOssResourceUpDir = environment.getProperty("jdevelops.oss.local.upload-dir", "");
     *   String localOssResourceContextPath = environment.getProperty("jdevelops.oss.local.context-path", "");
     */
    private String verifyLocalOssResourceUpDir;
    /**
     * CheckSpecialPath 验证 local oss 用的变量 (下面是默认的可以更改，表示 下面两个变量存在数据值才会对 local oss 文件进行鉴权
     *   String localOssResourceUpDir = environment.getProperty("jdevelops.oss.local.upload-dir", "");
     *   String localOssResourceContextPath = environment.getProperty("jdevelops.oss.local.context-path", "");
     */
    private String verifyLocalOssResourceContextPath;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getOssLocalJwtKey() {
        return ossLocalJwtKey==null?"token":ossLocalJwtKey;
    }

    public void setOssLocalJwtKey(String ossLocalJwtKey) {
        this.ossLocalJwtKey = ossLocalJwtKey;
    }

    public String getVerifyLocalOssResourceUpDir() {
        return verifyLocalOssResourceUpDir==null?"jdevelops.oss.local.upload-dir":verifyLocalOssResourceUpDir;
    }

    public void setVerifyLocalOssResourceUpDir(String verifyLocalOssResourceUpDir) {
        this.verifyLocalOssResourceUpDir = verifyLocalOssResourceUpDir;
    }

    public String getVerifyLocalOssResourceContextPath() {
        return verifyLocalOssResourceContextPath==null?"jdevelops.oss.local.context-path":verifyLocalOssResourceContextPath;
    }

    public void setVerifyLocalOssResourceContextPath(String verifyLocalOssResourceContextPath) {
        this.verifyLocalOssResourceContextPath = verifyLocalOssResourceContextPath;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OssLocalAuthentication.class.getSimpleName() + "[", "]")
                .add("enable=" + enable)
                .add("ossLocalJwtKey='" + ossLocalJwtKey + "'")
                .add("verifyLocalOssResourceUpDir='" + verifyLocalOssResourceUpDir + "'")
                .add("verifyLocalOssResourceContextPath='" + verifyLocalOssResourceContextPath + "'")
                .toString();
    }
}
