package cn.jdevelops.quartz.quick.dao.bo;

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
     * 任务类名
     * @return String
     */
    String getJobClassName();

    /**
     * 触发器开始时间
     * @return Long
     */
    Long getStartTime();

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
