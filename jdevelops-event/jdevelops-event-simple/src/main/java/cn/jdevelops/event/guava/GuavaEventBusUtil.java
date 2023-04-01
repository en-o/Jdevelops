package cn.jdevelops.event.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;

/**
 * 事件总线工具类 来源
 *  <a href="https://blog.csdn.net/qq_38345296/article/details/100539989">事件总线工具类</a>
 * @author fengjiale
 **/
public class GuavaEventBusUtil {
    private static volatile EventBus eventBus;
    private static volatile AsyncEventBus asyncEventBus;
    private static final Executor EXECUTOR = command -> new Thread(command).start();


    private GuavaEventBusUtil() {
    }

    /**
     * 双重锁单例模式
     * @return AsyncEventBus
     */
    private static AsyncEventBus getAsynEventBus(){
        if(asyncEventBus==null){
            synchronized (AsyncEventBus.class){
                if(asyncEventBus==null){
                    asyncEventBus = new AsyncEventBus(EXECUTOR);
                }
            }
        }
        return asyncEventBus;
    }

    /**
     * 双重锁单例模式
     * @return EventBus
     */
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

    /**
     * 同步发送事件
     * @param event 事件
     */
    public static void post(Object event){
        getEventBus().post(event);
    }

    /**
     * 异步方式发送事件
     * @param event 事件
     */
    public static void asyncPost(Object event){
        getAsynEventBus().post(event);
    }

    /**
     * 注册监听器（即对事件的处理方法
     * @param eventListener EventListener  @Subscribe
     */
    public static void register(Object eventListener){
        getEventBus().register(eventListener);
        getAsynEventBus().register(eventListener);
    }

    /**
     * 注销监听器
     * @param eventListener EventListener  @Subscribe
     */
    public static void unregister(Object eventListener) {
        getEventBus().unregister(eventListener);
        getAsynEventBus().unregister(eventListener);
    }

}
