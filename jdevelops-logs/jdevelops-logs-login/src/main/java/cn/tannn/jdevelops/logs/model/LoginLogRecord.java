package cn.tannn.jdevelops.logs.model;


import cn.tannn.jdevelops.logs.LoginLog;
import cn.tannn.jdevelops.logs.aspect.LoginLogAspect;
import cn.tannn.jdevelops.logs.enums.LoginType;

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
     * 登录token
     */
    private String token;

    /**
     * 请求头
     */
    private HttpServletRequest request;

    /**
     * 登录状态：0[失败],1[成功]
     */
    private Integer status;

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
     * 登录平台 (默认冲token里取)
     */
    private String tokenPlatform;

    /**
     * 登录时间（yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime loginTime;


    public void writeTokenPlatform(List<String> platform) {
        if(platform != null && !platform.isEmpty()) {
            this.tokenPlatform = String.join(",", platform);
        }
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
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

    public String getTokenPlatform() {
        return tokenPlatform;
    }

    public void setTokenPlatform(String tokenPlatform) {
        this.tokenPlatform = tokenPlatform;
    }

    public LocalDateTime getLoginTime() {
        return loginTime==null? LocalDateTime.now() : loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
