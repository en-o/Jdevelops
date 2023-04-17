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
@Table(name = "qrtz_paused_trigger_grps")
public class QrtzPausedTriggerGrpsEntity  implements Serializable,Cloneable {


    /** 调度器名  */
    @Id
    private  String  schedName ;


    /** 触发器分组 */
    @Id
    private  String  triggerGroup ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzPausedTriggerGrpsEntity that = (QrtzPausedTriggerGrpsEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(triggerGroup, that.triggerGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, triggerGroup);
    }

    @Override
    public String toString() {
        return "QrtzPausedTriggerGrpsEntity{" +
                "schedName='" + schedName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
