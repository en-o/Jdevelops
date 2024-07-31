package cn.tannn.jdevelops.quartz.dao;

import cn.tannn.jdevelops.quartz.dao.bo.JobAndTriggerBO;
import cn.tannn.jdevelops.quartz.entity.QrtzJobDetailsEntity;
import cn.tannn.jdevelops.quartz.entity.key.QrtzJobDetailsUPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Page<JobAndTriggerBO> findJobAndTrigger(Pageable pageable);
}
