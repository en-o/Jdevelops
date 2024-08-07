package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.dao.bo.JobAndTriggerBO;
import cn.tannn.jdevelops.quartz.entity.QrtzJobDetailsEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzJobDetailsUPK;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/26 下午2:29
 */
public class QrtzJobDetailsDaoImpl extends SimpleJpaRepository<QrtzJobDetailsEntity, QrtzJobDetailsUPK> implements QrtzJobDetailsDao{

    private final EntityManager entityManager;

    public QrtzJobDetailsDaoImpl(EntityManager entityManager) {
        super(QrtzJobDetailsEntity.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Page<JobAndTriggerBO> findJobAndTrigger(Pageable pageable) {
        String jpql = "SELECT DISTINCT " +
                " jd.jobDetailsUPK.jobName as jobName, " +
                " jd.jobDetailsUPK.jobGroup as jobGroup , " +
                " jd.jobClassName as jobClassName , " +
                " jd.isUpdateData as isUpdateData , " +
                " qt.startTime as startTime, " +
                " qt.endTime as endTime, " +
                " qt.nextFireTime as nextFireTime, " +
                " qt.prevFireTime as prevFireTime, " +
                " qt.triggerType as triggerType, " +
                " qt.triggerState as triggerState, " +
                " qt.cronTriggersUPK.triggerName as triggerName, " +
                " qt.cronTriggersUPK.triggerGroup as triggerGroup, " +
                " ct.cronExpression as cronExpression, " +
                " ct.timeZoneId  as timeZoneId " +
                "FROM " +
                " QrtzJobDetailsEntity jd " +
                "LEFT JOIN QrtzTriggersEntity qt ON qt.cronTriggersUPK.triggerGroup = jd.jobDetailsUPK.jobGroup " +
                "LEFT JOIN QrtzCronTriggersEntity ct ON jd.jobDetailsUPK.jobName = qt.cronTriggersUPK.triggerName " +
                "AND qt.cronTriggersUPK.triggerName = ct.cronTriggersUPK.triggerName " +
                "AND qt.cronTriggersUPK.triggerGroup = ct.cronTriggersUPK.triggerGroup ";


        // Create count query for total elements
        String countJpql = "SELECT COUNT(DISTINCT jd) " +
                "FROM QrtzJobDetailsEntity jd " +
                "LEFT JOIN QrtzTriggersEntity qt ON qt.cronTriggersUPK.triggerGroup = jd.jobDetailsUPK.jobGroup " +
                "LEFT JOIN QrtzCronTriggersEntity ct ON jd.jobDetailsUPK.jobName = qt.cronTriggersUPK.triggerName " +
                "AND qt.cronTriggersUPK.triggerName = ct.cronTriggersUPK.triggerName " +
                "AND qt.cronTriggersUPK.triggerGroup = ct.cronTriggersUPK.triggerGroup ";

        Query query = entityManager.createQuery(jpql, Tuple.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = query.getResultList();
        List<JobAndTriggerBO> results = tuples.stream()
                .map(JobAndTriggerBO::mapToJobAndTriggerBO)
                .collect(Collectors.toList());

        Query countQuery = entityManager.createQuery(countJpql);
        long total = (long) countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
}
