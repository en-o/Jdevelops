package cn.jdevelops.delay.jdk;

import java.util.List;

/**
 * jdk延时队列
 *  优点： 效率高，低延迟
 *  缺点： 宕机丢失（可以存数据库），不利于集群扩展
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:33
 */
public interface DelayService {

    /**
     * 生产延迟队列消息
     * @param delayMessage 消息
     */
    void produce(DelayTask delayMessage);

    /**
     * 生产延迟队列消息
     * @param delayMessage 消息
     */
    void produce(List<DelayTask> delayMessage);

    /**
     * 消费延迟队列数据
     * @exception InterruptedException 消费异常
     */
    void consumeDelay() throws InterruptedException;
}
