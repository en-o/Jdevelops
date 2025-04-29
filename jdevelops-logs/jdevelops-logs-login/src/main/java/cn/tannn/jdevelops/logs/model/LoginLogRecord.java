package cn.tannn.jdevelops.logs.model;


import cn.tannn.ip2region.sdk.IpRegionUtil;
import cn.tannn.ip2region.sdk.IpUtil;
import cn.tannn.ip2region.sdk.UserAgentUtil;
import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.constant.LoginType;
import cn.tannn.jdevelops.logs.context.LoginContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志记录
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/8 11:28
 */
public class LoginLogRecord {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogRecord.class);


    /**
     * 请求头
     */
    private HttpServletRequest request;

    /**
     * 登录状态：0[失败],1[成功]
     */
    private Integer status;


    /**
     * true = 退出 ， false = 登录
     */
    private boolean logout;

    /**
     * 备注
     */
    private String description;

    /**
     * 登录类型
     *
     * @see LoginType
     */
    private String type;


    /**
     * 登录时间（yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime loginTime;

    /**
     * 解析表达式后获取的参数
     */
    private String expression;

    /**
     * 访问设备信息
     */
    private String userAgent;

    /**
     * 登录的IP
     */
    private String ipAddress;

    /**
     * ip归属地[国家-区域-省份-城市-ISP]
     */
    private String ipRegion;

    /**
     * 上下文
     */
    private LoginContext loginContext;


    public LoginLogRecord() {
        this.loginTime = LocalDateTime.now();
    }

    public LoginLogRecord(LoginLog loginLog) {
        if (loginLog != null) {
            this.expression = loginLog.expression();
            this.type = loginLog.type();
            this.logout = !loginLog.login();
        }
        this.loginTime = LocalDateTime.now();
    }

    public LoginLogRecord(
            HttpServletRequest request
            , Integer status
            , String description
            , LoginLog loginLog) {
        this.request = request;
        this.status = status;
        this.description = description;
        this.loginTime = LocalDateTime.now();
        if (loginLog != null) {
            this.expression = loginLog.expression();
            this.type = loginLog.type();
            this.logout = loginLog.login();
        }
    }

    public LoginLogRecord(HttpServletRequest request
            , Integer status
            , boolean logout
            , String description
            , String type
            , String expression
    ) {
        this.request = request;
        this.status = status;
        this.logout = logout;
        this.description = description;
        this.type = type;
        this.loginTime = LocalDateTime.now();
        this.expression = expression;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        if (request != null) {
            try {
                this.ipAddress = IpUtil.getPoxyIpEnhance(request);
            } catch (Exception e) {
                LOG.error("ip获取失败：{}", e.getMessage());
            }
            if (this.ipAddress != null && !this.ipAddress.isBlank()) {
                try {
                    this.ipRegion = IpRegionUtil.getIpRegion(this.ipAddress);
                } catch (Exception e) {
                    LOG.error("ip归属地获取失败：{}", e.getMessage());
                }
                try {
                    this.userAgent = UserAgentUtil.parseStr(request);
                } catch (Exception e) {
                    LOG.error("设备信息获取失败：{}", e.getMessage());
                }
            }
        }
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getLoginTime() {
        return loginTime == null ? LocalDateTime.now() : loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isLogout() {
        return logout;
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpRegion() {
        return ipRegion;
    }

    public void setIpRegion(String ipRegion) {
        this.ipRegion = ipRegion;
    }

    public LoginContext getLoginContext() {
        return loginContext;
    }

    public void setLoginContext(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    @Override
    public String toString() {
        return "LoginLogRecord{" +
                "request=" + request +
                ", status=" + status +
                ", logout=" + logout +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", loginTime=" + loginTime +
                ", expression='" + expression + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", ipRegion='" + ipRegion + '\'' +
                ", loginContext=" + loginContext +
                '}';
    }

    public String toStyle() {
        return status + " | " +
                logout + " | " +
                '\'' + description + '\'' + " | " +
                '\'' + type + '\'' + " | " +
                loginTime + " | " +
                '\'' + expression + '\'' + " | " +
                '\'' + userAgent + '\'' + " | " +
                '\'' + ipAddress + '\'' + " | " +
                '\'' + ipRegion + '\'';
    }
}
