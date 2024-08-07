package cn.tannn.jdevelops.quartz.entity;


import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_fired_triggers")
@AutoConfigurationPackage
@Comment("存储与已触发的Trigger相关的状态信息，以及相联Job的执行信息")
public class QrtzFiredTriggersEntity   implements Serializable,Cloneable {


    /** 调度器名 */
    @Id
    @Comment("调度器名")
    private  String  schedName ;


    /** 入口ID */
    @Id
    @Comment("entryId")
    private  String  entryId ;


    /** 触发器名 */
    @Comment("触发器名")
    private  String  triggerName ;


    /** 触发器分组 */
    @Comment("触发器分组")
    private  String  triggerGroup ;


    /** 实例名  */
    @Comment("实例名")
    private  String  instanceName ;


    /** 执行时间 */
    @Comment("执行时间")
    private  Long  firedTime ;


    /** 调度时间 */
    @Comment("调度时间")
    private  Long  schedTime ;


    /** 优先级 */
    @Comment("优先级")
    private  Integer  priority ;


    /** 状态 */
    @Comment("状态")
    private  String  state ;


    /** 任务名 */
    @Comment("任务名")
    private  String  jobName ;


    /** 任务分组 */
    @Comment("任务分组")
    private  String  jobGroup ;


    /** 集群 */
    @Comment("集群")
    private  String  isNonconcurrent ;


    /** 是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则会被重新执行 */
    @Comment("是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则会被重新执行")
    private  String  requestsRecovery ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzFiredTriggersEntity that = (QrtzFiredTriggersEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(entryId, that.entryId) && Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup) && Objects.equals(instanceName, that.instanceName) && Objects.equals(firedTime, that.firedTime) && Objects.equals(schedTime, that.schedTime) && Objects.equals(priority, that.priority) && Objects.equals(state, that.state) && Objects.equals(jobName, that.jobName) && Objects.equals(jobGroup, that.jobGroup) && Objects.equals(isNonconcurrent, that.isNonconcurrent) && Objects.equals(requestsRecovery, that.requestsRecovery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, entryId, triggerName, triggerGroup, instanceName, firedTime, schedTime, priority, state, jobName, jobGroup, isNonconcurrent, requestsRecovery);
    }

    @Override
    public String toString() {
        return "QrtzFiredTriggersEntity{" +
                "schedName='" + schedName + '\'' +
                ", entryId='" + entryId + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", firedTime=" + firedTime +
                ", schedTime=" + schedTime +
                ", priority=" + priority +
                ", state='" + state + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", isNonconcurrent='" + isNonconcurrent + '\'' +
                ", requestsRecovery='" + requestsRecovery + '\'' +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Long getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(Long firedTime) {
        this.firedTime = firedTime;
    }

    public Long getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(Long schedTime) {
        this.schedTime = schedTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getIsNonconcurrent() {
        return isNonconcurrent;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public String getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }
}
