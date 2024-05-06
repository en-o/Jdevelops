package cn.jdevelops.config.standalone.service;

import cn.jdevelops.config.standalone.controller.dto.ConfigsPage;
import cn.jdevelops.config.standalone.dao.ConfigsDao;
import cn.jdevelops.config.standalone.model.Configs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 客户端注册上来的配置信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/28 13:01
 */
@Slf4j
@Service
public class ConfigsServiceImpl implements ConfigsService {

    private final ConfigsDao configsDao;

    public ConfigsServiceImpl(ConfigsDao configsDao) {
        this.configsDao = configsDao;
    }

    @Override
    public List<Configs> findAll() {
        return configsDao.findAll();
    }

    @Override
    public List<Configs> list(String app, String env, String ns) {
        return configsDao.findByAppAndEnvAndNs(app, env, ns);
    }

    @Override
    public Optional<Configs> select(String app, String env, String ns, String pkey) {
        return configsDao.findByAppAndEnvAndNsAndPkey(app, env, ns, pkey);
    }

    @Override
    public int update(Configs configs) {
        return configsDao.updateNoId(configs);
    }

    @Override
    public Configs insert(Configs configs) {
        return configsDao.save(configs);
    }

    @Override
    public Page<Configs> page(ConfigsPage page) {
        Sort orders = Sort.by(Sort.Direction.DESC, "app");
        return configsDao.findAll(page.select(), page.getPage().getPageable(orders));
    }
}
