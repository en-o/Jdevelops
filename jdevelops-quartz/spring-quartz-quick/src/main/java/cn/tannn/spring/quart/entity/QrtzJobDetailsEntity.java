package cn.tannn.spring.quart.entity;

import cn.tannn.spring.quart.entity.key.QrtzJobDetailsUPK;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 任务详情
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_job_details")
@Getter
@Setter
@ToString
public class QrtzJobDetailsEntity   implements Serializable,Cloneable{


    @EmbeddedId
    private  QrtzJobDetailsUPK jobDetailsUPK;

    /** 描述  */
    private  String  description ;


    /** 任务类名 */
    private  String  jobClassName ;


    /** 是否持久 */
    private  String  isDurable ;


    /** 是否集群 */
    private  String  isNonconcurrent ;


    /** 跟新数据 */
    private  String  isUpdateData ;


    /** 需要恢复 */
    private  String  requestsRecovery ;


    /** 任务数据 */
    private  byte[]  jobData ;

}
