package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.entity.QrtzTriggersEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzCronTriggersUPK;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * QrtzTriggersDao
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/26 下午2:26
 */
public class QrtzTriggersDaoImpl extends SimpleJpaRepository<QrtzTriggersEntity, QrtzCronTriggersUPK>
        implements QrtzTriggersDao {

    private final EntityManager entityManager;

    public QrtzTriggersDaoImpl(EntityManager entityManager) {
        super(QrtzTriggersEntity.class, entityManager);
        this.entityManager = entityManager;
    }

}
