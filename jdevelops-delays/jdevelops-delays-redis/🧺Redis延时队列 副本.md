> > 1. 出发点
>    1. 在业务上很多时候用到了延时改变状态，有时候一些小项目也要。
>    2. 最开始一切使用springboot的定时器（@Scheduled）做，后面改成了xxl_job进行定时调度
>    3. 后面改成了 [JDK延时队列](https://www.yuque.com/tanning/yg9ipo/syt07woc71l2p8vn?view=doc_embed)
>    4. 由于不好管理内存中的数据，所以到了redis中
> 2. 其它说明
>    1. 使用了lua脚本来源于 https://blog.csdn.net/Dongguabai/article/details/114001220
>    2. 延时数据可见可控
>    3. 可做集群
> 3. 注意
>    1. 延时数据不会重复，重复的 DelayQueueMessage 会被覆盖
>    2. 只导入依赖不进行`DelayExecute`实现`EnableAutoScanDelayCore#delayFactory` 会报错

# **引入依赖**
```xml
 <!-- 光引包不使用会报错，我还在看怎么解决  -->
<dependency>
    <groupId>cn.tannn.jdevelops</groupId>
    <artifactId>jdevelops-delays-redis</artifactId>
    <version>${jdevelops.version}</version>
</dependency>
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
    <version>2.0.29</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
# 配置和使用
> 一定要写的内容如下

## Channel 常量
> 这个**根据业务定义**，下面的 `DelayExecute`要用

```java
/**
* 延迟队列类型
*
* @author tnnn
* @version V1.0
* @date 2023-01-04 15:39
*/
public interface JdkDelayMessageChannel {

    /**
    * 支付 延迟通知
    */
    String PAY = "pay";

    /**
    * 活动 延迟通知
    */
    String ACTIVITY = "activity";

}
```
## 构建消费动作
> 延迟来了之后要执行的方法**,根据业务定义**，实现DelayExecute

### 活动延迟队列到期后要执行的方法
```java


/**
* 活动状态改变
*
* @author tnnn
* @version V1.0
* @date 2023-01-04 15:59
*/
@Service(JdkDelayMessageChannel.ACTIVITY)
@RequiredArgsConstructor
public class ActivityDelayExecute implements DelayExecute {

    private static final Logger logger = LoggerFactory.getLogger(ActivityDelayExecute.class);


    @Override
    public String channel() {
        return JdkDelayMessageChannel.ACTIVITY;
    }

    @Override
    public void delayExecute(DelayQueueMessage delayMessage) {
        logger.info("===> (" +RedisDelayMessageChannel.ACTIVITY+" ) 定时任务开始执行: getBody:{},channel:{}, time:{}",
            delayMessage.getBody(),
            delayMessage.getChannel(),
            delayMessage.getDelayTimeStr());
    }


}
```

### 支付延迟队列到期后要执行的方法
```java


/**
* 支付通知
*
* @author tnnn
* @version V1.0
* @date 2023-01-04 15:59
*/
@Service(JdkDelayMessageChannel.PAY)
@RequiredArgsConstructor
public class PayDelayExecute implements DelayExecute {

    private static final Logger logger = LoggerFactory.getLogger(PayDelayExecute.class);


    @Override
    public String channel() {
        return JdkDelayMessageChannel.PAY;
    }

    @Override
    public void delayExecute(DelayQueueMessage delayMessage) {
        logger.info("===> (" +RedisDelayMessageChannel.PAY+" ) 定时任务开始执行: getBody:{},channel:{}, time:{}",
                    delayMessage.getBody(),
                    delayMessage.getChannel(),
                    delayMessage.getDelayTimeStr());
    }


}
```
## 生产延时队列
```java

/**
 * 延时
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-08 00:18
 */
@RestController
@Api(tags = {"测试redis延时"})
public class DelayController {
    @Autowired
    private DelayService<DelayQueueMessage> redisDelayService;

    @ApiOperation("生产延时队列数据")
    @GetMapping("produce")
    public String produce(Long timeMillis) {
        Long paramTime = timeMillis == null ? System.currentTimeMillis() : timeMillis;
        //  填充延时队列数据
        List<DelayQueueMessage> delayTasks = Arrays.asList(
                new DelayQueueMessage("body1", RedisDelayMessageChannel.ACTIVITY,
                        paramTime + (10 * 1000), new Date(paramTime + (10 * 1000)).toString(), ""),
                new DelayQueueMessage("body2", RedisDelayMessageChannel.ACTIVITY,
                        paramTime + (20 * 1000), new Date(paramTime + (20 * 1000)).toString(), ""),
                new DelayQueueMessage("body3", RedisDelayMessageChannel.PAY,
                        paramTime + (30 * 1000), new Date(paramTime + (30 * 1000)).toString(), ""),
                new DelayQueueMessage("body4", RedisDelayMessageChannel.PAY,
                        paramTime + (40 * 1000), new Date(paramTime + (40 * 1000)).toString(), ""),
                new DelayQueueMessage("body5", RedisDelayMessageChannel.ACTIVITY,
                        paramTime + (50 * 1000), new Date(paramTime + (50 * 1000)).toString(), "")
                );
        redisDelayService.produce(delayTasks);
        return "生产延时队列数据... => "+paramTime;
    }
    
}
```


# 接口参考
> 具体使用时： 在spring启动时就开始消费，在具体业务里进行生产延时任务数据

## 主动调用运行
```java


/**
 * 延时
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-08 00:18
 */
@RestController
@Api(tags = {"测试redis延时"})
public class DelayController {
    @Autowired
    private DelayService<DelayQueueMessage> redisDelayService;

    @ApiOperation("开始消费延时队列")
    @GetMapping("consume")
    public String consume() {
        redisDelayService.consumeDelay();
        return "开始消费延时队列数据...";
    }
}
```
## 启动运行
```java
@Configuration
@Slf4j
public class DelayInit implements ApplicationRunner {

    @Autowired
    private DelayService<DelayQueueMessage> redisDelayService;

    @Override
    public void run(ApplicationArguments args) {
        log.debug(" ===> delay stating ...");
        // 启动就开始消费延迟队列数据
        redisDelayService.consumeDelay();
    }
}

```
# **示例项目地址**
[https://github.com/en-o/Jdevelops-Example/tree/main/delay/delay-redis/src](https://github.com/en-o/Jdevelops-Example/tree/main/delay/delay-redis/src)


# 其他
## 重构内置延时方法
> 比如报错之类的不想改底层实现，那就重写吧 (实现DelayService<DelayTask>)
> - 我最开始scheduleAtFixedRate就出现了不定时的启动异常（IllegalArgumentException）
> - 生产数据时我最开始遇到了重复数据
> - 所以写了这个说明，自己重写吧

```java


/**
 * 重构内置的 JdkDelayService
 * @author tnnn
 * @version V1.0
 * @date 2023-01-05 16:34
 */
@Service
@Slf4j
public class RefactorJdkDelayService implements DelayService<DelayTask> {


    @Resource
    private DelayFactory<DelayTask> delayFactory;

    /**
     * 线程池
     */
    private static final String NAME = "RefactorJdkDelayMessageTask-thread-";
    private final AtomicInteger seq = new AtomicInteger(1);
    private final ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, r ->
            new Thread(r, NAME + seq.getAndIncrement()));


    @Override
    public void produce(DelayTask delayMessage) {
        // 生产的实现可以参考 RedisDelayService 
    }

    @Override
    public void produce(List<DelayTask> delayMessage) {
       // 生产的实现可以参考 JdkDelayService 
    }

    @Override
    public void consumeDelay() {
       // 消费的实现可以参考 RedisDelayService 
    }
}

