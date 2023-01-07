package cn.jdevelops.delay.core.execute;


import cn.jdevelops.delay.core.entity.DelayQueueMessage;

/**
 * 延迟来了之后要执行的方法接口，具体使用是构建实现
 * @author tnnn
 * @date 2023-01-04 15:52:42
 */
public interface DelayExecute<T extends DelayQueueMessage> {

    /**
     *  获取频道
     * @return 频道
     */
    String channel();

    /**
     * 策略执行方法
     * @param delayMessage 执行动作需要的数据
     */
    void delayExecute(T delayMessage);
}
