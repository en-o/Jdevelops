package cn.jdevelops.config.standalone.dao;

import cn.jdevelops.config.standalone.model.Configs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 客户端注册上来的配置信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/28 13:00
 */
public interface ConfigsDao extends JpaRepository<Configs, Long>, JpaSpecificationExecutor<Configs> {
    /**
     * 查询配置
     *
     * @param app 应用
     * @param env 环境
     * @param ns  namespace
     * @return Configs fo List
     */
    List<Configs> findByAppAndEnvAndNs(String app, String env, String ns);

    /**
     * 查询
     *
     * @param app  应用
     * @param env  环境
     * @param ns   namespace
     * @param pkey 参数key
     * @return Configs
     */
    Optional<Configs> findByAppAndEnvAndNsAndPkey(String app, String env, String ns, String pkey);

    /**
     * 更新 pval （无ID更新
     *
     * @param configs （app and env and ns and pkey） update  pval
     * @return int
     */
    @Modifying
    @Query("update Configs u set u.pval = :#{#configs.pval} " +
            "where u.app = :#{#configs.app} " +
            "and u.env = :#{#configs.env} " +
            "and u.ns = :#{#configs.ns} " +
            "and u.pkey = :#{#configs.pkey} ")
    @Transactional(rollbackFor = Exception.class)
    int updateNoId(@Param("configs") Configs configs);
}
