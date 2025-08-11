package cn.tannn.jdevelops.renewpwd;

import java.util.Set;

/**
 * 配置刷新处理器接口
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public interface ConfigsRefreshHandler {

    /**
     * 刷新数据源相关配置 [spring.datasource]
     * <p> 通常用于数据库密码变更等场景
     */
    void refreshDataSourceConfig();

}