```
## 构建自己的消费对象
> Redis 才能使用 - 继承 `DelayQueueMessage` ， 也可以用原生的`DelayQueueMessage`
> - 看情况，我这里由于还要查库所有就没有用body，新构建了一个重要了 dataId的对象
> - 跟上面的重构内置延时方法差不多，只是不需要去重写 `RedisDelayService`

### CustomDelayQueueMessage
```java
/**
 * 自定义消息体
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/22 13:27
 */
@Getter
@Setter
@ToString(callSuper = true) // 父类tostring
public class CustomDelayQueueMessage extends DelayQueueMessage {

    /**
     * 数据ID
     */
    private Long dataId;

    public CustomDelayQueueMessage(Long dataId,
                                String channel,
                                Long delayTime,
                                String desc) {
        super(dataId+desc, channel, delayTime, getDateTimeStr(delayTime), desc);
        this.dataId = dataId;
    }


    /**
     * yyyy-MM-dd, HH:mm:ss 格式的字符串转为long
     * @param timestamp 时间戳
     * @return long
     */
    public static String getDateTimeStr(Long timestamp) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.print(timestamp);
    }
}
```
### 重新加载 `RedisDelayService`
```java


/**
 * @author tnnn
 */
@SpringBootApplication
public class DelayRedisApplication implements ApplicationRunner {

