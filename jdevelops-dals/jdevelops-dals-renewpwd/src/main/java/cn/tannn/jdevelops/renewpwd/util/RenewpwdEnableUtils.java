package cn.tannn.jdevelops.renewpwd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static cn.tannn.jdevelops.renewpwd.pojo.RenewpwdConstant.CONFIG_KEY;

public class RenewpwdEnableUtils {
    private static final Logger log = LoggerFactory.getLogger(RenewpwdEnableUtils.class);

    /**
     * 通过 ApplicationContext 判断是否启用 renewpwd
     * 优先全局配置，再查 RenewpwdProperties bean
     */
    public static boolean isRenewpwdEnabled(ApplicationContext context) {
        // 全局配置
        String configValue = context.getEnvironment().getProperty(CONFIG_KEY);
        if (configValue != null) {
            return Boolean.parseBoolean(configValue);
        }
        // 默认启用
        return true;
    }

    /**
     * 通过 Environment 和注解元数据判断是否启用 renewpwd
     * 优先全局配置，再查注解
     */
    public static boolean isRenewpwdEnabled(Environment environment) {
        if (environment != null) {
            String configValue = environment.getProperty(CONFIG_KEY);
            if (configValue != null) {
                return Boolean.parseBoolean(configValue);
            }
        }
        // 默认启用
        return true;
    }
}
