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
@Table(name = "qrtz_calendars")
@Getter
@Setter
@ToString
public class QrtzCalendarsEntity    implements Serializable,Cloneable {

    /** 调度器名  */
    @Id
    private  String  schedName ;


    /** 日程名  */
    @Id
    private  String  calendarName ;


    /** 日程数据 */
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
}
