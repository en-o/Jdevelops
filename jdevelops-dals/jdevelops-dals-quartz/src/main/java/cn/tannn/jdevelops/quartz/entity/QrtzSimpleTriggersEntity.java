package cn.tannn.jdevelops.quartz.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import java.io.Serializable;
import java.util.Objects;

/**
 * 存储简单的Trigger，包括重复次数、间隔、以及已触的次数
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_simple_triggers")
@AutoConfigurationPackage
@Comment("存储简单的Trigger，包括重复次数、间隔、以及已触的次数")
public class QrtzSimpleTriggersEntity implements Serializable,Cloneable{


    /** 调度器名 */
    @Id
    @Comment("调度器名")
    private  String  schedName ;


    /** 触发器名 */
    @Id
    @Comment("触发器名")
    private  String  triggerName ;


    /** 触发器分组 */
    @Id
    @Comment("触发器分组")
    private  String  triggerGroup ;


    /** 重复的次数统计 */
    @Comment("重复的次数统计")
    private  Long  repeatCount ;


    /** 重复的间隔时间  */
    @Comment("重复的间隔时间")
    private  Long  repeatInterval ;


    /** 已经触发的次数 */
    @Comment("已经触发的次数")
    private  Long  timesTriggered ;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzSimpleTriggersEntity that = (QrtzSimpleTriggersEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup) && Objects.equals(repeatCount, that.repeatCount) && Objects.equals(repeatInterval, that.repeatInterval) && Objects.equals(timesTriggered, that.timesTriggered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, triggerName, triggerGroup, repeatCount, repeatInterval, timesTriggered);
    }

    @Override
    public String toString() {
        return "QrtzSimpleTriggersEntity{" +
                "schedName='" + schedName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", repeatCount=" + repeatCount +
                ", repeatInterval=" + repeatInterval +
                ", timesTriggered=" + timesTriggered +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
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

    public Long getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Long repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Long getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(Long timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
