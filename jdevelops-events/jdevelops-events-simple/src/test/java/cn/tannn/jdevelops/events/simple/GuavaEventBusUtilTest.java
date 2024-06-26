package cn.tannn.jdevelops.events.simple;


import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;


class GuavaEventBusUtilTest {

    public static void main(String[] args) {

        //  测试 2
        System.out.println("测试 2:"+System.currentTimeMillis());
        TestEventListener testEventListener = new TestEventListener();
        GuavaEventBusUtil.register(testEventListener);
        GuavaEventBusUtil.post(123);
        // 发布消息
        GuavaEventBusUtil.post("测试2");
        System.out.println("测试 2:"+System.currentTimeMillis());

        //  测试 3
//        GuavaEventBusUtil.register(new TestEventListener()); // 多次注册会多次监听
        GuavaEventBusUtil.asyncPost(1232);
        // 发布消息
        GuavaEventBusUtil.asyncPost("测试3");
        System.out.println("测试 3:"+ System.currentTimeMillis());

        System.out.println(System.currentTimeMillis()+",主线程执行完毕："+Thread.currentThread().getName());
        GuavaEventBusUtil.unregister(testEventListener);
    }

    /**
     * 创建监听者类
     *      监听者也是一个类，不过在这个类里面需要确定我们需要处理哪些消息(可以是多个)，每个消息的处理对应一个函数，而且这个函数需要添加@Subscribe注解
     * 命令监听者
     */
    public static class TestEventListener {

        /**
         * 如果发送了OrderMessage消息，会进入到该函数的处理
         * @param event 消息
         */
        @AllowConcurrentEvents // 这个异步注解才有效。
        @Subscribe
        public void dealWithEvent(String event) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("睡眠出错"+ e.getMessage());
            }
            // TODO: 收到EventTest消息之后，做相应的处理
            System.out.println("我收到了您的命令，命令内容为：" + event);
        }


        /**
         * 如果发送了OrderMessage消息，会进入到该函数的处理
         * @param event 消息
         */
        @Subscribe
        @AllowConcurrentEvents
        public void dealWithEvent(Integer event) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("睡眠出错"+ e.getMessage());
            }
            // TODO: 收到EventTest消息之后，做相应的处理\
            System.out.println("我收到了您的命令，命令内容为：" + event);
        }


    }
}
