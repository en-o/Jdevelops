package cn.tannn.spring.quart.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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

}
