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
@Table(name = "qrtz_simple_triggers")
@Getter
@Setter
@ToString
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
}
