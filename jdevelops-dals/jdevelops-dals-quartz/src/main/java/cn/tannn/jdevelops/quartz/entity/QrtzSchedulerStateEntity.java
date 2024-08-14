package cn.tannn.jdevelops.quartz.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import java.io.Serializable;
import java.util.Objects;

/**
 * 储少量的有关Scheduler的状态信息，和别的Scheduler实例
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_scheduler_state")
@AutoConfigurationPackage
@Comment("储少量的有关Scheduler的状态信息，和别的Scheduler实例")
public class QrtzSchedulerStateEntity implements Serializable, Cloneable {


    /**
     * 调度器名
     */
    @Id
    @Comment("调度器名")
    private String schedName;


    /**
     * 实例名
     */
    @Id
    @Comment("实例名")
    private String instanceName;


    /**
     * 最后检查时间
     */
    @Comment("最后检查时间")
    private Long lastCheckinTime;


    /**
     * 检查时间间隔
     */
    @Comment("检查时间间隔")
    private Long checkinInterval;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzSchedulerStateEntity that = (QrtzSchedulerStateEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(instanceName, that.instanceName) && Objects.equals(lastCheckinTime, that.lastCheckinTime) && Objects.equals(checkinInterval, that.checkinInterval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, instanceName, lastCheckinTime, checkinInterval);
    }

    @Override
    public String toString() {
        return "QrtzSchedulerStateEntity{" +
                "schedName='" + schedName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", lastCheckinTime=" + lastCheckinTime +
                ", checkinInterval=" + checkinInterval +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Long getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(Long lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public Long getCheckinInterval() {
        return checkinInterval;
    }

    public void setCheckinInterval(Long checkinInterval) {
        this.checkinInterval = checkinInterval;
    }
}

