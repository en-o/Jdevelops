package cn.tannn.jdevelops.quartz.service;

import cn.tannn.jdevelops.quartz.dao.QrtzJobDetailsDao;
import cn.tannn.jdevelops.quartz.dao.bo.JobAndTriggerBO;
import cn.tannn.jdevelops.quartz.exception.TaskException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
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
    public Page<JobAndTriggerBO> getJobAndTriggerDetails(Pageable pageable) {
        return jobDetailsDao.findJobAndTrigger(pageable);
    }

    @Override
    public void recurringJob(Class<? extends Job> jobBeanClass,
                             String jName,
                             String jGroup,
                             String tName,
                             String tGroup,
                             String cron,
                             boolean isStartNow) {
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
                    // 设置的开始时间数据
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron.trim())).build();
            // 启动调度器
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!isStartNow) {
                pauseJob(jName, jGroup);
            }
        } catch (Exception e) {
            LOG.error("任务创建失败{}", e.getMessage());
            if (e instanceof ObjectAlreadyExistsException) {
                throw new TaskException("请不要重复创建相同任务，任务名：" + jName);
            } else {
                throw new TaskException("任务创建失败", e);
            }
        }
    }


    @Override
    public void recurringJob(Class<? extends Job> jobBeanClass, String jName, String cron, boolean isStartNow) {
        recurringJob(jobBeanClass, jName, jName, jName, jName, cron, isStartNow);
    }


    @Override
    public void recurringJob(Class<? extends Job> jobBeanClass,
                             String jName,
                             String jGroup,
                             String tName,
                             String tGroup,
                             Date startTime,
                             boolean isStartNow) {
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
        recurringJob(jobBeanClass, jName, jGroup, tName, tGroup, startCron, isStartNow);
    }


    @Override
    public void delayJob(Class<? extends Job> jobBeanClass,
                         String jName,
                         String jGroup,
                         String tName,
                         String tGroup,
                         int delaySeconds) {
        try {
            // 构建JobDetail
            JobDetail jobDetail = JobBuilder.newJob(jobBeanClass)
                    // 指定任务组名和任务名
                    .withIdentity(jName, jGroup)
                    // 添加一些参数，执行的时候用
//                    .usingJobData("data", data)
                    .build();
            //创建触发器，指定任务执行时间
            Trigger trigger = TriggerBuilder.newTrigger()
                    // 指定触发器组名和触发器名
                    .withIdentity(tName, tGroup)
                    // 设置的开始时间数据
                    .startNow()
                    .startAt(DateBuilder.futureDate(delaySeconds, DateBuilder.IntervalUnit.SECOND)).build();
            // 启动调度器
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOG.error("任务创建失败{}", e.getMessage());
            if (e instanceof ObjectAlreadyExistsException) {
                throw new TaskException("请不要重复创建相同任务，任务名：" + jName);
            } else {
                throw new TaskException("任务创建失败", e);
            }
        }
    }

    @Override
    public void delayJob(Class<? extends Job> jobBeanClass, String jName, int delaySeconds) {
        delayJob(jobBeanClass, jName, jName, jName, jName, delaySeconds);
    }

    @Override
    public void delayJob(Class<? extends Job> jobBeanClass
            , String jName
            , String jGroup, String tName
            , String tGroup
            , LocalTime delaySeconds) {
        int delayDate = delaySeconds.toSecondOfDay();
        int nowDate = Calendar.getInstance().get(Calendar.SECOND);
        if (delayDate <= nowDate) {
            throw new TaskException("延时时间不允许小于等于当前");
        }
        delayJob(jobBeanClass, jName, jGroup, tName, tGroup, delayDate - nowDate);
    }

    @Override
    public void delayJob(Class<? extends Job> jobBeanClass, String jName, LocalTime delaySeconds) {
        delayJob(jobBeanClass, jName, jName, jName, jName, delaySeconds);
    }


    @Override
    public void pauseJob(String jName, String jGroup) throws TaskException {
        try {
            scheduler.pauseJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            LOG.error("任务暂停失败{}", e.getMessage());
            throw new TaskException("任务暂停失败", e);
        }
    }

    @Override
    public void resumeJob(String jName, String jGroup) throws TaskException {
        try {
            scheduler.resumeJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            LOG.error("任务恢复失败{}", e.getMessage());
            throw new TaskException("任务恢复失败", e);
        }
    }

    @Override
    public void resJob(String jName, String jGroup, String cron) throws TaskException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jName, jGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron.trim());
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
    public void deleteJob(String jName, String jGroup) throws TaskException {
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

    @Override
    public void runJob(String jName, String jGroup) throws TaskException {
        try {
            JobKey jobKey = JobKey.jobKey(jName, jGroup);
            this.scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException("run Fail", e);
        }
    }

}
