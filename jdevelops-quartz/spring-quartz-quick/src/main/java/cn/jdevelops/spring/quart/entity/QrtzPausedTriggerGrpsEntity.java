package cn.jdevelops.spring.quart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
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
}
