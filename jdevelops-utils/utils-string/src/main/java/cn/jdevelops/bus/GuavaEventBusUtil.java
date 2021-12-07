package cn.jdevelops.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;

/**
 * 事件总线工具类 来源
 * @link https://blog.csdn.net/qq_38345296/article/details/100539989
 * @author fengjiale
 **/
public class GuavaEventBusUtil {
    private static volatile EventBus eventBus;
    private static AsyncEventBus asyncEventBus;
    private static final Executor executor = command -> new Thread(command).start();
    //双重锁单例模式
    private static AsyncEventBus getAsynEventBus(){
        if(asyncEventBus==null){
            synchronized (AsyncEventBus.class){
                if(asyncEventBus==null){
                    asyncEventBus = new AsyncEventBus(executor);
                }
            }
        }
        return asyncEventBus;
    }
    //双重锁单例模式
    private static EventBus getEventBus(){
        if(eventBus==null){
            synchronized (EventBus.class){
                if(eventBus==null){
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus;
    }
    public static void post(Object event){
        getEventBus().post(event);
    }
    //异步方式发送事件
    public static void asyncPost(Object event){
        getAsynEventBus().post(event);
    }
    public static void register(Object object){
        getEventBus().register(object);
        getAsynEventBus().register(object);
    }
 
}