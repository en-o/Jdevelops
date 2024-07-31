package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.entity.QrtzTriggersEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzCronTriggersUPK;
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
