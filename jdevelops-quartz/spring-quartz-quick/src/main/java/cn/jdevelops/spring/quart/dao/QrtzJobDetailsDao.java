package cn.jdevelops.spring.quart.dao;

import cn.jdevelops.spring.quart.dao.bo.JobAndTriggerBO;
import cn.jdevelops.spring.quart.entity.QrtzJobDetailsEntity;
import cn.jdevelops.spring.quart.entity.key.QrtzJobDetailsUPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 任务详情
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:30
 */
public interface QrtzJobDetailsDao extends JpaRepository<QrtzJobDetailsEntity, QrtzJobDetailsUPK> {


    /**
     * 查询任务详情
     * @param pageable 分页
     * @return JobAndTriggerBO of Page
     */
    @Query("SELECT DISTINCT " +
            " jd.jobDetailsUPK.jobName as jobName, " +
            " jd.jobDetailsUPK.jobGroup as jobGroup , " +
            " jd.jobClassName as jobClassName , " +
            " qt.startTime as startTime, " +
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
            "AND qt.cronTriggersUPK.triggerGroup = ct.cronTriggersUPK.triggerGroup ")
    Page<JobAndTriggerBO> findJobAndTrigger(Pageable pageable);
}
