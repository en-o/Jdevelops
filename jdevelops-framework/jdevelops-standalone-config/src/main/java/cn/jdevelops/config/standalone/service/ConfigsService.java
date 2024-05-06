package cn.jdevelops.config.standalone.service;

import cn.jdevelops.config.standalone.controller.dto.ConfigsPage;
import cn.jdevelops.config.standalone.model.Configs;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 客户端注册上来的配置信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/28 13:00
 */
public interface ConfigsService {

    /**
     * 查询所有
     * @return Configs>
     */
    List<Configs> findAll();

    /**
     * 查询配置
     * @param app 应用
     * @param env 环境
     * @param ns namespace
     * @return Configs fo List
     */
    List<Configs> list(String app, String env, String ns);

    /**
     * 查询
     * @param app 应用
     * @param env 环境
     * @param ns namespace
     * @param pkey 参数key
     * @return Configs
     */
    Optional<Configs> select(String app, String env, String ns, String pkey);

    /**
     * 更新 pval
     * @param configs {@link Configs}
     * @return Configs
     */
    int update(Configs configs);

    /**
     * 保存配置
     * @param configs {@link Configs}
     * @return int
     */
    Configs insert(Configs configs);

    /**
     * 分页查询
     * @param page 分页
     * @return Configs
     */
    Page<Configs> page(ConfigsPage page);
}
