package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.pojo.PwdExpireInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * 密码延迟触发器（简化版）
 *
 * 基于数据驱动的延迟触发器，根据查询到的密码过期信息动态计算触发时间。
 * 工作流程：
 * 1. 查询密码过期信息
 * 2. 计算触发时间（过期时间 - 提前触发时间）
 * 3. 延迟到触发时间后执行处理方法
 * 4. 执行密码刷新
 * 5. 重新开始查询，如此循环
 *
 * 简化说明：
 * - 移除了ScheduledFuture的复杂性，使用递归调度方式
 * - 保持非阻塞特性，不会影响其他线程
 * - 代码更简洁，逻辑更清晰
 *
 * <pre>
 * // 用法示例
 *
 * &#064;Override
 * public void run(ApplicationArguments args) throws Exception {
 *	 log.info("密码续命触发器启动");
 *	 try (PwdCheckDetector detector = PwdCheckDetector.builder()
 *			// 这里的当前密码可能不是spring.datasource.password，看怎么处理一下
 *			.pwdExpireSupplier(() -> new PwdExpireInfo(currentPassword, checkPassword()))
 *		    // .retryIntervalMinutes(5) // 探测间隔，默认5分钟
 *			.build()) {
 *		// 触发器会自动循环运行
 *		detector.start();
 *	 } catch (Exception e) {
 *		log.error("密码续命触发器启动失败", e);
 *	 }
 *	 checkPassword();
 * }
 * </pre>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V3.0
 * @date 2025/8/13 16:27
 */
public class PwdCheckDetector implements AutoCloseable, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(PwdCheckDetector.class);

    /**
     * 控制运行状态的原子布尔值
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 定时执行服务
     * 注意：简化版本中移除了ScheduledFuture，直接使用递归调度
     */
    private ScheduledExecutorService scheduler;

    /**
     * 密码过期信息查询方法 和 处理方法
     */
    private final Supplier<PwdExpireInfo> pwdExpireSupplier;

    /**
     *  查询失败/或者暂未过期时的重试间隔（分钟）
     */
    private final int retryIntervalMinutes;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private PwdCheckDetector(Builder builder) {
        this.pwdExpireSupplier = builder.pwdExpireSupplier;
        this.retryIntervalMinutes = builder.retryIntervalMinutes;

        // 验证必要参数
        if (this.pwdExpireSupplier == null) {
            throw new IllegalArgumentException("pwdExpireSupplier cannot be null");
        }
    }

    /**
     * 启动触发器
     */
    public synchronized boolean start() {
        if (!running.compareAndSet(false, true)) {
            log.warn("密码续命触发器已经在运行中");
            return false;
        }

        try {
            // 创建单线程调度器
            scheduler = new ScheduledThreadPoolExecutor(1, r -> {
                // 创建线程时设置名称和异常处理
                Thread thread = new Thread(r, "PwdCheckDetector-Thread");
                // 守护线程，确保应用退出时可以自动结束且不会被jvm回收
                thread.setDaemon(true);
                thread.setUncaughtExceptionHandler((t, e) ->
                        log.error("密码触发器线程异常", e));
                return thread;
            });

            // 立即开始第一次查询和调度（简化版：直接开始循环，不需要保存ScheduledFuture）
            scheduleNextCheck();

            log.info("密码续命触发器已启动，每隔 {} 分钟触发一次", retryIntervalMinutes);
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
     * 停止触发器
     */
    public synchronized boolean stop() {
        if (!running.compareAndSet(true, false)) {
            log.debug("密码续命触发器已经停止");
            return false;
        }

        try {
            // 简化版本：不需要取消具体任务，直接关闭调度器即可
            // 关闭调度器
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                        log.warn("强制关闭密码续命触发器调度器");
                    }
                } catch (InterruptedException e) {
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
        stop();
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
     * 这是整个触发器的核心循环方法
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
                scheduler.submit(() -> safeExecutePasswordFlow(pwdInfo));
            }
        } catch (Exception e) {
            log.error("调度下一次检查时发生异常", e);
            scheduleRetry();
        }
    }

    /**
     * 查询密码过期信息（带异常处理）
     */
    private PwdExpireInfo queryPwdExpireInfo() {
        try {
            PwdExpireInfo pwdInfo = pwdExpireSupplier.get();
            if (pwdInfo != null && pwdInfo.isCurrentIsExpireSoon()) {
                log.debug("查询到密码过期信息，需要进行密码重置,当前时间：{}", LocalDateTime.now());
            }
            return pwdInfo;
        } catch (Exception e) {
            log.error("查询密码过期信息时发生异常", e);
            return null;
        }
    }

    /**
     * 安全执行密码处理流程
     */
    private void safeExecutePasswordFlow(PwdExpireInfo pwdInfo) {
        try {
            executePasswordFlow(pwdInfo);
        } catch (Exception e) {
            log.error("执行密码处理流程时发生异常", e);
        } finally {
            // 无论成功还是失败，都要继续下一轮循环
            if (running.get()) {
                log.info("密码处理流程完成，开始下一轮查询调度");
                scheduleNextCheck();
            }
        }
    }

    /**
     * 执行密码处理流程
     * 流程：处理方法 → 刷新方法 → 重新开始循环
     */
    private void executePasswordFlow(PwdExpireInfo pwdInfo) {
        log.info("密码过期 - 开始执行密码处理流程，当前时间: {}", LocalDateTime.now());

        try {
            log.info("执行上下文环境配置刷新和数据库密码更新");
            applicationContext.getBean(RenewPwdRefresh.class).fixPassword(pwdInfo.getNewPassword());
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
        // 简化版：不需要保存ScheduledFuture引用
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
        stop();
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
        private Supplier<PwdExpireInfo> pwdExpireSupplier;
        private int retryIntervalMinutes = 5; // 默认查询失败5分钟后重试

        /**
         * 设置密码过期信息查询和处理方法
         */
        public Builder pwdExpireSupplier(Supplier<PwdExpireInfo> supplier) {
            this.pwdExpireSupplier = supplier;
            return this;
        }

        /**
         * 设置查询失败时的重试间隔（分钟）
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
