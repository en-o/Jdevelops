package cn.tannn.jdevelops.monitor.actuator;

import cn.tannn.jdevelops.monitor.actuator.properties.AppInfoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

import java.util.Map;

/**
 * actuator/info信息填充
 * @author tan
 */
public class AppInfoContributor implements InfoContributor {

    @Autowired
    private AppInfoConfiguration appInfoConfiguration;
    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = appInfoConfiguration.toMap();
        builder.withDetail("application", details);
    }



}
