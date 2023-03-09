package cn.jdevelops.spring.quart;

import cn.jdevelops.spring.quart.entity.QrtzJobDetailsEntity;
import cn.jdevelops.spring.quart.exception.TaskException;
import org.quartz.Job;
import org.springframework.data.domain.Page;

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
     * @return QrtzJobDetailsEntity of Page
     */
    Page<QrtzJobDetailsEntity> getJobAndTriggerDetails(Integer pageNum, Integer pageSize);

    /**
     * 添加任务
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param jName 任务名
     * @param jGroup  任务分组
     * @param tName 触发器名
     * @param tGroup 触发器分组
     * @param startNow 是否立即开始
     * @param cron 开始时间
     */
    void addScheduleJob(Class<? extends Job> jobBeanClass,
                        String jName,
                        String jGroup,
                        String tName,
                        String tGroup,
                        boolean startNow,
                        String cron);

    /**
     * 指定时间来调度任务
     * @param jobBeanClass  定时任务的bean（ extends QuartzJobBean）
     * @param jName 任务名
     * @param jGroup  任务分组
     * @param tName 触发器名
     * @param tGroup 触发器分组
     * @param startNow 是否立即开始
     * @param startTime 开始时间
     */
    void addScheduleJob(Class<? extends Job> jobBeanClass,
                          String jName,
                          String jGroup,
                          String tName,
                          String tGroup,
                          boolean startNow,
                          Date startTime);

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

}
