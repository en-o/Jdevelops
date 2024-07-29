package cn.tannn.jdevelops.quartz.util;

import cn.tannn.jdevelops.quartz.exception.TaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/29 上午10:55
 */
public class ClazzUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClazzUtil.class);


    /**
     * 通过类全路径加载 class
     * @param classPath 类全路径
     * @return
     */
    public static Class checkClasses(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (Exception e) {
            LOG.warn("quartz class not found: {}", classPath);
            throw new TaskException("quartz class not found", e);
        }
    }
}
