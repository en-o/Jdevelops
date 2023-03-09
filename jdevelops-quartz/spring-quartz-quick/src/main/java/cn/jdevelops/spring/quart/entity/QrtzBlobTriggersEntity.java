package cn.jdevelops.spring.quart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
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
}
