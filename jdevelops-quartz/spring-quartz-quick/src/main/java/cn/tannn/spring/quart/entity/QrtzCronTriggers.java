package cn.tannn.spring.quart.entity;


/**
 * 生产中的定时器
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
