package cn.jdevelops.delay.jdk;

import cn.jdevelops.delay.constant.DelayQueueConstant;
import cn.jdevelops.delay.core.factory.DelayFactory;
import cn.jdevelops.delay.core.service.DelayService;
import cn.jdevelops.delay.task.DelayTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jdk延时队列
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:34
 */
@Service
public class JdkDelayServiceImpl implements DelayService<DelayTask> {

    private static final Logger logger = LoggerFactory.getLogger(JdkDelayServiceImpl.class);
    /**
     * 线程池
     */
    private static final String NAME = "RemindMessageTask-thread-";
    private final AtomicInteger seq = new AtomicInteger(1);
    private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, r ->
            new Thread(r, NAME + seq.getAndIncrement()));

    @Resource
    private DelayFactory<DelayTask> delayRunFactory;

    @Override
    public void produce(DelayTask delayMessage) {
        DelayQueueConstant.DELAY_QUEUE.put(delayMessage);
    }

    @Override
    public void produce(List<DelayTask> delayMessage) {
        // 定义一个延时队列
        DelayQueue<DelayTask> queue = DelayQueueConstant.DELAY_QUEUE;
        delayMessage.forEach(task ->  queue.put(task));
    }

    @Override
    public void consumeDelay() {
        // IllegalArgumentException 的话 initialDelay = 1， period = 1 直接写死
        // 初始化
        long initialDelay =Math.round(Math.random()*10+10);
        // 周期 周期小于或等于零时会抛异常
        long period = Math.round(Math.random()*10);
        logger.info("开始消费延时队列数据...");
        pool.scheduleAtFixedRate(()->{
            try {
                DelayQueue<DelayTask> queue  = DelayQueueConstant.DELAY_QUEUE;
                if (!queue.isEmpty()){
                    DelayTask delayTask = queue.take();
                    delayRunFactory.delayExecute(delayTask);
                }
            }catch (Exception e){
                logger.error("execute function error..",e);
            }
        }, initialDelay,period, TimeUnit.SECONDS);
    }
}
