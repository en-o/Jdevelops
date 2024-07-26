package cn.tannn.jdevelops.quartz.service;


import cn.tannn.jdevelops.quartz.dao.bo.JobAndTriggerBO;
import cn.tannn.jdevelops.quartz.exception.TaskException;
import org.quartz.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 定时器服务
 * @author tan
 */
public interface ScheduleService {

    /**
     * 获取定时任务详情
     * @param pageNum 页码 1开始
     * @param pageSize 每页数量
     * @return JobAndTriggerBO of Page
     */
    Page<JobAndTriggerBO> getJobAndTriggerDetails(Integer pageNum, Integer pageSize);


    /**
     * 获取定时任务详情
     * @param pageable Pageable
     * @return JobAndTriggerBO of Page
     */
    Page<JobAndTriggerBO> getJobAndTriggerDetails(Pageable pageable);


    /**
     * 添加任务 （新增的任务在时间到了就会立即开始）
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param jName 任务名
     * @param jGroup  任务分组
     * @param tName 触发器名
     * @param tGroup 触发器分组
     * @param cron 开始时间
     * @param isStartNow 是否立即执行[true立即执行]
     */
    void addScheduleJob(Class<? extends Job> jobBeanClass,
                        String jName,
                        String jGroup,
                        String tName,
                        String tGroup,
                        String cron,
                        boolean isStartNow);


    /**
     * 添加任务 （新增的任务在时间到了就会立即开始）
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param jName 任务名 [jName=jGroup=tName=tGroup]
     * @param cron 开始时间
     * @param isStartNow 是否立即执行[true立即执行]
     */
    void addScheduleJob(Class<? extends Job> jobBeanClass,
                        String jName,
                        String cron,
                        boolean isStartNow);

    /**
     * 指定时间来调度任务（新增的任务在时间到了就会立即开始）
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param jName 任务名
     * @param jGroup  任务分组
     * @param tName 触发器名
     * @param tGroup 触发器分组
     * @param startTime 开始时间
     * @param isStartNow 是否立即执行[true立即执行]
     */
    void addScheduleJob(Class<? extends Job> jobBeanClass,
                          String jName,
                          String jGroup,
                          String tName,
                          String tGroup,
                          Date startTime,
                        boolean isStartNow);

    /**
     * 暂停任务
     * @param jName 任务名
     * @param jGroup 任务分组
     * @throws TaskException task
     */
    void pauseScheduleJob(String jName, String jGroup) throws TaskException;

    /**
     * 恢复任务
     * @param jName 任务名
     * @param jGroup 任务分组
     * @throws TaskException task
     */
    void resumeScheduleJob(String jName, String jGroup) throws TaskException;

    /**
     * 重置任务调度时间
     * @param jName 任务名
     * @param jGroup 任务分组
     * @param cron 开始时间
     * @throws TaskException task
     */
    void resScheduleJob(String jName, String jGroup, String cron) throws TaskException;

    /**
     * 删除任务
     * @param jName 任务名
     * @param jGroup  任务分组
     * @throws TaskException task
     */
    void deleteScheduleJob(String jName, String jGroup) throws TaskException;

    /**
     * 执行一次
     * @param jName 任务名
     * @param jGroup  任务分组
     * @throws TaskException task
     */
    void runJob(String jName, String jGroup) throws TaskException;

}
