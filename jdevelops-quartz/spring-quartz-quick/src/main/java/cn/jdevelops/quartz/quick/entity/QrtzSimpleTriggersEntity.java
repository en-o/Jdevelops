package cn.jdevelops.quartz.quick.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "qrtz_simple_triggers")
public class QrtzSimpleTriggersEntity implements Serializable,Cloneable{


    /** 调度器名 */
    @Id
    private  String  schedName ;


    /** 触发器名 */
    @Id
    private  String  triggerName ;


    /** 触发器分组 */
    @Id
    private  String  triggerGroup ;


    /** 重复技术 */
    private  Long  repeatCount ;


    /** 重复间隔  */
    private  Long  repeatInterval ;


    /** 时间触发 */
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
