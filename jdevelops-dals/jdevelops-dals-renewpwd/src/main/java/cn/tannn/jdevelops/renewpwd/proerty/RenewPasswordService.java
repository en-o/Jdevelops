package cn.tannn.jdevelops.renewpwd.proerty;

import cn.tannn.jdevelops.renewpwd.pojo.PwdConfigChangeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码处理器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/13 15:48
 */
public class RenewPasswordService {
    /**
     * 当前使用的配置
     */
    private volatile Map<String, String> config = new HashMap<>();

    /**
     * 初始化配置
     */
    public void initialize(String password) {
        // 初始化配置
        HashMap<String, String> configMap = new HashMap<>();
        configMap.put("spring.datasource.password", password);
        this.config = configMap;
    };


    public String[] getPropertyNames() {
        // 返回配置的所有key
        return this.config.keySet().toArray(new String[0]);
    }


    public String getProperty(String name) {
        // 调用这里获取新的配置值
        return config.get(name);
    }

}
