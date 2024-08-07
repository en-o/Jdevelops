package cn.tannn.jdevelops.quartz.entity;


import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *  存储CalendarIntervalTrigger和DailyTimeIntervalTrigger
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_simprop_triggers")
@AutoConfigurationPackage
@Comment("存储CalendarIntervalTrigger和DailyTimeIntervalTrigger")
public class QrtzSimpropTriggersEntity  implements Serializable,Cloneable {


    /** 调度器名 */
    @Id
    @Comment("调度器名")
    private  String  schedName ;


    /** 触发器名 */
    @Id
    @Comment("触发器名")
    private  String  triggerName ;


    /** 触发器分组 */
    @Id
    @Comment("触发器分组")
    private  String  triggerGroup ;


    /** String类型的trigger的第一个参数 */
    @Comment("String类型的trigger的第一个参数")
    private  String  strProp1 ;


    /** String类型的trigger的第二个参数 */
    @Comment("String类型的trigger的第二个参数")
    private  String  strProp2 ;


    /** String类型的trigger的第三个参数 */
    @Comment("String类型的trigger的第三个参数")
    private  String  strProp3 ;


    /** int类型的trigger的第一个参数 */
    @Comment("int类型的trigger的第一个参数")
    private  Integer  intProp1 ;


    /** int类型的trigger的第二个参数 */
    @Comment("int类型的trigger的第二个参数")
    private  Integer  intProp2 ;


    /** long类型的trigger的第一个参数 */
    @Comment("long类型的trigger的第一个参数")
    private  Long  longProp1 ;


    /** long类型的trigger的第二个参数 */
    @Comment("long类型的trigger的第二个参数")
    private  Long  longProp2 ;


    /** decimal类型的trigger的第一个参数 */
    @Comment("decimal类型的trigger的第一个参数")
    private  Double  decProp1 ;


    /** decimal类型的trigger的第二个参数 */
    @Comment("decimal类型的trigger的第二个参数")
    private  Double  decProp2 ;


    /** Boolean类型的trigger的第一个参数 */
    @Comment("Boolean类型的trigger的第一个参数")
    private  String  boolProp1 ;


    /** Boolean类型的trigger的第二个参数 */
    @Comment("Boolean类型的trigger的第二个参数")
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


    @Override
    public String toString() {
        return "QrtzSimpropTriggersEntity{" +
                "schedName='" + schedName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", strProp1='" + strProp1 + '\'' +
                ", strProp2='" + strProp2 + '\'' +
                ", strProp3='" + strProp3 + '\'' +
                ", intProp1=" + intProp1 +
                ", intProp2=" + intProp2 +
                ", longProp1=" + longProp1 +
                ", longProp2=" + longProp2 +
                ", decProp1=" + decProp1 +
                ", decProp2=" + decProp2 +
                ", boolProp1='" + boolProp1 + '\'' +
                ", boolProp2='" + boolProp2 + '\'' +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getStrProp1() {
        return strProp1;
    }

    public void setStrProp1(String strProp1) {
        this.strProp1 = strProp1;
    }

    public String getStrProp2() {
        return strProp2;
    }

    public void setStrProp2(String strProp2) {
        this.strProp2 = strProp2;
    }

    public String getStrProp3() {
        return strProp3;
    }

    public void setStrProp3(String strProp3) {
        this.strProp3 = strProp3;
    }

    public Integer getIntProp1() {
        return intProp1;
    }

    public void setIntProp1(Integer intProp1) {
        this.intProp1 = intProp1;
    }

    public Integer getIntProp2() {
        return intProp2;
    }

    public void setIntProp2(Integer intProp2) {
        this.intProp2 = intProp2;
    }

    public Long getLongProp1() {
        return longProp1;
    }

    public void setLongProp1(Long longProp1) {
        this.longProp1 = longProp1;
    }

    public Long getLongProp2() {
        return longProp2;
    }

    public void setLongProp2(Long longProp2) {
        this.longProp2 = longProp2;
    }

    public Double getDecProp1() {
        return decProp1;
    }

    public void setDecProp1(Double decProp1) {
        this.decProp1 = decProp1;
    }

    public Double getDecProp2() {
        return decProp2;
    }

    public void setDecProp2(Double decProp2) {
        this.decProp2 = decProp2;
    }

    public String getBoolProp1() {
        return boolProp1;
    }

    public void setBoolProp1(String boolProp1) {
        this.boolProp1 = boolProp1;
    }

    public String getBoolProp2() {
        return boolProp2;
    }

    public void setBoolProp2(String boolProp2) {
        this.boolProp2 = boolProp2;
    }
}
