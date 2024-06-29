> > 1. 出发点
>    1. 在业务上很多时候用到了延时改变状态，有时候一些小项目也要。
>    2. 最开始一切使用springboot的定时器（@Scheduled）做，后面改成了xxl_job进行定时调度
>    3. 目前手上的好几个小小的项目都要做定时改发布状态，不想在启动xxl_job了所以有了它
> 2. 其它说明
>    1. 实现 java.util.concurrent.Delayed
>    2. 优点：效率高，低延迟
>    3. 缺点：宕机丢失
>       1. 可以将数据存起来，没重启时重写写一遍数据到队列里
> 3. 注意
>    1. 延迟队列里的数据没法去重，所以数据重复就会执行两次调用，自己在sql里处理下第二次执行了没用吧也可以改用 [redis延时队列](https://www.yuque.com/tanning/yg9ipo/obcpms0qs0w9ystm/edit)


---

# **引入依赖**
```xml
<dependency>
      <groupId>cn.tannn.jdevelops</groupId>
      <artifactId>jdevelops-delays-jdk</artifactId>
      <version>0.0.1-SNAPSHOT</version>
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
        logger.info(JdkDelayMessageChannel.ACTIVITY+" => 定时任务开始执行: getBody:{},channel:{}, time:{}",
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
        logger.info(JdkDelayMessageChannel.PAY+" => 定时任务开始执行: getBody:{},channel:{}, time:{}",
                    delayMessage.getBody(),
                    delayMessage.getChannel(),
                    delayMessage.getDelayTimeStr());
    }


}
```

## 重构内置延时方法
> 比如报错之类的不想改底层实现，那就重写吧（实现 DelayService<DelayTask> ）
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
        // 生产的实现可以参考 JdkDelayService 
    }

    @Override
    public void produce(List<DelayTask> delayMessage) {
       // 生产的实现可以参考 JdkDelayService 
    }

    @Override
    public void consumeDelay() {
       // 消费的实现可以参考 JdkDelayService 
    }
}

```
# 接口参考
> 具体使用时： 在spring启动时就开始消费，在具体业务里进行生产延时任务数据

```java



/**
 * 延时
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-08 00:18
 */
@RestController
@Api(tags = {"测试jdk延时"})
public class DelayController {
    @Autowired
    private DelayService<DelayTask> jdkDelayService;

    /**
     * 建议springboot 启动时就开始消费 <>Application  implements ApplicationRunner 中执行</>
     */
    @ApiOperation("开始消费延时队列")
    @GetMapping("consume")
    public String consume() {
        jdkDelayService.consumeDelay();
        return "开始消费延时队列数据...";
    }



    @ApiOperation("生产延时队列数据")
    @GetMapping("produce")
    public String produce()  {
        long timeMillis = System.currentTimeMillis();
        //  填充延时队列数据
        List<DelayTask> delayTasks = Arrays.asList(
                new DelayTask("test1", JdkDelayMessageChannel.PAY,
                        timeMillis+(10*1000),new Date(timeMillis+(10*1000)).toString(),""),
                new DelayTask("test2",JdkDelayMessageChannel.PAY,
                        timeMillis+(20*1000),new Date(timeMillis+(20*1000)).toString(),""),
                new DelayTask("test3",JdkDelayMessageChannel.PAY,
                        timeMillis+(30*1000),new Date(timeMillis+(30*1000)).toString(),""),
                new DelayTask("test4",JdkDelayMessageChannel.PAY,
                        timeMillis+(40*1000),new Date(timeMillis+(40*1000)).toString(),""),
                new DelayTask("test5",JdkDelayMessageChannel.PAY,
                        timeMillis+(50*1000),new Date(timeMillis+(50*1000)).toString(),"")
        );
        jdkDelayService.produce(delayTasks);
        return "生产延时队列数据...";
    }
}
```
# **示例项目地址**
[https://github.com/en-o/Jdevelops-Example/tree/main/delay/delay-jdk/src/main/java/cn/tannn/delayjdk](https://github.com/en-o/Jdevelops-Example/tree/main/delay/delay-jdk/src/main/java/cn/tannn/delayjdk)
