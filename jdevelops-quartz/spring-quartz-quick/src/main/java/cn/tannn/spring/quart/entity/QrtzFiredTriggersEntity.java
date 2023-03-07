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
@Table(name = "qrtz_fired_triggers")
@Getter
@Setter
@ToString
public class QrtzFiredTriggersEntity   implements Serializable,Cloneable {


    /** 调度器名 */
    @Id
    private  String  schedName ;


    /** 入口ID */
    @Id
    private  String  entryId ;


    /** 触发器名 */
    private  String  triggerName ;


    /** 触发器分组 */
    private  String  triggerGroup ;


    /** 实例名  */
    private  String  instanceName ;


    /** 执行时间 */
    private  Long  firedTime ;


    /** 调度时间 */
    private  Long  schedTime ;


    /** 优先级 */
    private  Integer  priority ;


    /** 状态 */
    private  String  state ;


    /** 任务名 */
    private  String  jobName ;


    /** 任务分组 */
    private  String  jobGroup ;


    /** 集群 */
    private  String  isNonconcurrent ;


    /** 需要恢复 */
    private  String  requestsRecovery ;

}
