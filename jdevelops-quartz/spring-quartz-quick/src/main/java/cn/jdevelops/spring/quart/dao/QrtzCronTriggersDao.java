package cn.jdevelops.spring.quart.dao;

import cn.jdevelops.spring.quart.entity.QrtzCronTriggersEntity;
import cn.jdevelops.spring.quart.entity.key.QrtzCronTriggersUPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    @Query("delete from QrtzCronTriggersEntity  qt where qt.cronTriggersUPK.schedName = :#{#key.schedName} and " +
            " qt.cronTriggersUPK.triggerGroup = :#{#key.triggerGroup} " +
            "and qt.cronTriggersUPK.triggerName = :#{#key.triggerName}")
    int deleteByPrimaryKey(@Param("key") QrtzCronTriggersUPK key);


    /**
     * 根据 主键 跟新
     * @param cronTriggers QrtzCronTriggersEntity
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update QrtzCronTriggersEntity qt set qt.cronExpression = :#{#cronTriggers.cronExpression} , " +
            " qt.timeZoneId = :#{#cronTriggers.timeZoneId} " +
            " where qt.cronTriggersUPK.schedName = :#{#cronTriggers.cronTriggersUPK.schedName} and " +
            " qt.cronTriggersUPK.triggerGroup = :#{#cronTriggers.cronTriggersUPK.triggerGroup} " +
            "and qt.cronTriggersUPK.triggerName = :#{#cronTriggers.cronTriggersUPK.triggerName}")
    void updateByPrimaryKey(@Param("cronTriggers") QrtzCronTriggersEntity cronTriggers);
}
