package cn.jdevelops.config.standalone.select;

import cn.jdevelops.config.standalone.model.Configs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * specification
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/6 13:48
 */
public class ConfigsSelect {

    /**
     * 条件  app   =
     */
    public static Specification<Configs> eqApp(String app) {
        if (ObjectUtils.isEmpty(app)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else{
            return (root, query, builder) -> builder.equal(root.get("app"), app);
        }
    }

    /**
     * 条件  env   =
     */
    public static Specification<Configs> eqEnv(String env) {
        if (ObjectUtils.isEmpty(env)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else{
            return (root, query, builder) -> builder.equal(root.get("env"), env);
        }
    }

    /**
     * 条件  ns   =
     */
    public static Specification<Configs> eqNs(String ns) {
        if (ObjectUtils.isEmpty(ns)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else{
            return (root, query, builder) -> builder.equal(root.get("ns"), ns);
        }
    }

    /**
     * 条件  pkey   =
     */
    public static Specification<Configs> eqPkey(String pkey) {
        if (ObjectUtils.isEmpty(pkey)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else{
            return (root, query, builder) -> builder.equal(root.get("pkey"), pkey);
        }
    }

    /**
     * 条件  pval   =
     */
    public static Specification<Configs> eqPval(String pval) {
        if (ObjectUtils.isEmpty(pval)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaQuery.getRestriction();
        }else{
            return (root, query, builder) -> builder.equal(root.get("pval"), pval);
        }
    }
}
