package cn.tannn.spring.quart.entity;

import cn.tannn.spring.quart.entity.key.QrtzCronTriggersUPK;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 触发器
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_triggers")
@Getter
@Setter
@ToString
public class QrtzTriggersEntity   implements Serializable,Cloneable {


    @EmbeddedId
    private QrtzCronTriggersUPK cronTriggersUPK;


    /** 任务名 */
    private  String  jobName ;


    /** 任务分组 */
    private  String  jobGroup ;


    /** 描述 */
    private  String  description ;


    /** 下一次执行时间 */
    private  Long  nextFireTime ;


    /**上一次执行时间*/
    private  Long  prevFireTime ;


    /** 优先级 */
    private  Integer  priority ;


    /** 触发器状态 */
    private  String  triggerState ;


    /** 触发器类型 */
    private  String  triggerType ;


    /** 开始时间 */
    private  Long  startTime ;


    /** 结束时间 */
    private  Long  endTime ;


    /** 日程名 */
    private  String  calendarName ;


    /** 执行失败标识符  */
    private  Integer  misfireInstr ;


    /** 任务数据 */
    private  byte[]  jobData ;

}
