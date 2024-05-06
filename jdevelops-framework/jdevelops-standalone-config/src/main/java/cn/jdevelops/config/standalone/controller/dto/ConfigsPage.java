package cn.jdevelops.config.standalone.controller.dto;

import cn.jdevelops.config.standalone.request.PageRequest;
import lombok.Data;

/**
 * 分页查询
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/5 下午3:24
 */
@Data
public class ConfigsPage {

    PageRequest page;

    public PageRequest getPage() {
        if(page == null) {
            return new PageRequest();
        }else {
            return page;
        }
    }
}
