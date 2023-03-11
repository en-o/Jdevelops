package cn.jdevelops.redis.pub.server.impl;


import cn.jdevelops.redis.pub.server.RedisReceiverServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;

/**
 * 消息监听数据展示实现了
 * @author tn
 * @date 2020-09-11 10:36
 */
public class RedisReceiverImpl implements RedisReceiverServer<String> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisReceiverImpl.class);
    /**
     * 消息接收
     * @param message message
     * @return String
     */
    @Override
    public String pubMessage(Message message) {
        LOG.info("\n----------------------------------------------------------\n\t" +
                "redis监听频道 "+new String(message.getChannel())+" 的消息\n\t" +
                "消息体："+new String(message.getBody()) + "\n" +
                "----------------------------------------------------------");
        return  message.toString();
    }
}
