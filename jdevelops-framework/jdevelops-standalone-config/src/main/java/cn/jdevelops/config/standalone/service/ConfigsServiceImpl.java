package cn.jdevelops.config.standalone.service;

import cn.jdevelops.config.standalone.controller.dto.ConfigsPage;
import cn.jdevelops.config.standalone.dao.ConfigsDao;
import cn.jdevelops.config.standalone.listener.ConfigChangeEvent;
import cn.jdevelops.config.standalone.listener.ConfigChangeListener;
import cn.jdevelops.config.standalone.model.Configs;
import cn.jdevelops.config.standalone.properties.ConfigMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * 属性变化监听器
     */
    List<ConfigChangeListener> listeners =  new ArrayList<>();

    private final ConfigsDao configsDao;

    private final ConfigMeta configMeta;

    public ConfigsServiceImpl(ConfigsDao configsDao, ConfigMeta configMeta) {
        this.configsDao = configsDao;
        this.configMeta = configMeta;
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
        int updated = configsDao.updateNoId(configs);
        if(updated>0){
            send();
        }
        return updated;
    }

    @Override
    public Configs insert(Configs configs) {
        Configs save = configsDao.save(configs);
        send();
        return save;
    }

    @Override
    public Page<Configs> page(ConfigsPage page) {
        Sort orders = Sort.by(Sort.Direction.DESC, "app");
        return configsDao.findAll(page.select(), page.getPage().getPageable(orders));
    }

    /**
     * 变化发送事件，  让spring env重刷和改变 ConfigServiceImpl.config 的值
     */
    public void send(){
        HashMap<String, String> newConfigs = getConfig();
        listeners.forEach(listener -> listener.onChange(new ConfigChangeEvent(configMeta, newConfigs)));
    }


    @Override
    public HashMap<String, String> getConfig() {
        List<Configs> configs = findAll();
        HashMap<String, String> newConfigs = new HashMap<>();
        configs.forEach(c -> newConfigs.put(c.getPkey(), c.getPval()));
        return newConfigs;
    }


    @Override
    public void addListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }
}
