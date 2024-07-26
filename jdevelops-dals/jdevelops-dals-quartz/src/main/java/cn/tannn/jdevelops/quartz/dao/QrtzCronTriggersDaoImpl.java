package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.entity.QrtzCronTriggersEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzCronTriggersUPK;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/26 下午2:31
 */
public class QrtzCronTriggersDaoImpl extends SimpleJpaRepository<QrtzCronTriggersEntity, QrtzCronTriggersUPK> implements QrtzCronTriggersDao {

    private final EntityManager entityManager;

    public QrtzCronTriggersDaoImpl(EntityManager entityManager) {
        super(QrtzCronTriggersEntity.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public int deleteByPrimaryKey(QrtzCronTriggersUPK key) {
        String sql = "delete from QrtzCronTriggersEntity  qt where qt.cronTriggersUPK.schedName = :#{#key.schedName} and " +
                " qt.cronTriggersUPK.triggerGroup = :#{#key.triggerGroup} " +
                "and qt.cronTriggersUPK.triggerName = :#{#key.triggerName}";
        return 0;
    }

    @Override
    public void updateByPrimaryKey(QrtzCronTriggersEntity cronTriggers) {
        String sql = "update QrtzCronTriggersEntity qt set qt.cronExpression = :#{#cronTriggers.cronExpression} , " +
                " qt.timeZoneId = :#{#cronTriggers.timeZoneId} " +
                " where qt.cronTriggersUPK.schedName = :#{#cronTriggers.cronTriggersUPK.schedName} and " +
                " qt.cronTriggersUPK.triggerGroup = :#{#cronTriggers.cronTriggersUPK.triggerGroup} " +
                "and qt.cronTriggersUPK.triggerName = :#{#cronTriggers.cronTriggersUPK.triggerName}";
    }
}
