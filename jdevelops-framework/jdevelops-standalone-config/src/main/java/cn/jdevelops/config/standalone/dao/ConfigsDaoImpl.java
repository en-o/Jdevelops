package cn.jdevelops.config.standalone.dao;

import cn.jdevelops.config.standalone.model.Configs;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/6 12:01
 */
public class ConfigsDaoImpl extends SimpleJpaRepository<Configs, Long> implements ConfigsDao{
    private final EntityManager entityManager;
    public ConfigsDaoImpl(EntityManager entityManager) {
        super(Configs.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<Configs> findByAppAndEnvAndNs(String app, String env, String ns) {
        return entityManager
                .createQuery("select c from Configs c where c.app =:app and c.env = :env and c.ns = :ns ",Configs.class)
                .setParameter("app",app)
                .setParameter("env",env)
                .setParameter("ns",ns)
                .getResultList();
    }

    @Override
    public Optional<Configs> findByAppAndEnvAndNsAndPkey(String app, String env, String ns, String pkey) {
        List<Configs> configs = entityManager
                .createQuery("select c from Configs c where c.app =:app " +
                                "and c.env = :env " +
                                "and c.ns = :ns " +
                                "and c.pkey = :pkey "
                        , Configs.class)
                .setParameter("app", app)
                .setParameter("env", env)
                .setParameter("ns", ns)
                .setParameter("pkey", pkey)
                .getResultList();
        if(configs.isEmpty()){
            return Optional.empty();
        }else {
            return Optional.ofNullable(configs.get(0));
        }
    }

    @Override
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    public int updateNoId(Configs configs) {
        return entityManager.createQuery("UPDATE Configs c SET c.pval = :pval WHERE c.app =:app " +
                        "and c.env = :env " +
                        "and c.ns = :ns " +
                        "and c.pkey = :pkey")
                .setParameter("app", configs.getApp())
                .setParameter("env", configs.getEnv())
                .setParameter("ns", configs.getNs())
                .setParameter("pkey", configs.getPkey())
                .setParameter("pval", configs.getPval())
                .executeUpdate();
    }
}