    private RedisDelayService<CustomDelayQueueMessage> redisDelayService;

    public static void main(String[] args) {
        SpringApplication.run(DelayRedisApplication.class, args);
    }

    /**
     * 重新加载 bean
     * @return DelayService
     */
    @Bean
    public DelayService<CustomDelayQueueMessage> delayService() {
        this.redisDelayService = new RedisDelayService<>(CustomDelayQueueMessage.class);
        return redisDelayService;
    }

    /**
     * 启动就开始消费延迟队列数据
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 启动就开始消费延迟队列数据
        redisDelayService.consumeDelay();
    }
}

```
### 使用
```java
/**
 * 延时
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-08 00:18
 */
@RestController
@Tag(name = "测试redis延时")
public class DelayController {
    @Autowired
    private DelayService<CustomDelayQueueMessage> delayService;

    @Operation(summary = "开始消费延时队列")
    @GetMapping("consume")
    public String consume() {
        delayService.consumeDelay();
        return "开始消费延时队列数据...";
    }


    @Operation(summary = "生产延时队列数据,测试自定义消息体")
    @GetMapping("produce/custom_message")
    @Parameter(name = "timeMillis",description = "延时消费的时间(时间戳<毫秒>)(默认当前+n/s)")
    @Parameter(name = "dataId",description = "数据ID")
    public String produce_custom_message(@RequestParam("dataId") Long dataId, Long timeMillis) {
        Long paramTime = timeMillis == null ? System.currentTimeMillis() : timeMillis;
        //  填充延时队列数据
        delayService.produce( new CustomDelayQueueMessage(dataId, RedisDelayMessageChannel.FAIL,
                paramTime + (10 * 1000), "测试自定义消息体"));
        return "生产延时队列数据... => "+paramTime;
    }
}

```
```java


/**
 * 
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-04 15:59
 */
@Service(RedisDelayMessageChannel.FAIL)
@RequiredArgsConstructor
public class FailDelayExecute_custom_dq_message implements DelayExecute<CustomDelayQueueMessage> {

    private static final Logger logger = LoggerFactory.getLogger(FailDelayExecute_custom_dq_message.class);


    @Override
    public String channel() {
        return RedisDelayMessageChannel.FAIL;
    }

    @Override
    public void delayExecute(CustomDelayQueueMessage delayMessage) {
        logger.info(RedisDelayMessageChannel.FAIL+" => 定时任务开始执行: getBody:{},channel:{}, time:{} , str: {}",
                delayMessage.getBody(),
                delayMessage.getChannel(),
                delayMessage.getDelayTimeStr(),
                delayMessage);
    }


}

```
# 注意

1. 里面的取消队列只对 redis有效，且不建议使用
