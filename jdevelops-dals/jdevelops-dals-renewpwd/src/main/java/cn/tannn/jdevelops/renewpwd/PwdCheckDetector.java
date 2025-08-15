package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.annotation.EnableRenewpwd;
import cn.tannn.jdevelops.renewpwd.exception.StopDetectorException;
import cn.tannn.jdevelops.renewpwd.pojo.PasswordPool;
import cn.tannn.jdevelops.renewpwd.pojo.PwdExpireInfo;
import cn.tannn.jdevelops.renewpwd.util.DatabaseUtils;
import cn.tannn.jdevelops.renewpwd.util.RenewpwdEnableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * 密码续命探测器
 *
 * <p> 基于数据驱动的触发器，根据查询到的密码过期信息进行触发。
 * <p> 工作流程：
 * <p> 1. 查询密码过期信息
 * <p> 3. 判断是否需要需要触发，不需要触发进行下一次探测
 * <p> 4. 执行密码刷新
 * <p> 5. 重新开始查询，如此循环
 *
 * <p> 自管理特性：
 * <p> - 自动注册JVM关闭钩子，确保应用退出时线程被正确关闭
 * <p> - 无需手动调用stop()方法
 * <p> - 创建后直接start()即可
 *
 * <pre>
 * // 用法示例
 * &#064;Autowired
 * private ApplicationContext applicationContext;
 * &#064;Override
 * public void run(ApplicationArguments args) throws Exception {
 *	try {
 *	    log.info("密码续命触发器启动");
 *	    PwdCheckDetector detector = PwdCheckDetector.builder()
 *			.pwdExpireSupplier(() -> new PwdExpireInfo(currentPassword, checkPassword()))
 *		    .retryIntervalMinutes(5)// 探测间隔，默认5分钟
 *		    .applicationContext(applicationContext)
 *			.build();
 *	    detector.start(); // 启动后会自动管理生命周期
 *	 } catch (Exception e) {
 *		log.error("密码续命触发器启动失败", e);
 *	 }
 * }
 * </pre>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V4.0
 * @date 2025/8/15 16:27
 */
