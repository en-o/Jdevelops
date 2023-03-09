package cn.jdevelops.spring.quart;

import cn.jdevelops.spring.quart.dao.bo.JobAndTriggerBO;
import cn.jdevelops.spring.quart.entity.QrtzJobDetailsEntity;
import cn.jdevelops.spring.quart.dao.QrtzJobDetailsDao;
import cn.jdevelops.spring.quart.exception.TaskException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    /**
     * 调度器，Quartz通过调度器来注册、暂停、删除Trigger和JobDetail。
     * Scheduler还拥有一个SchedulerContext，顾名思义就是上下文，通过SchedulerContext我们可以获取到触发器和任务的一些信息。
     */
    private final Scheduler scheduler;
    private final QrtzJobDetailsDao jobDetailsDao;

    public ScheduleServiceImpl(Scheduler scheduler, QrtzJobDetailsDao jobDetailsDao) {
        this.scheduler = scheduler;
        this.jobDetailsDao = jobDetailsDao;
    }


    @Override
    public Page<JobAndTriggerBO> getJobAndTriggerDetails(Integer pageNum, Integer pageSize) {
        pageNum = Optional.ofNullable(pageNum).orElse(1);
        pageSize = Optional.ofNullable(pageSize).orElse(10);
        return jobDetailsDao.findJobAndTrigger(PageRequest.of(pageNum - 1, pageSize));
    }

    @Override
    public void addScheduleJob(Class<? extends Job> jobBeanClass,
                               String jName,
                               String jGroup,
                               String tName,
                               String tGroup,
                               boolean startNow,
                               String cron) {
        try {
            // 构建JobDetail
            JobDetail jobDetail = JobBuilder.newJob(jobBeanClass)
                    // 指定任务组名和任务名
                    .withIdentity(jName, jGroup)
                    // 添加一些参数，执行的时候用
//                    .usingJobData("data", data)
                    .build();
            //创建触发器，指定任务执行时间
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    // 指定触发器组名和触发器名
                    .withIdentity(tName, tGroup)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            if(startNow){
                // 启动调度器
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOG.error("任务创建失败{}", e.getMessage());
            throw new TaskException("任务创建失败", e);
        }
    }

    @Override
    public void addScheduleJob(Class<? extends Job> jobBeanClass,
                               String jName,
                               String jGroup,
                               String tName,
                               String tGroup,
                               boolean startNow,
                               Date startTime) {
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
        addScheduleJob(jobBeanClass, jName, jGroup, tName, tGroup, startNow, startCron);
    }

    @Override
    public void pauseScheduleJob(String jName, String jGroup) throws TaskException {
        try {
            scheduler.pauseJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            LOG.error("任务暂停失败{}", e.getMessage());
            throw new TaskException("任务暂停失败", e);
        }
    }

    @Override
    public void resumeScheduleJob(String jName, String jGroup) throws TaskException {
        try {
            scheduler.resumeJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            LOG.error("任务恢复失败{}", e.getMessage());
            throw new TaskException("任务恢复失败", e);
        }
    }

    @Override
    public void resScheduleJob(String jName, String jGroup, String cron) throws TaskException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jName, jGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行，重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            LOG.error("任务重置调用时间失败{}", e.getMessage());
            throw new TaskException("任务重置调用时间失败", e);
        }

    }

    @Override
    public void deleteScheduleJob(String jName, String jGroup) throws TaskException {
        try {
            // 暂停触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(jName, jGroup));
            // 移除触发器中的任务
            scheduler.unscheduleJob(TriggerKey.triggerKey(jName, jGroup));
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            LOG.error("任务取消失败{}", e.getMessage());
            throw new TaskException("任务取消失败", e);
        }
    }

}
