package cn.jdevelops.config.standalone.properties;

/**
 * 利用的配置的基础信息里配置文件需要填写的 （配置key
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/4 下午9:30
 */
public interface ConfigMetaProperties {

    /**
     * 客户端ID
     */
    String APP = "jdevelops.config.app";

    /**
     * 客户端环境
     */
    String DEV = "jdevelops.config.dev";

    /**
     * 客户端 namespace
     */
    String NS = "jdevelops.config.ns";

    /**
     * 注册中心地址
     */
    String CONFIG_SERVER = "jdevelops.config.configServer";

}
