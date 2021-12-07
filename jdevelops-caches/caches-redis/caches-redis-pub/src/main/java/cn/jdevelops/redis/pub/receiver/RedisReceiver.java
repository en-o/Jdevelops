package cn.jdevelops.redis.pub.receiver;

import cn.jdevelops.redis.pub.server.RedisReceiverServer;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 承MessageListener，就能拿到消息体和频道名
 * @author tn
 * @version 1
 * @date 2020/7/3 9:19
 */
@Component
public class RedisReceiver implements MessageListener {

    @Resource
    private RedisReceiverServer redisReceiverServer;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        // TODO 收到消息之后执行方法
        redisReceiverServer.pubMessage(message);
    }
}
