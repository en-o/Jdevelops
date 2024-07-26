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
@Table(name = "qrtz_calendars")
@AutoConfigurationPackage
public class QrtzCalendarsEntity    implements Serializable,Cloneable {

    /** 调度器名  */
    @Id
    private  String  schedName ;


    /** 日程名  */
    @Id
    private  String  calendarName ;


    /** 日程数据 */
    @Column(columnDefinition="Blob")
    private  byte[]  calendar ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzCalendarsEntity that = (QrtzCalendarsEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(calendarName, that.calendarName) && Arrays.equals(calendar, that.calendar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(schedName, calendarName);
        result = 31 * result + Arrays.hashCode(calendar);
        return result;
    }

    @Override
    public String toString() {
        return "QrtzCalendarsEntity{" +
                "schedName='" + schedName + '\'' +
                ", calendarName='" + calendarName + '\'' +
                ", calendar=" + Arrays.toString(calendar) +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public byte[] getCalendar() {
        return calendar;
    }

    public void setCalendar(byte[] calendar) {
        this.calendar = calendar;
    }
}
