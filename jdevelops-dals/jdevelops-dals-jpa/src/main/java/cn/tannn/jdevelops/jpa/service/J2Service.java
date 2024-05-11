package cn.tannn.jdevelops.jpa.service;


import cn.tannn.jdevelops.jpa.repository.JpaBasicsRepository;

/**
 * jpa公共service
 *
 * @param <B> 实体
 * @author tn
 * @date 2021-01-22 13:35
 */
public interface J2Service<B> {

    /**
     * 获取 dao
     *
     * @param <M> dao
     * @return dao
     */
    <ID, M extends JpaBasicsRepository<B, ID>> M getJpaBasicsDao();


}
