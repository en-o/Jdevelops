package cn.tannn.jdevelops.renewpwd;

import cn.tannn.jdevelops.renewpwd.pojo.PwdExpireInfo;
import cn.tannn.jdevelops.renewpwd.util.PwdRefreshUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 密码探测器
 *
 * 用于定时检测数据库账户密码是否即将过期。
 * 通过自定义线程实现可控（启动、关闭、重启）的定时检测。
 * 具体的密码检测逻辑需通过重写 checkPwdExpire() 方法实现。
 *
 * <pre>
 * // 用法示例
 * PwdCheckDetector detector = new PwdCheckDetector(
 *     () -> {
 *         // 查询数据库，返回 PwdExpireInfo
 *         return new PwdExpireInfo(
 *             "主密码",
 *             "新密码",
 *             true,
 *             LocalDateTime.now().plusMinutes(15)
 *         );
 *     },
 *     info -> {
 *         // 密码修复过程回调，可记录日志或通知
 *         System.out.println("已修复密码类型: " + info.getType());
 *     },
 *     renewPwdRefresh // 你的 RenewPwdRefresh 实例
 * );
 * detector.start();
 * </pre>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/13 16:27
 */
public class PwdCheckDetector {
    private static final Logger log = LoggerFactory.getLogger(PwdCheckDetector.class);
    /**
     * 控制线程运行状态的标志
     */
    private volatile boolean running = false;

    /**
     * 探测线程对象
     */
    private Thread detectorThread;

    /**
     * 密码过期信息提供者
     * 通过 Supplier 接口获取密码过期信息。
     * 具体实现由调用方提供，通常是查询数据库或其他数据源。
     */
    private final Supplier<PwdExpireInfo> pwdExpireSupplier;

    /* * 密码修复操作
     * 通过 Consumer 接口处理密码修复逻辑。
     * 具体实现由调用方提供，通常是更新数据库中的密码。
     */
    private final Consumer<PwdExpireInfo> onFixPassword;

    private final RenewPwdRefresh renewPwdRefresh;

    public PwdCheckDetector(Supplier<PwdExpireInfo> pwdExpireSupplier, Consumer<PwdExpireInfo> onFixPassword, RenewPwdRefresh renewPwdRefresh) {
        this.pwdExpireSupplier = pwdExpireSupplier;
        this.onFixPassword = onFixPassword;
        this.renewPwdRefresh = renewPwdRefresh;
    }


    /**
     * 启动探测线程
     * 若已启动则不重复启动。
     * 线程每分钟执行一次密码过期检测逻辑。
     */
    public synchronized void start() {
        if (running) return;
        running = true;
        detectorThread = new Thread(() -> {
            while (running) {
                try {
                    // 执行密码过期检测
                    checkPwdExpire();
                    // 每分钟执行一次
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    // 响应中断，安全退出
                    Thread.currentThread().interrupt();
                }
            }
        }, "PwdCheckDetector-Thread");
        // 设置为守护线程，随主程序退出
        detectorThread.setDaemon(true);
        detectorThread.start();
    }

    /**
     * 停止探测线程
     * 设置 running 为 false，并中断线程。
     */
    public synchronized void stop() {
        running = false;
        if (detectorThread != null) {
            detectorThread.interrupt();
        }
    }

    /**
     * 重启探测线程
     * 先停止再启动。
     */
    public synchronized void restart() {
        stop();
        start();
    }

    /**
     * 检查密码是否即将过期，并自动续期
     */
    protected void checkPwdExpire() {
        PwdExpireInfo pwdInfos = pwdExpireSupplier.get();
        if (pwdInfos != null && pwdInfos.getExpireTime() != null) {
            LocalDateTime now = LocalDateTime.now();

            // 触发时间点：密码过期前10分钟
            LocalDateTime triggerTime = pwdInfos.getExpireTime().minusMinutes(10);
            if (now.isAfter(triggerTime) && pwdInfos.isExpireSoon()) {
                renewPwdRefresh.fixPassword(pwdInfos.getNewPassword());
                if (onFixPassword != null) {
                    onFixPassword.accept(pwdInfos);
                }
            }else {
                log.info("当前密码未到期或不需要续期，当前时间: {}, 过期时间: {}", now, pwdInfos.getExpireTime());
            }
        }
    }
}
