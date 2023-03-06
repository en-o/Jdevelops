package cn.tannn.spring.quart.entity;


/**
 * 存储CronTrigger（生效的定时器），包括Cron表达式和时区信息
 * @author tnnn
 * @date 2023-3-6
 */
public class QrtzCronTriggers {
    /**
     * 调度器名称
     */
    private String schedName;
    /**
     * 定时器名
     */
    private String triggerName;
    /**
     * 定时器分组
     */
    private String triggerGroup;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 时区
     */
    private String timeZoneId;


}
