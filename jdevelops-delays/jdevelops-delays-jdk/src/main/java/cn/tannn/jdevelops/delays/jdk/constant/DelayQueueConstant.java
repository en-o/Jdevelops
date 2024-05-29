package cn.tannn.jdevelops.delays.jdk.constant;


import cn.tannn.jdevelops.delays.jdk.task.DelayTask;

import java.util.concurrent.DelayQueue;

/**
 * 队列常量
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:37
 */
public class DelayQueueConstant {
    /**
     *  定义一个延时队列
     */
    public static final DelayQueue<DelayTask> DELAY_QUEUE = new DelayQueue<>();
}
