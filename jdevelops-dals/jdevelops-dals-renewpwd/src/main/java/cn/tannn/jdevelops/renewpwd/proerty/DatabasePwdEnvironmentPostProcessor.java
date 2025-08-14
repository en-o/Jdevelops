package cn.tannn.jdevelops.renewpwd.proerty;

import cn.tannn.jdevelops.renewpwd.pojo.PasswordPool;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PasswordUpdateListener;
import cn.tannn.jdevelops.renewpwd.util.PwdRefreshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 数据库密码处理器 - 启动时对密码进行处理
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PasswordUpdateListener {

    private final static String PROPERTY_SOURCES = "renewpwdConfigPropertySources";
    private final static String PROPERTY_SOURCE = "renewpwdConfigPropertySource";
    private static final Logger log = LoggerFactory.getLogger(DatabasePwdEnvironmentPostProcessor.class);
    private Environment environment;
    private RenewPasswordService configService;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        String password = ENV.getProperty("spring.datasource.password");
        String backupPassword = password;

        password = decryptPassword(password);

        try {
            Binder binder = Binder.get(environment);
            PasswordPool passwordPool = binder.bind("jdevelops.renewpwd", Bindable.of(PasswordPool.class))
                    .orElseThrow(() -> new IllegalStateException("无法加载密码配置"));
            backupPassword = decryptPassword(passwordPool.getBackupPassword());
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                log.warn("{}，使用datasource的密码进行处理", e.getMessage());
            } else {
                log.warn("没有配置密码池，使用datasource的密码进行处理");
            }
        }
        // 一共就两个，我这里就这样弄了，如果是2+ 目前还没找到标记当前的方法
        if (!PwdRefreshUtil.validateDatasourceConfig(ENV, password, backupPassword)) {
            if (PwdRefreshUtil.validateDatasourceConfig(ENV, backupPassword, password)) {
                password = backupPassword;
            } else {
                throw new RuntimeException("数据库密码配置错误，请检查配置文件或环境变量");
            }
        }

        configService = new RenewPasswordService();
        configService.initialize(password);
        // 将实例注册到Spring容器中，确保是单例
        beanFactory.registerSingleton("renewPasswordService", configService);

        // 注册监听器
        PwdRefreshUtil.setPasswordUpdateListener(this);

        // 构建spring 配置格式- 配置源的集成机制
        RenewpwdPropertySource propertySource = new RenewpwdPropertySource(PROPERTY_SOURCES, configService);
        CompositePropertySource composite = new CompositePropertySource(PROPERTY_SOURCE);
        composite.addPropertySource(propertySource);
        // 将配置中心得到属性置顶(拥有更高的优先级
        ENV.getPropertySources().addFirst(composite);
    }





    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    /**
     * 解密密码
     * @param password 密码字符
     * @return 解密的
     */
    private static String decryptPassword(String password) {
        // 需要解密
        if (password != null && password.startsWith("ENC(")) {
            String encrypted = password.substring(4, password.length() - 1);
            password = AESUtil.decrypt(encrypted);
        }
        return password;
    }

    @Override
    public void onPasswordUpdated(String newPassword) {
        log.info("[renewpwd] 收到密码更新通知，重新初始化RenewPasswordService");
        if (configService != null) {
            configService.initialize(newPassword);
        }
    }
}
