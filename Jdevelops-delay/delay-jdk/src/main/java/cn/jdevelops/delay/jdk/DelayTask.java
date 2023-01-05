package cn.jdevelops.delay.jdk;


import cn.jdevelops.delay.util.Md5Util;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列 task
 *  重写 Delayed
 *  优点： 效率高，低延迟
 *  缺点： 宕机丢失（可以存数据库），不利于集群扩展
 * @author tnnn
 * @version V1.0
 * @date 2023-01-03 15:24
 */

public class DelayTask implements Delayed {

    /**
     * 消息唯一标识 （body md5）
     */
    private String id;


    /**
     * 消息体 (格式为json),具体执行用的数据
     */
    private String body;

    /**
     * 消息类型： 自定义， e.g. pay,notification,changeStatus
     */
    private String channel;


    /**
     * 延时时间（毫秒） eg: 2023-01-04 15:32:31 -> 1672817551000
     */
    private Long delayTime;


    /**
     * 延时时间 人看的
     */
    private String delayTimeStr;

    /**
     * 备注
     */
    private String desc;

    public DelayTask() {
    }

    /**
     *  添加延迟队列数据
     * @param body  消息体 (格式为json),具体执行用的数据
     * @param channel 消息类型： 自定义， e.g. pay,notification,changeStatus
     * @param delayTime 延时时间（毫秒） eg: 2023-01-04 15:32:31 -> 1672817551000
     * @param delayTimeStr 延时时间 人看的 = 延时时间的字符串时间格式
     * @param desc 备注可为空
     */
    public DelayTask(String body, String channel, Long delayTime, String delayTimeStr, String desc) {
        this.id =  Md5Util.getSha256StrJava(body);
        this.body = body;
        this.channel = channel;
        this.delayTime = delayTime;
        this.delayTimeStr = delayTimeStr;
        this.desc = desc;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // < 0 便执行
        return delayTime-System.currentTimeMillis();
    }

    @Override
    public int compareTo( Delayed o) {
        // 实现 比对方法
        DelayTask delayTask = (DelayTask)o;
        return delayTime - delayTask.getDelayTime() <= 0 ?-1:1;
    }


    @Override
    public String toString() {
        return "DelayTask{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", channel='" + channel + '\'' +
                ", delayTime=" + delayTime +
                ", delayTimeStr='" + delayTimeStr + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public String getDelayTimeStr() {
        return delayTimeStr;
    }

    public void setDelayTimeStr(String delayTimeStr) {
        this.delayTimeStr = delayTimeStr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
