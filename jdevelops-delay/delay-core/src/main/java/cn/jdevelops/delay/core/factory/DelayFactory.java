package cn.jdevelops.delay.core.factory;

import cn.jdevelops.delay.core.entity.DelayQueueMessage;
import cn.jdevelops.delay.core.execute.DelayExecute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 延时执行方法的工厂类
 * @author tnnn
 * @date 2023-01-04 16:54:02
 */
@Component
public class DelayFactory<T extends DelayQueueMessage> {

    private static final Logger logger = LoggerFactory.getLogger(DelayFactory.class);


    /**
     * Spring会自动将 DelayRun 接口的实现类注入到这个Map中，
     * key为bean id，
     * value值则为对应的策略实现类
     */
    @Resource
    private Map<String, DelayExecute<T>> delaydMap;


    /**
     * 执行 DelayExecute的 实现，根据 channel 查询该走哪一个实现
     * @param delayMessage 消息
     */
    public void delayExecute(T delayMessage){
         DelayExecute<T> targetCommand = delaydMap.get(delayMessage.getChannel());
         if(Objects.isNull(targetCommand)){
             logger.error("非法的频道，请使用 DelayMessageChannel 中的数据");
         }else {
             targetCommand.delayExecute(delayMessage);
         }

    }

}
