package com.detabes.redis.pub.server;

import com.detabes.string.core.StringFormat;
import org.springframework.data.redis.connection.Message;

/**
 * @ClassName  ReisReciverServer
 * @description  消息订阅   ---  处理接收到的数据0
 * @author  tn
 * @date 2020-09-11 10:11
 */
public interface RedisReceiverServer<T> {

    /**
     *  消息接收
     * @param message
     * @return
     */
    default T pubMessage(Message message){
        StringFormat.consoleLog("\n----------------------------------------------------------\n\t" +
                 "redis监听频道 "+new String(message.getChannel())+" 的消息\n\t" +
                 "消息体："+new String(message.getBody()) + "\n" +
                 "----------------------------------------------------------");
         return (T) message.toString();
    };
}
