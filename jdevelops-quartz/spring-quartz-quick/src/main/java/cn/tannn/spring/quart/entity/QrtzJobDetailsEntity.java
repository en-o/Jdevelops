package cn.tannn.spring.quart.entity;

/**
 * 任务详情
 * @author tnnn
 * @version V1.0
 * @date 2023-3-6
 */
//@Entity
//@Table(name = "qrtz_job_details")
//@org.hibernate.annotations.Table(appliesTo = "qrtz_job_details", comment = "")
public class QrtzJobDetailsEntity {


    /** 调度名字 */
    private  String  schedName ;


    /** 任务名字 */
    private  String  jobName ;


    /** 任务分组  */
    private  String  jobGroup ;


    /** 描述 */
    private  String  description ;


    /** 任务类名 */
    private  String  jobClassName ;


    /** 是否持久 */
    private  String  isDurable ;


    /** 是否单体  */
    private  String  isNonconcurrent ;


    /** 更新时间 */
    private  String  isUpdateData ;


    /** 请求恢复 */
    private  String  requestsRecovery ;


    /** 数据详情  */
    private  byte[]  jobData ;

}
