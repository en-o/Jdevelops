package cn.tannn.jdevelops.events.redis.receiver;

import cn.tannn.jdevelops.events.redis.server.RedisReceiverServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;



/**
 * 承MessageListener，就能拿到消息体和频道名
 * @author tn
 * @version 1
 * @date 2020/7/3 9:19
 */
public class RedisReceiver implements MessageListener {

    @Autowired
    private RedisReceiverServer redisReceiverServer;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        // TODO 收到消息之后执行方法
        redisReceiverServer.pubMessage(message);
    }
}
