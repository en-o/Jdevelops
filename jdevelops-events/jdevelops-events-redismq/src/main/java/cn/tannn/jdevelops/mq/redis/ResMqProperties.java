package cn.tannn.jdevelops.mq.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis mq 基础配置
 *
 * @author tan
 */
@Component
@ConfigurationProperties(prefix = "jdevelops.redismq")
@ConditionalOnProperty(name = "jdevelops.redismq.enable", havingValue = "true", matchIfMissing = true)
public class ResMqProperties {

    /**
     * 是否启用，默认true
     */
    private boolean enable = true;

    /**
     * 最大队列数， xadd 队列只会存放这个数的数据量，多了就会自动裁剪
     */
    private int maxQueueSize = 3000;

    /**
     * 最大队列数的优化：true:允许稍微超出,false:精确
     */
    private Boolean approximateTrimming = true;


    /**
     * 队列 消费组 L组名, R消费名 （所有队列都用一样）
     * 格式: "groupName,consumerName"
     */
    private String streamKeyGroup;

    /**
     * true: 自动确认消息
     */
    private Boolean autoAck = false;

    /**
     * 超时检测/毫秒
     */
    private Integer pollTimeout = 1000;

    /**
     * 批次数量
     */
    private Integer batchSize = 5;

    /**
     * ACK后是否删除消息，默认false,队列会自己处理超长的，所以目前没使用
     */
    private boolean deleteAfterAck = false;

    /**
     * RedisTemplate 消息队列主题统一前缀
     * <p>注意必须最后要有 : 我不会主动处理</p>
     * <p>默认：jdevelops:cn:tan:resmq:</p>
     */
    private String prefix;

    /**
     * 获取解析后的Pair对象
     */
    public Pair<String, String> getStreamKeyGroupPair() {
        if (StringUtils.hasText(streamKeyGroup)) {
            String[] parts = streamKeyGroup.split(",");
            if (parts.length == 2) {
                return Pair.of(parts[0].trim(), parts[1].trim());
            }
        }
        return Pair.of("defaultGroup", "defaultConsumer");
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public Boolean getApproximateTrimming() {
        return approximateTrimming;
    }

    public void setApproximateTrimming(Boolean approximateTrimming) {
        this.approximateTrimming = approximateTrimming;
    }

    public String getStreamKeyGroup() {
        return streamKeyGroup;
    }

    public void setStreamKeyGroup(String streamKeyGroup) {
        this.streamKeyGroup = streamKeyGroup;
    }

    public Boolean getAutoAck() {
        return autoAck;
    }

    public void setAutoAck(Boolean autoAck) {
        this.autoAck = autoAck;
    }

    public Integer getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Integer pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isDeleteAfterAck() {
        return deleteAfterAck;
    }

    public void setDeleteAfterAck(boolean deleteAfterAck) {
        this.deleteAfterAck = deleteAfterAck;
    }

    public String getPrefix() {
        return prefix==null?"jdevelops:cn:tan:resmq:":prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "ResMqProperties{" +
                "enable=" + enable +
                ", maxQueueSize=" + maxQueueSize +
                ", approximateTrimming=" + approximateTrimming +
                ", streamKeyGroup='" + streamKeyGroup + '\'' +
                ", autoAck=" + autoAck +
                ", pollTimeout=" + pollTimeout +
                ", batchSize=" + batchSize +
                ", deleteAfterAck=" + deleteAfterAck +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
