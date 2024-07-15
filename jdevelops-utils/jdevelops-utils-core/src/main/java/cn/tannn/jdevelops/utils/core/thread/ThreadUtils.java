package cn.tannn.jdevelops.utils.core.thread;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * thread utils.
 *
 * @author 互联网
 */
public interface ThreadUtils {

    /**
     * sleep current thread.
     *
     * @param timeUnit the time unit
     * @param time     the time
     */
    static void sleep(final TimeUnit timeUnit, final long time) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * sleep current thread.
     *
     * @param millis 挂起的毫秒数
     */
    static void sleep(final long millis) {
        if (millis > 0) {
            sleep(TimeUnit.MILLISECONDS, millis);
        }
    }


    Scheduler Default = new SchedulerImpl();

    static Scheduler getDefault() {
        if (Default.isInitialized()) {
            return Default;
        }
        int coreSize = Integer.parseInt(System.getProperty("utils.task.coreSize", "1"));
        Default.init(coreSize);
        return Default;
    }

    interface Scheduler {
        boolean isInitialized();

        /**
         * @param coreSize cpu核心数
         */
        void init(int coreSize);

        void shutdown();

        /**
         * schedule
         *
         * @param runnable function to schedule
         * @param delay 延时n毫秒开始执行
         * @param interval 每n毫秒执行一次
         * @return ScheduledFuture
         */
        ScheduledFuture<?> schedule(Runnable runnable, long delay, long interval);
    }

    class SchedulerImpl implements Scheduler {
        boolean initialized = false;
        ScheduledExecutorService executor;

        public void init(int coreSize) {
            executor = Executors.newScheduledThreadPool(coreSize);
            initialized = true;
        }

        @Override
        public void shutdown() {
            executor.shutdown();
            try {
                executor.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
                if (!executor.isTerminated()) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // ignore it
                //throw new RuntimeException(e);
            }
        }

        @Override
        public ScheduledFuture<?> schedule(Runnable runnable, long delay, long interval) {
            return executor.scheduleAtFixedRate(runnable, delay,
                    interval, java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }


}
