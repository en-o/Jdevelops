package cn.jdevelops.spring.quart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzSimpropTriggersEntity that = (QrtzSimpropTriggersEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup) && Objects.equals(strProp1, that.strProp1) && Objects.equals(strProp2, that.strProp2) && Objects.equals(strProp3, that.strProp3) && Objects.equals(intProp1, that.intProp1) && Objects.equals(intProp2, that.intProp2) && Objects.equals(longProp1, that.longProp1) && Objects.equals(longProp2, that.longProp2) && Objects.equals(decProp1, that.decProp1) && Objects.equals(decProp2, that.decProp2) && Objects.equals(boolProp1, that.boolProp1) && Objects.equals(boolProp2, that.boolProp2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, triggerName, triggerGroup, strProp1, strProp2, strProp3, intProp1, intProp2, longProp1, longProp2, decProp1, decProp2, boolProp1, boolProp2);
    }
}
