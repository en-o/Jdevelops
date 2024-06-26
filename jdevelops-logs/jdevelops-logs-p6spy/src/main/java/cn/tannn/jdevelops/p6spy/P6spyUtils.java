package cn.tannn.jdevelops.p6spy;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.List;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 上午10:27
 */
public class P6spyUtils {

    private static final Logger logger = LoggerFactory.getLogger(P6spyUtils.class);
    public static String checkDriverClasses(List<String> drivers) {
        String driverStrs = "";
        for (String driverClass : drivers) {
            try {
                Class.forName(driverClass);
                driverStrs += driverClass + ",";
            } catch (Exception e) {
                logger.warn("Driver class not found: {}", driverClass);
            }
        }
        if (driverStrs.isEmpty()) {
            throw new RuntimeException("Drivers not found: " + drivers);
        }
        return driverStrs.substring(0, driverStrs.length() - 1);
    }
}
