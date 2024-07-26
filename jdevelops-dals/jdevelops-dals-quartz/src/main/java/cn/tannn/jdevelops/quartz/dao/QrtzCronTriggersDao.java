package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.entity.QrtzCronTriggersEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzCronTriggersUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 存储 CronTrigger
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:23
 */
public interface QrtzCronTriggersDao  extends JpaRepository<QrtzCronTriggersEntity, QrtzCronTriggersUPK> {


    /**
     * 删除
     * @param key 联合主键
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    int deleteByPrimaryKey(@Param("key") QrtzCronTriggersUPK key);


    /**
     * 根据 主键 跟新
     * @param cronTriggers QrtzCronTriggersEntity
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void updateByPrimaryKey(@Param("cronTriggers") QrtzCronTriggersEntity cronTriggers);
}
