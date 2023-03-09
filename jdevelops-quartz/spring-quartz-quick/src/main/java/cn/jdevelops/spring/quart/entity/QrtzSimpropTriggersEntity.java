package cn.jdevelops.spring.quart.entity;

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
@Table(name = "qrtz_simprop_triggers")
@Getter
@Setter
@ToString
public class QrtzSimpropTriggersEntity  implements Serializable,Cloneable {


    /** 调度器名 */
    @Id
    private  String  schedName ;


    /** 触发器名 */
    @Id
    private  String  triggerName ;


    /** 触发器分组 */
    @Id
    private  String  triggerGroup ;


    /**  */
    private  String  strProp1 ;


    /**  */
    private  String  strProp2 ;


    /**  */
    private  String  strProp3 ;


    /**  */
    private  Integer  intProp1 ;


    /**  */
    private  Integer  intProp2 ;


    /**  */
    private  Long  longProp1 ;


    /**  */
    private  Long  longProp2 ;


    /**  */
    private  Double  decProp1 ;


    /**  */
    private  Double  decProp2 ;


    /**  */
    private  String  boolProp1 ;


    /**  */
    private  String  boolProp2 ;

}
