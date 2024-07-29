package cn.tannn.jdevelops.quartz.dao.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 任务详情（job和trigger）
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-09 16:22
 */
public interface JobAndTriggerBO {

    /**
     * 任务名
     * @return String
     */
    String getJobName();

    /**
     * 任务分组
     * @return String
     */
    String getJobGroup();

    /**
     * 是否更新数据[0:否 1:是]
     * @return String
     */
    String getIsUpdateData();

    /**
     * 任务类名
     * @return String
     */
    String getJobClassName();

    /**
     * 触发器开始时间(毫秒)
     * @return Long
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getStartTime();

    /**
     * 触发器开始时间(毫秒)
     * @return Long
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getEndTime();

    /**
     * 下一次触发时间，默认为-1，意味不会自动触发
     * @return Long
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getNextFireTime();

    /**
     * 上一次触发时间（毫秒）
     * @return Long
     */
    @JsonSerialize(using = ToStringSerializer.class)
    Long getPrevFireTime();

    /**
     * 触发器状态 Trigger.TriggerState
     * @return String
     */
    String getTriggerState();

    /**
     * 触发器名称
     * @return String
     */
    String getTriggerName();

    /**
     * 触发器分组
     * @return String
     */
    String getTriggerGroup();

    /**
     * 触发器类型
     * @return String
     */
    String getTriggerType();

    /**
     * cron 表达式
     * @return String
     */
    String getCronExpression();

    /**
     * 时区
     * @return String
     */
    String getTimeZoneId();

}
