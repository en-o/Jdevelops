package cn.tannn.spring.quart.dao;

import cn.tannn.spring.quart.entity.QrtzJobDetailsEntity;
import cn.tannn.spring.quart.entity.key.QrtzJobDetailsUPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 任务详情
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-07 11:30
 */
public interface QrtzJobDetailsDao extends JpaRepository<QrtzJobDetailsEntity, QrtzJobDetailsUPK> {

}
