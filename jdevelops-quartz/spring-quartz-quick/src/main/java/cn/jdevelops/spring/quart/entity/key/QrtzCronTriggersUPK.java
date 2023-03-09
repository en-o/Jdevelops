package cn.jdevelops.spring.quart.entity.key;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

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

}
