package cn.tannn.jdevelops.renewpwd;

import java.util.Map;

/**
 * 配置文件格式类 - 配置源
 *
 * @param env    环境
 * @param config Map
 */
public record PwdConfigChangeEvent(String env, Map<String, String> config) {
}
