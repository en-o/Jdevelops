package cn.tannn.jdevelops.renewpwd.proerty;

import cn.tannn.jdevelops.renewpwd.jdbc.ExecuteJdbcSql;
import cn.tannn.jdevelops.renewpwd.properties.RenewpwdProperties;
import cn.tannn.jdevelops.renewpwd.util.AESUtil;
import cn.tannn.jdevelops.renewpwd.util.PasswordUpdateListener;
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
import org.springframework.util.StringUtils;

/**
 * 数据库密码处理器 - 启动时对密码进行处理
 * <P> 1. 使用密码池密码 </P>
 * <P> 2. 密码解密 </P>
 * <P> 3. masterPassword为空时自动初始化（全局有效） </P>
 */
public class DatabasePwdEnvironmentPostProcessor implements BeanFactoryPostProcessor,
        EnvironmentAware, PasswordUpdateListener {

    private static final String PROPERTY_SOURCES = "renewpwdConfigPropertySources";
    private static final String PROPERTY_SOURCE = "renewpwdConfigPropertySource";
    private static final String DATASOURCE_PASSWORD_KEY = "spring.datasource.password";
    private static final String RENEWPWD_CONFIG_KEY = "jdevelops.renewpwd";
    private static final Logger log = LoggerFactory.getLogger(DatabasePwdEnvironmentPostProcessor.class);

    private Environment environment;
    private RenewPasswordService configService;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // 注册密码更新监听器
        registerPasswordUpdateListener();

        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        String originalPassword = env.getProperty(DATASOURCE_PASSWORD_KEY);

        // 处理密码配置并确保全局有效
        PasswordConfig passwordConfig = processPasswordConfiguration(originalPassword, beanFactory);

        // 验证和选择有效密码
        String validPassword = selectValidPassword(env, passwordConfig);

        // 初始化配置服务
        initializeConfigService(validPassword);

        // 注册服务到Spring容器
        beanFactory.registerSingleton("renewPasswordService", configService);

        // 设置属性源
        setupPropertySource(env);
    }

    /**
     * 注册密码更新监听器
     * <p> 注册监听器(先注册防止PwdRefreshUtil#passwordUpdateListener为空)
     * <p> 将当前的 DatabasePwdEnvironmentPostProcessor 实例（实现了 PasswordUpdateListener 接口）注册为密码更新事件的监听器
     * <p> 这样当 ExecuteJdbcSql 成功更新数据库密码时，会回调 onPasswordUpdated(newPassword) 方法，通知 DatabasePwdEnvironmentPostProcessor，从而可以用新密码重新初始化 RenewPasswordService，保证应用始终使用最新的数据库密码。
     *
     */
    private void registerPasswordUpdateListener() {
        // 先注册防止PwdRefreshUtil#passwordUpdateListener为空
        ExecuteJdbcSql.setPasswordUpdateListener(this);
        log.debug("[renewpwd] 密码更新监听器注册完成");
    }

    /**
     * 处理密码配置并确保全局有效
     * @param originalPassword spring.datasource.password 的原始密码
     * @param beanFactory ConfigurableListableBeanFactory
     */
    private PasswordConfig processPasswordConfiguration(String originalPassword,
                                                        ConfigurableListableBeanFactory beanFactory) {
        try {
            Binder binder = Binder.get(environment);
            RenewpwdProperties renewpwdProperties = binder.bind(RENEWPWD_CONFIG_KEY, Bindable.of(RenewpwdProperties.class))
                    .orElseThrow(() -> new IllegalStateException("无法加载密码配置"));

            // 初始化masterPassword如果为空，并确保全局有效
            initializePasswordIfEmpty(renewpwdProperties, originalPassword);

            // 将修改后的PasswordPool注册为Spring Bean，确保全局有效
            registerPasswordPoolAsGlobalBean(renewpwdProperties, beanFactory);

            return new PasswordConfig(
                    AESUtil.decryptPassword(originalPassword, renewpwdProperties.getPwdEncryptKey()),
                    determineBackupPassword(renewpwdProperties, originalPassword),
                    renewpwdProperties
            );
        } catch (Exception e) {
            log.warn("密码续命配置处理异常: {}，使用默认密码处理", e.getMessage());
            return new PasswordConfig(
                    AESUtil.decryptPassword(originalPassword, null),
                    originalPassword,
                    null
            );
        }
    }

    /**
     * 初始化masterPassword如果为空
     */
    private void initializePasswordIfEmpty(RenewpwdProperties renewpwdProperties, String originalPassword) {
        if (!StringUtils.hasText(renewpwdProperties.getMasterPassword())) {
            log.info("[renewpwd] masterPassword为空，使用数据源密码进行初始化");
            renewpwdProperties.setMasterPassword(originalPassword);
            log.info("[renewpwd] masterPassword初始化完成: {}", originalPassword);
        }
        if (!StringUtils.hasText(renewpwdProperties.getBackupPassword())) {
            log.info("[renewpwd] backupPassword为空，使用数据源密码进行初始化");
            renewpwdProperties.setMasterPassword(originalPassword);
            log.info("[renewpwd] backupPassword初始化完成: {}", originalPassword);
        }
    }

    /**
     * 将PasswordPool注册为全局Bean，确保其他地方注入时能获取到修改后的实例
     */
    private void registerPasswordPoolAsGlobalBean(RenewpwdProperties renewpwdProperties, ConfigurableListableBeanFactory beanFactory) {

        // 直接注册修改后的PasswordPool实例为单例Bean
        // 如果已存在同名Bean，registerSingleton会覆盖它
        beanFactory.registerSingleton("renewpwdProperties", renewpwdProperties);
        log.info("[renewpwd] PasswordPool已注册为全局单例Bean，masterPassword修改全局有效");
    }

    /**
     * 确定备用密码
     */
    private String determineBackupPassword(RenewpwdProperties renewpwdProperties, String originalPassword) {
        String backupPassword = renewpwdProperties.getBackupPassword();
        if (StringUtils.hasText(backupPassword)) {
            return renewpwdProperties.getBackupPasswordDecrypt();
        }
        return originalPassword;
    }

    /**
     * 选择有效的密码
     */
    private String selectValidPassword(ConfigurableEnvironment env, PasswordConfig config) {
        // 首先尝试主密码 - 错误的情况下会更新备份密码所以下面的第二次判断就会正确并且使用备份密码
        if (ExecuteJdbcSql.validateDatasourceConfig(env, config.primaryPassword, config.backupPassword, config.renewpwdProperties)) {
            log.info("[renewpwd] 使用主密码连接数据库成功");
            return config.primaryPassword;
        }

        // 主密码失效，尝试备用密码
        if (ExecuteJdbcSql.validateDatasourceConfig(env, config.backupPassword, config.primaryPassword, config.renewpwdProperties)) {
            log.info("[renewpwd] 主密码失效，使用备用密码连接数据库成功");
            return config.backupPassword;
        }

        // 两个密码都无效
        throw new RuntimeException("数据库密码配置错误，主密码和备用密码都无法连接数据库，请检查配置文件或环境变量");
    }

    /**
     * 初始化配置服务
     */
    private void initializeConfigService(String password) {
        if (configService == null) {
            configService = new RenewPasswordService();
            configService.initialize(password);
        }
        log.info("[renewpwd] RenewPasswordService初始化完成");
    }

    /**
     * 设置属性源
     */
    private void setupPropertySource(ConfigurableEnvironment env) {
        RenewpwdPropertySource propertySource = new RenewpwdPropertySource(PROPERTY_SOURCES, configService);
        CompositePropertySource composite = new CompositePropertySource(PROPERTY_SOURCE);
        composite.addPropertySource(propertySource);
        // 将配置中心得到属性置顶(拥有更高的优先级)
        env.getPropertySources().addFirst(composite);
        log.debug("[renewpwd] 属性源设置完成");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onPasswordUpdated(String newPassword) {
        log.info("[renewpwd] 收到密码更新通知，重新初始化RenewPasswordService");
        initializeConfigService(newPassword);
    }

    /**
     * 密码配置内部类
     */
    public static class PasswordConfig {
        final String primaryPassword;
        final String backupPassword;
        final RenewpwdProperties renewpwdProperties;

        PasswordConfig(String primaryPassword, String backupPassword, RenewpwdProperties renewpwdProperties) {
            this.primaryPassword = primaryPassword;
            this.backupPassword = backupPassword;
            this.renewpwdProperties = renewpwdProperties;
        }

        public String getPrimaryPassword() {
            return primaryPassword;
        }

        public String getBackupPassword() {
            return backupPassword;
        }

        public RenewpwdProperties getRenewpwdProperties() {
            return renewpwdProperties;
        }
    }
}
