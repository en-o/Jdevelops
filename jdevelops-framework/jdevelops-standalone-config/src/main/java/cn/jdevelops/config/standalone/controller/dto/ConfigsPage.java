package cn.jdevelops.config.standalone.controller.dto;

import cn.jdevelops.config.standalone.model.Configs;
import cn.jdevelops.config.standalone.request.PageRequest;
import cn.jdevelops.config.standalone.select.ConfigsSelect;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;


/**
 * 分页查询
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/5 下午3:24
 */
@Data
public class ConfigsPage {

    /**
     * 应用
     */
    private String app;
    /**
     * 环境
     */
    private String env;
    /**
     * namespace
     */
    private String ns;
    /**
     * 参数key
     */
    private String pkey;
    /**
     * 参数value
     */
    private String pval;

    /**
     * 分页
     */
    PageRequest page;

    public PageRequest getPage() {
        if(page == null) {
            return new PageRequest();
        }else {
            return page;
        }
    }


    public Specification<Configs> select(){
        return ConfigsSelect.eqApp(app)
                .and(ConfigsSelect.eqEnv(env))
                .and(ConfigsSelect.eqNs(ns))
                .and(ConfigsSelect.eqPkey(pkey))
                .and(ConfigsSelect.eqPval(pval));
    }
}
