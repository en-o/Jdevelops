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

}
