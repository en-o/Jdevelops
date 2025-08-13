package cn.tannn.jdevelops.renewpwd;

/**
 * 密码探测器
 *
 * 用于定时检测数据库账户密码是否即将过期。
 * 通过自定义线程实现可控（启动、关闭、重启）的定时检测。
 * 具体的密码检测逻辑需通过重写 checkPwdExpire() 方法实现。
 *
 * <pre>
 * &#47;**
 *  * 自定义密码检测器示例
 *  *&#47;
 * public class MyPwdCheckDetector extends PwdCheckDetector {
 *     &#64;Override
 *     protected void checkPwdExpire() {
 *         // 这里实现数据库密码过期检测逻辑
 *         // 例如：用JDBC查询密码有效期
 *     }
 * }
 *
 * // 使用方式
 * MyPwdCheckDetector detector = new MyPwdCheckDetector();
 * detector.start();   // 启动检测
 * // detector.stop(); // 停止检测
 * // detector.restart(); // 重启检测
 * </pre>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/8/13 16:27
 */
public class PwdCheckDetector {

    /**
     * 控制线程运行状态的标志
     */
    private volatile boolean running = false;

    /**
     * 探测线程对象
     */
    private Thread detectorThread;

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
     * 密码过期检测逻辑
     * 需由业务实现，负责查询数据库判断密码是否即将过期。
     * 可使用 JDBC、MyBatis、JPA、JdbcTemplate 等方式。
     */
    protected void checkPwdExpire() {
        // TODO: 查询数据库，判断密码是否即将过期
        // 可用JDBC/MyBatis/JPA/JdbcTemplate等
    }
}
