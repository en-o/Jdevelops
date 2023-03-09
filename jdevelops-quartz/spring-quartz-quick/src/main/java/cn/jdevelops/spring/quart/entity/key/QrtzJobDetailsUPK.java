package cn.jdevelops.spring.quart.entity.key;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 任务详情联合主键
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 12:04
 */
@Embeddable
@Getter
@Setter
@ToString
public class QrtzJobDetailsUPK implements Serializable,Cloneable  {

    /** 调度器名 */
    private  String  schedName ;

    /** 任务名 */
    private  String  jobName ;

    /** 任务分组 */
    private  String  jobGroup ;
}
