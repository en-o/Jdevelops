package cn.jdevelops.spring.quart.entity;

import cn.jdevelops.spring.quart.entity.key.QrtzCronTriggersUPK;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 存储 CronTrigger
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_cron_triggers")
@Getter
@Setter
@ToString
public class QrtzCronTriggersEntity   implements Serializable,Cloneable {

    @EmbeddedId
    private QrtzCronTriggersUPK cronTriggersUPK;

    /** cron表达式 */
    private  String  cronExpression ;


    /** 时区  */
    private  String  timeZoneId ;

}
