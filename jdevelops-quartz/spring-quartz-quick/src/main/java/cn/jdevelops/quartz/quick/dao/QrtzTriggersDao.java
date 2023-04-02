package cn.jdevelops.quartz.quick.dao;

import cn.jdevelops.quartz.quick.entity.QrtzTriggersEntity;
import cn.jdevelops.quartz.quick.entity.key.QrtzCronTriggersUPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 触发器
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:31
 */
public interface QrtzTriggersDao extends JpaRepository<QrtzTriggersEntity, QrtzCronTriggersUPK> {

}
