package cn.tannn.jdevelops.events.redis.config;

import cn.tannn.jdevelops.events.redis.receiver.RedisReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

/**
 * (消息订阅)
 *  默认监听器，也可以自己重写
 *  配置监听适配器、消息监听容器
 *  public class RedisListenDefault extends RedisCacheConfig
 *
 * @author tnnn
 */
@EnableCaching
public class RedisCacheListen {

    private static final Logger LOG = LoggerFactory.getLogger(RedisCacheListen.class);

    @Autowired
    private RedisEventConfig reidsCacheBean;


    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机
        // 消息监听容器增加监听的消息，第一个参数是监听适配器，第2个参数是监听的频道。
        List<String> patternTopic = reidsCacheBean.getPatternTopic();
        if(patternTopic==null||patternTopic.isEmpty()){
            container.addMessageListener(listenerAdapter, new PatternTopic("test"));
        }else{
            patternTopic.forEach(str -> container.addMessageListener(listenerAdapter, new PatternTopic(str)));
        }

        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiver receiver
     * @return MessageListenerAdapter
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
        LOG.info("默认消息适配器");
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }



    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }



}
