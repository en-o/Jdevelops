package cn.tannn.jdevelops.delays.core.entity;


import cn.tannn.jdevelops.delays.core.util.Md5Util;

/**
 * 延时消息
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-07 22:53
 */
public class DelayQueueMessage {

    /**
     * 消息唯一标识 （body md5（基本不用自己填写）
     */
    private String id;


    /**
     * 消息体 (格式为json),具体执行用的数据
     */
    private String body;

    /**
     * 消息类型： 自定义， e.g. pay,notification,changeStatus
     * 必填
     */
    private String channel;


    /**
     * 延时时间（毫秒） eg: 2023-01-04 15:32:31 -> 1672817551000
     */
    private Long delayTime;


    /**
     * 延时时间 人看的 爱填不填
     */
    private String delayTimeStr;

    /**
     * 备注  爱填不填
     */
    private String desc;


    public DelayQueueMessage() {
    }

    /**
     *  添加延迟队列数据
     * @param body  消息体 (格式为json),具体执行用的数据
     * @param channel 消息类型： 自定义， e.g. pay,notification,changeStatus
     * @param delayTime 延时时间（毫秒） eg: 2023-01-04 15:32:31 -> 1672817551000
     * @param delayTimeStr 延时时间 人看的 = 延时时间的字符串时间格式
     * @param desc 备注可为空
     */
    public DelayQueueMessage(String body, String channel, Long delayTime, String delayTimeStr, String desc) {
        this.id =  Md5Util.getSha256StrJava(body);
        this.body = body;
        this.channel = channel;
        this.delayTime = delayTime;
        this.delayTimeStr = delayTimeStr;
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "DelayQueueMessage{" +
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
