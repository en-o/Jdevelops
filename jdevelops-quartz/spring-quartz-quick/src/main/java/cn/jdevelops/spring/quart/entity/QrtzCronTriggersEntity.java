package cn.jdevelops.spring.quart.entity;

import cn.jdevelops.spring.quart.entity.key.QrtzCronTriggersUPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * 存储 CronTrigger
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_cron_triggers")

public class QrtzCronTriggersEntity   implements Serializable,Cloneable {

    @EmbeddedId
    private QrtzCronTriggersUPK cronTriggersUPK;

    /** cron表达式 */
    private  String  cronExpression ;


    /** 时区  */
    private  String  timeZoneId ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzCronTriggersEntity that = (QrtzCronTriggersEntity) o;
        return Objects.equals(cronTriggersUPK, that.cronTriggersUPK) && Objects.equals(cronExpression, that.cronExpression) && Objects.equals(timeZoneId, that.timeZoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cronTriggersUPK, cronExpression, timeZoneId);
    }

    @Override
    public String toString() {
        return "QrtzCronTriggersEntity{" +
                "cronTriggersUPK=" + cronTriggersUPK +
                ", cronExpression='" + cronExpression + '\'' +
                ", timeZoneId='" + timeZoneId + '\'' +
                '}';
    }

    public QrtzCronTriggersUPK getCronTriggersUPK() {
        return cronTriggersUPK;
    }

    public void setCronTriggersUPK(QrtzCronTriggersUPK cronTriggersUPK) {
        this.cronTriggersUPK = cronTriggersUPK;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
}
