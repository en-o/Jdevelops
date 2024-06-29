package cn.tannn.jdevelops.delays.jdk.task;


import cn.tannn.jdevelops.delays.core.entity.DelayQueueMessage;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列 task
 *  重写 Delayed
 *  优点： 效率高，低延迟
 *  缺点： 宕机丢失（可以存数据库），不利于集群扩展
 * @author tnnn
 * @version V1.0
 * @date 2023-01-03 15:24
 */

public class DelayTask extends DelayQueueMessage implements Delayed {

    public DelayTask() {
    }

    public DelayTask(String body, String channel, Long delayTime, String delayTimeStr, String desc) {
        super(body, channel, delayTime, delayTimeStr, desc);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // < 0 便执行
        return getDelayTime()-System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        // 实现 比对方法
        DelayTask delayTask = (DelayTask)o;
        return getDelayTime() - delayTask.getDelayTime() <= 0 ?-1:1;
    }


}
