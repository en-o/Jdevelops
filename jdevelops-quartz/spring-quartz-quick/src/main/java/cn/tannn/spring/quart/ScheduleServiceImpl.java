package cn.tannn.spring.quart;

import cn.tannn.spring.quart.exception.TaskException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private String defaultGroup = "default_group";

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final Scheduler scheduler;

    public ScheduleServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Override
    public String scheduleJob(Class<? extends Job> jobBeanClass, String cron, String data) {
        String jobName = UUID.randomUUID().toString();
        JobDetail jobDetail = JobBuilder.newJob(jobBeanClass)
                .withIdentity(jobName, defaultGroup)
                .usingJobData("data", data)
                .build();
        //创建触发器，指定任务执行时间
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, defaultGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            LOG.error("任务调度执行失败{}", e.getMessage());
            throw new TaskException("任务调度执行失败",e);
        }
        return jobName;
    }

    @Override
    public String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, String data) {

        //日期转CRON表达式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        String startCron = String.format("%d %d %d %d %d ? %d",
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));
        return scheduleJob(jobBeanClass, startCron, data);
    }

    @Override
    public Boolean cancelScheduleJob(String jobName) {
        try {
            // 暂停触发器
            scheduler.pauseTrigger(new TriggerKey(jobName, defaultGroup));
            // 移除触发器中的任务
            scheduler.unscheduleJob(new TriggerKey(jobName, defaultGroup));
            // 删除任务
            scheduler.deleteJob(new JobKey(jobName, defaultGroup));
        } catch (Exception e) {
            LOG.error("任务取消失败{}", e.getMessage());
            throw new TaskException("任务取消失败",e);
        }
        return true;
    }

    @Override
    public String findScheduleJob(String jobName) {
        return null;
    }
}
