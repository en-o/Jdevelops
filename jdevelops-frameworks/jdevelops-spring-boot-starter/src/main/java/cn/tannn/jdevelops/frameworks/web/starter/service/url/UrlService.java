package cn.tannn.jdevelops.frameworks.web.starter.service.url;



import cn.tannn.jdevelops.frameworks.web.starter.entity.url.MethodUrl;
import cn.tannn.jdevelops.frameworks.web.starter.entity.url.Urls;

import java.util.List;

/**
 * 接口服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 11:35
 */
public interface UrlService {

    /**
     * 获取当前项目所有的接口
     * @return Urls
     */
    List<Urls> getLocalUrl();


    /**
     * 获取当前项目所有的接口
     * @param methodUrls 过滤这个list中的数据(数据库已存在的资源
     * @return Urls
     */
    List<Urls>  getLocalUrl(List<MethodUrl> methodUrls);
}
