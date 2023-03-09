package cn.jdevelops.spring.quart.entity.key;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * 存储CronTrigger的 联合主键
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:45
 */
@Embeddable
@Getter
@Setter
@ToString
public class QrtzCronTriggersUPK  implements Serializable,Cloneable {

    /** 调度器名 */
    private  String  schedName ;

    /** 触发器名  */
    private  String  triggerName ;

    /** 触发器分组 */
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
}
