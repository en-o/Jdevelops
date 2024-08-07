package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.entity.QrtzCronTriggersEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzCronTriggersUPK;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

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
        String jpql = "delete from QrtzCronTriggersEntity  qt where qt.cronTriggersUPK.schedName =  :schedName and " +
                " qt.cronTriggersUPK.triggerGroup = :triggerGroup " +
                "and qt.cronTriggersUPK.triggerName = :triggerName";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("schedName", key.getSchedName());
        query.setParameter("triggerGroup", key.getTriggerGroup());
        query.setParameter("triggerName", key.getTriggerName());

        return query.executeUpdate();
    }

    @Override
    public void updateByPrimaryKey(QrtzCronTriggersEntity cronTriggers) {
        String jpql = "update QrtzCronTriggersEntity qt set qt.cronExpression = :cronExpression , " +
                " qt.timeZoneId = :timeZoneId  " +
                " where qt.cronTriggersUPK.schedName = :schedName  and " +
                " qt.cronTriggersUPK.triggerGroup = :triggerGroup " +
                "and qt.cronTriggersUPK.triggerName = :triggerName ";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("cronExpression", cronTriggers.getCronExpression());
        query.setParameter("timeZoneId", cronTriggers.getTimeZoneId());
        query.setParameter("schedName", cronTriggers.getCronTriggersUPK().getSchedName());
        query.setParameter("triggerGroup", cronTriggers.getCronTriggersUPK().getTriggerGroup());
        query.setParameter("triggerName", cronTriggers.getCronTriggersUPK().getTriggerName());

        query.executeUpdate();
    }
}
