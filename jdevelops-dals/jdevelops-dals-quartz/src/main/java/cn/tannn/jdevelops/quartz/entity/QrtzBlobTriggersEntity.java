package cn.tannn.jdevelops.quartz.entity;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_blob_triggers")
@AutoConfigurationPackage
public class QrtzBlobTriggersEntity  implements Serializable,Cloneable {


    /** 调度器名 */
    @Id
    private  String  schedName ;

    /** 触发器名 */
    @Id
    private  String  triggerName ;

    /** 触发器分组 */
    @Id
    private  String  triggerGroup ;

    /** 数据 */
    @Column(columnDefinition="Blob")
    private  byte[]  blobData ;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzBlobTriggersEntity that = (QrtzBlobTriggersEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup) && Arrays.equals(blobData, that.blobData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(schedName, triggerName, triggerGroup);
        result = 31 * result + Arrays.hashCode(blobData);
        return result;
    }

    @Override
    public String toString() {
        return "QrtzBlobTriggersEntity{" +
                "schedName='" + schedName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", blobData=" + Arrays.toString(blobData) +
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

    public byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
}
