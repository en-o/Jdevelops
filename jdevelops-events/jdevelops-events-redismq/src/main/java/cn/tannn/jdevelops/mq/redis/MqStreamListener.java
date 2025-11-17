//import com.sunway.papermate.modules.wiki.dao.ResourceExtractDao;
//import com.sunway.papermate.modules.wiki.service.ResourceFileService;
//import com.sunway.result.spring.SpringContextUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.connection.stream.Consumer;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.ReadOffset;
//import org.springframework.data.redis.connection.stream.StreamOffset;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.stream.StreamListener;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import org.springframework.data.redis.stream.Subscription;
//
//import java.util.Map;
//
///**
// * 业务自己消费监听器
// */
//@Slf4j
//public  class MqStreamListener implements StreamListener<String, MapRecord<String, String, String>> {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final String groupName;
//    private final String consumer;
//    private final String streamKey;
//    private final Boolean autoAck;
//
//    /**
//     * @param redisTemplate RedisTemplate
//     * @param groupName 消费组
//     * @param consumer 消费者
//     * @param streamKey mq名
//     * @param autoAck 是否自定确认
//     */
//    public MqStreamListener(RedisTemplate<String, Object> redisTemplate
//            , String groupName, String consumer
//            , String streamKey, Boolean autoAck) {
//        this.redisTemplate = redisTemplate;
//        this.groupName = groupName;
//        this.consumer = consumer;
//        this.streamKey =  streamKey;;
//        this.autoAck = autoAck;
//    }
//
//    @Override
//    public void onMessage(MapRecord<String, String, String> message) {
//        // 处理消息
//        processMessage(message);
//    }
//
//    private void processMessage(MapRecord<String, String, String> message) {
//        log.info("========MqStreamListener====================");
//        long startTime = System.currentTimeMillis();
//
//        try {
//            ResourceFileService resourceFileService = SpringContextUtils.getInstance().getBean(ResourceFileService.class);
//            ResourceExtractDao resourceExtractDao = SpringContextUtils.getInstance().getBean(ResourceExtractDao.class);
//            // 更新文件状态
//            Map<String, String> value = message.getValue();
//            Long fileId = Long.valueOf(value.get("fileId"));
//            Integer fileStatus = Integer.valueOf(value.get("fileStatus"));
//            String reason = value.get("reason");
//
//            resourceFileService.updateStatus(fileId, fileStatus, reason);
//            if(message.getStream().equalsIgnoreCase(MqConstants.TOPIC_PREFIX+ MqConstants.PARSE_OK_TOPIC)){
//                // 设置文件提取状态为未提取，因为解析成功后才有md文件
//                resourceExtractDao.updateExtractStatus(0, fileId);
//            } else if (message.getStream().equalsIgnoreCase(MqConstants.TOPIC_PREFIX+ MqConstants.ERROR_TOPIC)) {
//                resourceExtractDao.updateExtractStatus(-2, fileId);
//            }
//
//            // 非自动确认则手动确认
//            if (!autoAck) {
//                // 手动确认消息
//                redisTemplate.opsForStream().acknowledge(groupName, message);
//                long duration = System.currentTimeMillis() - startTime;
//                log.info("✅ 消息处理成功，耗时: {}ms", duration);
//            }else {
//                log.warn("🤧 消息由receiveAutoAck自动确认");
//            }
//        } catch (Exception e) {
//            log.error("处理消息失败: {}", e.getMessage());
//        }
//    }
//
//
//    /**
//     * 加载文件解析监听器
//     */
//    public Subscription loadListener(StreamMessageListenerContainer<String, MapRecord<String, String, String>> container){
//        // 创建解析队列监听器
//
//        // 组消费 -  必须先创建Consumer Group
//        MqUtil.initConsumerGroup(redisTemplate
//                , new ResMqProperties()
//                , streamKey
//                , groupName, ReadOffset.from("0-0"));
//
//        // 使用receive方法（需要手动ACK）， receiveAutoAck 可以自定
//        // https://docs.spring.io/spring-data/redis/reference/3.4/redis/redis-streams.html#redis.streams.acknowledge
//        if (autoAck) {
//           return container.receive(
//                   Consumer.from(groupName, consumer),
//                   StreamOffset.create(MqConstants.TOPIC_PREFIX+streamKey, ReadOffset.lastConsumed()),
//                   this);
//        } else {
//
//            return container.receiveAutoAck(
//                    Consumer.from(groupName, consumer),
//                    StreamOffset.create(MqConstants.TOPIC_PREFIX+streamKey, ReadOffset.lastConsumed()),
//                    this);
//        }
//    }
//
//}
