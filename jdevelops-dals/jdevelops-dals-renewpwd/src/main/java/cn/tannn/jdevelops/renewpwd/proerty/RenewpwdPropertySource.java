package cn.tannn.jdevelops.renewpwd.proerty;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * 封装自定义的配置源 （ 元数据来源于 注册中心
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:44
 */
public class RenewpwdPropertySource extends EnumerablePropertySource<RenewPasswordService> {


    public RenewpwdPropertySource(String name, RenewPasswordService source) {
        super(name, source);
    }

    /**
     *  Spring 的 PropertySource 需要知道有哪些属性可用
     * @return 配置源名称
     */
    @Override
    public String[] getPropertyNames() {
        return this.source.getPropertyNames();
    }

    /**
     * Spring 在解析 ${} 占位符时会调用此方法
     * @param name key
     * @return value
     */
    @Override
    public Object getProperty(String name) {
        return this.source.getProperty(name);
    }
}
