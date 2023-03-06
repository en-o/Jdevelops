package cn.tannn.spring.quart;

import org.quartz.Job;

import java.util.Date;

/**
 * 定时器服务
 * @author tan
 */
public interface ScheduleService {

    /**
     * 通过 Cron 表达式来调度任务
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param cron 开始时间
     * @param data 数据
     * @return 任务名
     */
    String scheduleJob(Class<? extends Job> jobBeanClass, String cron, String data);

    /**
     * 指定时间来调度任务
     * @param jobBeanClass  定时任务的bean （ extends QuartzJobBean）
     * @param startTime 开始时间
     * @param data 数据
     * @return 任务名
     */
    String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, String data);

    /**
     * 取消定时任务
     * @param jobName 任务名
     * @return boolean
     */
    Boolean cancelScheduleJob(String jobName);

    /**
     * 查询定时任务详情
     * @param jobName 任务名
     * @return String
     */
    String findScheduleJob(String jobName);
}
