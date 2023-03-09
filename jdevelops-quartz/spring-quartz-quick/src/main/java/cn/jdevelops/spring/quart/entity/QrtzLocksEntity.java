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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzLocksEntity that = (QrtzLocksEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(lockName, that.lockName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, lockName);
    }
}
