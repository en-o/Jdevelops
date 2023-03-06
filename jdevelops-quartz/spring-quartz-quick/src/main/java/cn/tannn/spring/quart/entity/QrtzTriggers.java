package cn.tannn.spring.quart.entity;


/**
 * 所有定时器
 * @author tnnn
 * @version V1.0
 * @date 2023-3-6
 */
//@Entity
//@Table(name = "qrtz_triggers")
//@org.hibernate.annotations.Table(appliesTo = "qrtz_triggers", comment = "")
public class QrtzTriggers {


    /** 调度器名 */
    private  String  schedName ;


    /** 定时器名 */
    private  String  triggerName ;


    /** 定时器分组 */
    private  String  triggerGroup ;


    /** 任务名  */
    private  String  jobName ;


    /** 任务分组  */
    private  String  jobGroup ;


    /** 描述 */
    private  String  description ;


    /** BIGINT 下一次执行时间 */
    private  Long  nextFireTime ;


    /** BIGINT 上一次执行时间 */
    private  Long  prevFireTime ;


    /** 优先级 */
    private  Integer  priority ;


    /** 定时器状态 */
    private  String  triggerState ;


    /** 定时器类型  */
    private  String  triggerType ;


    /** BIGINT 开始时间 */
    private  Long  startTime ;


    /** BIGINT 结束时间 */
    private  Long  endTime ;


    /** calendarName  */
    private  String  calendarName ;


    /** SMALLINT 执行策略？ */
    private  Integer  misfireInstr ;


    /** 任务数据  */
    private  byte[]  jobData ;

}
