package cn.tannn.jdevelops.quartz.entity;


import org.hibernate.annotations.Comment;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@AutoConfigurationPackage
@Comment("存储程序的悲观锁的信息")
public class QrtzLocksEntity  implements Serializable,Cloneable{


    /** 调度器名  */
    @Id
    @Comment("调度器名")
    private  String  schedName ;


    /** 锁名 */
    @Id
    @Comment("锁名")
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

    @Override
    public String toString() {
        return "QrtzLocksEntity{" +
                "schedName='" + schedName + '\'' +
                ", lockName='" + lockName + '\'' +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
