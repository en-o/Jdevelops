package cn.tannn.jdevelops.renewpwd.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码池 - 只在两个密码之间切换
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/11 14:54
 */
@ConfigurationProperties(prefix = "jdevelops.renewpwd")
public class PasswordPool {
    /**
     * 密码组,DatabasePwdEnvironmentPostProcessor#postProcessBeanFactory
     * <p>如果空，这使用datasource中的密码进行处理所有事情</p>
     * <p>如果不为空，主密码登录失败就会从密码组里挑选密码进行重试或重置</p>
     * <p> 如果需要加密请使用：ENC(你的密码)</p>
     */
    private List<String> passwords = new ArrayList<>();

    /**
     * 是否启用密码虚续命，以配置文件为准-这里只是为空编写配置的时候有提示，具体在RenewpwdRegister#isRefreshEnabled
     * <p>这个可以控制注解中的enabled，这不代表不写注解，注解必须写</p>
     */
    private Boolean enabled;


    public List<String> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<String> passwords) {
        this.passwords = passwords;
    }

    public Boolean getEnabled() {
        return enabled != null && enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "PasswordPool{" +
                "passwords=" + passwords +
                ", enabled=" + enabled +
                '}';
    }
}