public class PwdCheckDetector implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(PwdCheckDetector.class);

    /**
     * 控制运行状态的原子布尔值
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 关闭钩子是否已注册
     */
    private final AtomicBoolean shutdownHookRegistered = new AtomicBoolean(false);

    /**
     * 定时执行服务
     */
    private ScheduledExecutorService scheduler;

    /**
     * 自行注册密码过期查询方法
     */
    private final Supplier<PwdExpireInfo> pwdExpireSupplier;

    /**
     * 查询失败/或者暂未过期时的重试间隔（分钟）
     */
    private final int retryIntervalMinutes;

    /**
     * 上下文环境
     */
    private ApplicationContext applicationContext;


    private PwdCheckDetector(Builder builder) {
        this.pwdExpireSupplier = builder.pwdExpireSupplier;
        this.retryIntervalMinutes = builder.retryIntervalMinutes;
        this.applicationContext = builder.applicationContext;

        // 验证必要参数
        if (this.pwdExpireSupplier == null) {
            throw new IllegalArgumentException("pwdExpireSupplier cannot be null");
        }
        if (this.applicationContext == null) {
            throw new IllegalArgumentException("applicationContext cannot be null");
        }
    }

    /**
     * 启动触发器（自动注册关闭钩子）
     */
    public synchronized boolean start() {

        if(!RenewpwdEnableUtils.isRenewpwdEnabled(applicationContext)){
            log.warn("密码续命触发器未启用，请检查配置");
            return false;
        }
        if (!running.compareAndSet(false, true)) {
            log.warn("密码续命触发器已经在运行中");
            return false;
        }

        try {
            // 创建单线程调度器
            scheduler = new ScheduledThreadPoolExecutor(1, r -> {
                Thread thread = new Thread(r, "PwdCheckDetector-Thread");
                // 非守护线程，JVM 会等待所有非守护线程完成后才退出，确保任务完成
                thread.setDaemon(false);
                thread.setUncaughtExceptionHandler((t, e) ->
                        log.error("密码触发器线程异常", e));
                return thread;
            });

            // 注册JVM关闭钩子（只注册一次）
            registerShutdownHook();

            // 立即开始第一次查询和调度
            scheduleNextCheck();

            log.info("密码续命触发器已启动，每隔 {} 分钟触发一次，已自动注册关闭钩子", retryIntervalMinutes);
            return true;

        } catch (Exception e) {
            running.set(false);
            log.error("启动密码续命触发器失败", e);
            if (scheduler != null) {
                scheduler.shutdown();
                scheduler = null;
            }
            throw new RuntimeException("启动密码续命触发器失败", e);
        }
    }

    /**
     * 注册JVM关闭钩子（自动管理生命周期的核心）
     */
    private void registerShutdownHook() {
        if (shutdownHookRegistered.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("JVM正在关闭，自动停止密码续命触发器...");
                internalStop();
            }, "PwdCheckDetector-ShutdownHook"));
            log.debug("已注册JVM关闭钩子");
        }
    }

    /**
     * 停止触发器（公共方法，也可以手动调用）
     */
    public synchronized boolean stop() {
        return internalStop();
    }

    /**
     * 内部停止方法
     */
    private synchronized boolean internalStop() {
        if (!running.compareAndSet(true, false)) {
            log.debug("密码续命触发器已经停止或未启动");
            return false;
        }

        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                        log.warn("等待任务结束超时，强制关闭调度器");
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    log.warn("等待任务结束被中断，强制关闭调度器");
                    scheduler.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                scheduler = null;
            }

            log.info("密码续命触发器已停止");
            return true;

        } catch (Exception e) {
            log.error("停止密码续命触发器时发生异常", e);
            return false;
        }
    }

    /**
     * 重启触发器
     */
    public synchronized boolean restart() {
        log.info("正在重启密码续命触发器...");
        internalStop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return start();
    }

    /**
     * 检查触发器是否正在运行
     */
    public boolean isRunning() {
        return running.get() && scheduler != null && !scheduler.isShutdown();
    }

    /**
     * 调度下一次检查 (使用递归调度方式)
     */
    private void scheduleNextCheck() {
        if (!running.get()) {
            return;
        }

        try {
            // 1. 查询密码过期信息
            PwdExpireInfo pwdInfo = queryPwdExpireInfo();

            if (pwdInfo == null || !pwdInfo.isCurrentIsExpireSoon()) {
                log.warn("数据库密码未过期，继续下一次探测，探测间隔：{}分钟", retryIntervalMinutes);
                scheduleRetry();
            } else {
                // 触发时间已到，立即执行
                log.info("触发时间已到，立即执行密码处理流程");
                scheduler.submit(this::safeExecutePasswordFlow);
            }
        } catch (Exception e) {
            if(e instanceof StopDetectorException) {
                log.error("触发器停止运行", e);
                internalStop();
                return;
            }else {
                log.error("调度下一次检查时发生异常", e);
                scheduleRetry();
            }
        }
    }

    /**
     * 查询密码过期信息（带异常处理）
     */
    private PwdExpireInfo queryPwdExpireInfo() {
        String driverClassName = applicationContext
                .getEnvironment().getProperty("spring.datasource.driver-class-name");
        try {
            PwdExpireInfo pwdInfo = pwdExpireSupplier.get();
            if (pwdInfo != null && pwdInfo.isCurrentIsExpireSoon()) {
                log.debug("查询到密码过期信息，需要进行密码重置,当前时间：{}", LocalDateTime.now());
            }
            return pwdInfo;
        } catch (Exception e) {
            SQLException firstSQLException = DatabaseUtils.findDeepestSQLException(e);
            if(null!=firstSQLException){
                log.error("sql 操作异常: {}", e.getMessage());
                if(DatabaseUtils.isPasswordExpiredError(firstSQLException.getErrorCode(),driverClassName)){
                    return new PwdExpireInfo(true);
                }
            }
            throw new StopDetectorException("查询密码过期信息时发生异常，停止触发器", e);
        }
    }

    /**
     * 安全执行密码处理流程
     */
    private void safeExecutePasswordFlow() {
        try {
            executePasswordFlow();
            // 只有在成功执行后才继续下一轮循环
            if (running.get()) {
                log.info("密码处理流程执行成功，开始下一轮查询调度");
                scheduler.schedule(
                        this::scheduleNextCheck,
                        30,
                        TimeUnit.SECONDS
                );
            }
        } catch (Exception e) {
            // 可以根据异常类型决定是否继续重试
            if (isRetryableException(e)) {
                log.warn("执行密码处理流程时发生可重试异常，继续下一轮调度", e);
                if (running.get()) {
                    scheduleNextCheck();
                }
            } else {
                log.error("执行密码处理流程时发生严重异常，停止触发器", e);
                // 发生严重异常时直接停止触发器
                internalStop();
            }
        }
    }

    /**
     * 执行密码处理流程
     * <p> 会根据当前上下文进行密码更新和配置刷新
     */
    private void executePasswordFlow() {
        log.info("密码过期 - 开始执行密码处理流程，当前时间: {}", LocalDateTime.now());

        try {
            log.info("执行上下文环境配置刷新和数据库密码更新");
            applicationContext.getBean(RenewPwdRefresh.class).fixPassword();
            log.info("完成上下文环境配置刷新和数据库密码更新");
        } catch (Exception e) {
            log.error("密码处理流程执行失败", e);
            throw e;
        }
    }

    /**
     * 调度重试
     */
    private void scheduleRetry() {
        if (!running.get()) {
            return;
        }
        scheduler.schedule(
                this::scheduleNextCheck,
                retryIntervalMinutes,
                TimeUnit.MINUTES
        );
    }

    /**
     * 实现 AutoCloseable 接口
     */
    @Override
    public void close() {
        internalStop();
    }

    /**
     * 判断异常是否可重试
     * 可以根据具体业务需求自定义重试逻辑
     */
    private boolean isRetryableException(Exception e) {
        // 示例：网络相关异常可以重试，业务逻辑异常不重试
        // 可以根据实际需求调整
        return e instanceof java.net.ConnectException
                || e instanceof java.sql.SQLTransientException;
    }

    /**
     * 创建构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构建器类
     */
    public static class Builder {
        /**
         * 自行注册密码过期查询方法
         */
        private Supplier<PwdExpireInfo> pwdExpireSupplier;
        /**
         * 查询失败/或者暂未过期时的重试间隔（分钟）默认5分钟
         */
        private int retryIntervalMinutes = 5;

        /**
         * 上下文环境
         */
        private ApplicationContext applicationContext;

        /**
         * 自行注册密码过期查询方法
         */
        public Builder pwdExpireSupplier(Supplier<PwdExpireInfo> supplier) {
            this.pwdExpireSupplier = supplier;
            return this;
        }

        /**
         * 上下文环境
         */
        public Builder applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        /**
         * 查询失败/或者暂未过期时的重试间隔（分钟）默认5分钟
         */
        public Builder retryIntervalMinutes(int minutes) {
            if (minutes <= 0) {
                throw new IllegalArgumentException("重试间隔必须大于0");
            }
            this.retryIntervalMinutes = minutes;
            return this;
        }

        public PwdCheckDetector build() {
            return new PwdCheckDetector(this);
        }
    }



}
