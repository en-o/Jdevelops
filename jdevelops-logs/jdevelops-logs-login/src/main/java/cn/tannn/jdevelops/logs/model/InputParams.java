package cn.tannn.jdevelops.logs.model;

/**
 * 入参
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/4/28 12:15
 */
public class InputParams {
    String loginName;
    String platform;

    public InputParams() {
    }

    public InputParams(String loginName, String platform) {
        this.loginName = loginName;
        this.platform = platform;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
