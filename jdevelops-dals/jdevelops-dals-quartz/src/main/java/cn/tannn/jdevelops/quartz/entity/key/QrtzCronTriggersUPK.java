package cn.tannn.jdevelops.quartz.entity.key;



import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * 存储CronTrigger的 联合主键
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:45
 */
@AutoConfigurationPackage
@Embeddable
public class QrtzCronTriggersUPK  implements Serializable,Cloneable {

    /** 调度器名 */
    @Comment("调度器名")
    private  String  schedName ;

    /** 触发器名  */
    @Comment("触发器名")
    private  String  triggerName ;

    /** 触发器分组 */
    @Comment("触发器分组")
    private  String  triggerGroup ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzCronTriggersUPK that = (QrtzCronTriggersUPK) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, triggerName, triggerGroup);
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

    @Override
    public String toString() {
        return "QrtzCronTriggersUPK{" +
                "schedName='" + schedName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                '}';
    }
}
