package cn.jdevelops.quartz.quick.entity;

import cn.jdevelops.quartz.quick.entity.key.QrtzCronTriggersUPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 触发器
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_triggers")
public class QrtzTriggersEntity   implements Serializable,Cloneable {


    @EmbeddedId
    private QrtzCronTriggersUPK cronTriggersUPK;


    /** 任务名 */
    private  String  jobName ;


    /** 任务分组 */
    private  String  jobGroup ;


    /** 描述 */
    private  String  description ;


    /** 下一次执行时间 */
    private  Long  nextFireTime ;


    /**上一次执行时间*/
    private  Long  prevFireTime ;


    /** 优先级 */
    private  Integer  priority ;


    /** 触发器状态 */
    private  String  triggerState ;


    /** 触发器类型 */
    private  String  triggerType ;


    /** 开始时间 */
    private  Long  startTime ;


    /** 结束时间 */
    private  Long  endTime ;


    /** 日程名 */
    private  String  calendarName ;


    /** 执行失败标识符  */
    private  Integer  misfireInstr ;


    /** 任务数据 */
    @Column(columnDefinition="Blob")
    private  byte[]  jobData ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzTriggersEntity that = (QrtzTriggersEntity) o;
        return Objects.equals(cronTriggersUPK, that.cronTriggersUPK) && Objects.equals(jobName, that.jobName) && Objects.equals(jobGroup, that.jobGroup) && Objects.equals(description, that.description) && Objects.equals(nextFireTime, that.nextFireTime) && Objects.equals(prevFireTime, that.prevFireTime) && Objects.equals(priority, that.priority) && Objects.equals(triggerState, that.triggerState) && Objects.equals(triggerType, that.triggerType) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(calendarName, that.calendarName) && Objects.equals(misfireInstr, that.misfireInstr) && Arrays.equals(jobData, that.jobData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cronTriggersUPK, jobName, jobGroup, description, nextFireTime, prevFireTime, priority, triggerState, triggerType, startTime, endTime, calendarName, misfireInstr);
        result = 31 * result + Arrays.hashCode(jobData);
        return result;
    }

    @Override
    public String toString() {
        return "QrtzTriggersEntity{" +
                "cronTriggersUPK=" + cronTriggersUPK +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", description='" + description + '\'' +
                ", nextFireTime=" + nextFireTime +
                ", prevFireTime=" + prevFireTime +
                ", priority=" + priority +
                ", triggerState='" + triggerState + '\'' +
                ", triggerType='" + triggerType + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", calendarName='" + calendarName + '\'' +
                ", misfireInstr=" + misfireInstr +
                ", jobData=" + Arrays.toString(jobData) +
                '}';
    }

    public QrtzCronTriggersUPK getCronTriggersUPK() {
        return cronTriggersUPK;
    }

    public void setCronTriggersUPK(QrtzCronTriggersUPK cronTriggersUPK) {
        this.cronTriggersUPK = cronTriggersUPK;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(Long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Integer getMisfireInstr() {
        return misfireInstr;
    }

    public void setMisfireInstr(Integer misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    public byte[] getJobData() {
        return jobData;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }
}
