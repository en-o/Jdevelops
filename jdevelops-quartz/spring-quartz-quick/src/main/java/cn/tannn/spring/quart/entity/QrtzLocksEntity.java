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
@Table(name = "qrtz_locks")
@Getter
@Setter
@ToString
public class QrtzLocksEntity  implements Serializable,Cloneable{


    /** 调度器名  */
    @Id
    private  String  schedName ;


    /** 锁名 */
    @Id
    private  String  lockName ;

}
