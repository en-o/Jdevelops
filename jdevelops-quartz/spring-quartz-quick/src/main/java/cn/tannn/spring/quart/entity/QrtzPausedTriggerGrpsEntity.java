package cn.tannn.spring.quart.entity;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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

}
