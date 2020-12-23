package com.detabes.redis.pub.server.impl;


import com.detabes.redis.pub.server.RedisReceiverServer;
import com.detabes.string.core.StringFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

/**
 * @ClassName RedisReceiverImpl
 * @description 消息监听数据展示实现了
 * @author tn
 * @date 2020-09-11 10:36
 */
@Slf4j
public class RedisReceiverImpl implements RedisReceiverServer<String> {

    /**
     * 消息接收
     * @param message
     * @return
     */
    @Override
    public String pubMessage(Message message) {
        log.info("\n----------------------------------------------------------\n\t" +
                "redis监听频道 "+new String(message.getChannel())+" 的消息\n\t" +
                "消息体："+new String(message.getBody()) + "\n" +
                "----------------------------------------------------------");
        return  message.toString();
    }
}
