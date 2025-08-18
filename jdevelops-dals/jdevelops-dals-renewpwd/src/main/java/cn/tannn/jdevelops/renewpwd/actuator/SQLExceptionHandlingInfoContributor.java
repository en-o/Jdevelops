package cn.tannn.jdevelops.renewpwd.actuator;

import cn.tannn.jdevelops.renewpwd.exception.SQLExceptionHandlingDataSourceProxy;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.properties.SQLExceptionHandlingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL异常处理信息贡献者
 * 向/actuator/info端点添加SQL异常处理相关信息
 *
 * @author tannn
 * @since 1.0.0
 */
@Component
@ConditionalOnClass(InfoContributor.class)
@ConditionalOnProperty(
    prefix = "jdevelops.renewpwd",
    name = "enabled",
    havingValue = "true"
)
public class SQLExceptionHandlingInfoContributor implements InfoContributor {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RenewpwdProperties config;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> sqlExceptionHandlingInfo = new HashMap<>();

        sqlExceptionHandlingInfo.put("enabled", config.getEnabled());
        sqlExceptionHandlingInfo.put("proxyActive", dataSource instanceof SQLExceptionHandlingDataSourceProxy);
        sqlExceptionHandlingInfo.put("version", "2.0.0");
        sqlExceptionHandlingInfo.put("features", getFeaturesList());

        builder.withDetail("sqlExceptionHandling", sqlExceptionHandlingInfo);
    }

    private Map<String, Boolean> getFeaturesList() {
        Map<String, Boolean> features = new HashMap<>();
        features.put("globalExceptionInterception", true);
        features.put("slowQueryDetection", true);
        features.put("deadlockDetection", true);
        features.put("connectionLeakDetection", config.getException().isEnableConnectionLeakDetection());
        features.put("sqlInjectionDetection", config.getException().isEnableSqlInjectionDetection());
        features.put("batchOperationMonitoring", config.getException().isEnableBatchMonitoring());
        features.put("transactionMonitoring", config.getException().isEnableTransactionMonitoring());
        features.put("connectionPoolMonitoring", config.getException().isEnableConnectionPoolMonitoring());
        features.put("alerting", config.getException().isAlertEnabled());
        return features;
    }
}
