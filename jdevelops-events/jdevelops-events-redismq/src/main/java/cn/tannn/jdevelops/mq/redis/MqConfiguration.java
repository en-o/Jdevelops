//import jakarta.annotation.PreDestroy;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import org.springframework.data.redis.stream.Subscription;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 业务自己启动监听器 跟MqConfiguration配合使用
// *
// * @author <a href="https://t.tannn.cn/">tan</a>
// * @version V1.0
// * @date 2025/9/23 11:42
// */
//@Configuration
//@Slf4j
//public class MqConfiguration implements ApplicationRunner {
//
//    @Autowired
//    private RedisConnectionFactory connectionFactory;
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//    @Resource
//    private ResMqProperties resMqProperties;
//
//    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;
//    private final List<Subscription> subscriptions = new ArrayList<>();
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
////        // 创建上传队列 - 不需要监听只需要创建的
//        MqUtil.ensureStreamExists(redisTemplate,resMqProperties,MqConstants.UPLOAD_TOPIC);
//        MqUtil.ensureStreamExists(redisTemplate,resMqProperties,MqConstants.WAIT_TRANS_TOPIC);
//
//        // 创建 container
//        createMqContainer();
//
//        // 监听+队列创建
//        // 解析队列监听 -- 新增监听抄这个在后面追加，或者在MqStreamListener里处理不同的steam
//        subscriptions.add(new MqStreamListener(
//            redisTemplate
//                ,resMqProperties.getStreamKeyGroupPair().getLeft()
//                ,resMqProperties.getStreamKeyGroupPair().getRight()
//                ,MqConstants.ERROR_TOPIC
//                ,resMqProperties.getAutoAck()
//        ).loadListener(container));
//        subscriptions.add(new MqStreamListener(
//                redisTemplate
//                ,resMqProperties.getStreamKeyGroupPair().getLeft()
//                ,resMqProperties.getStreamKeyGroupPair().getRight()
//                ,MqConstants.PARSE_OK_TOPIC
//                ,resMqProperties.getAutoAck()
//        ).loadListener(container));
//        subscriptions.add(new MqStreamListener(
//                redisTemplate
//                ,resMqProperties.getStreamKeyGroupPair().getLeft()
//                ,resMqProperties.getStreamKeyGroupPair().getRight()
//                ,MqConstants.PARSE_RUN_TOPIC
//                ,resMqProperties.getAutoAck()
//        ).loadListener(container));
//        subscriptions.add(new MqStreamListener(
//                redisTemplate
//                ,resMqProperties.getStreamKeyGroupPair().getLeft()
//                ,resMqProperties.getStreamKeyGroupPair().getRight()
//                ,MqConstants.TRANS_OK_TOPIC
//                ,resMqProperties.getAutoAck()
//        ).loadListener(container));
//        subscriptions.add(new MqStreamListener(
//                redisTemplate
//                ,resMqProperties.getStreamKeyGroupPair().getLeft()
//                ,resMqProperties.getStreamKeyGroupPair().getRight()
//                ,MqConstants.TRANS_RUN_TOPIC
//                ,resMqProperties.getAutoAck()
//        ).loadListener(container));
//        container.start();
//        log.info("✅ Redis stream 监听器已启动");
//    }
//
//
//    /**
//     * 创建队列容器
//     */
//    public void createMqContainer(){
//        // 配置容器
//        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
//                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
//                        .builder()
//                        .pollTimeout(Duration.ofMillis(resMqProperties.getPollTimeout()))
//                        .batchSize(resMqProperties.getBatchSize())
//                        .build();
//        container = StreamMessageListenerContainer.create(connectionFactory, options);
//    }
//
//
//
//
//    @PreDestroy
//    public void stop() {
//        subscriptions.forEach(Subscription::cancel);
//        container.stop();
//        log.warn("🛑 所有监听器已停止");
//    }
//}
