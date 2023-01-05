package cn.jdevelops.delay.jdk;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DelayTaskTest {



    @Test
    public void testDelay() throws InterruptedException {
        DelayServiceImpl delayService = new DelayServiceImpl();
        long timeMillis = System.currentTimeMillis();

        //  填充延时队列数据
        List<DelayTask> delayTasks = Arrays.asList(
                new DelayTask("test1","pay",timeMillis+(10*1000),"",""),
                new DelayTask("test1","pay",timeMillis+(20*1000),"",""),
                new DelayTask("test1","queue",timeMillis+(30*1000),"",""),
                new DelayTask("test1","queue",timeMillis+(40*1000),"",""),
                new DelayTask("test1","pay",timeMillis+(50*1000),"","")
                );
        delayService.produce(delayTasks);
        TimeUnit.MILLISECONDS.sleep(1000);
        // 开始消费延迟队列
        delayService.consumeDelay();
        // 测试需要等待子进程要不然直接就么了
        TimeUnit.MILLISECONDS.sleep(141000);
    }
}
