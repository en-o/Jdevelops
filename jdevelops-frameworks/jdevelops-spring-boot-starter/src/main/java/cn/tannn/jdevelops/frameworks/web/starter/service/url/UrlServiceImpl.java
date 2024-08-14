package cn.tannn.jdevelops.frameworks.web.starter.service.url;


import cn.tannn.jdevelops.frameworks.web.starter.config.InterceptUrl;
import cn.tannn.jdevelops.frameworks.web.starter.entity.url.MethodUrl;
import cn.tannn.jdevelops.frameworks.web.starter.entity.url.Urls;
import cn.tannn.jdevelops.frameworks.web.starter.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

import static cn.tannn.jdevelops.frameworks.web.starter.util.UrlUtil.noRecordUrl;


/**
 * 接口服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 11:35
 */
@Service
public class UrlServiceImpl implements UrlService {

    private static final Logger LOG = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    WebApplicationContext applicationContext;

    @Resource
    private InterceptUrl interceptUrl;


    @Override
    public List<Urls> getLocalUrl() {
        return getUrls(new ArrayList<>());
    }


    @Override
    public List<Urls> getLocalUrl(List<MethodUrl> methodUrls) {
        return getUrls(methodUrls);
    }


    private List<Urls> getUrls(List<MethodUrl> methodUrls) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Urls> urls = new ArrayList<>();

        loop: for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethod : map.entrySet()) {
            Urls builder = new Urls();
            RequestMappingInfo info = handlerMethod.getKey();
            HandlerMethod method = handlerMethod.getValue();
            //获取当前方法所在类名
            Class<?> bean = method.getBeanType();
            //使用反射获取当前类注解内容 获取自定义注解
            Tag api = bean.getAnnotation(Tag.class);
            if (!Objects.isNull(api)) {
                builder.setGrouping(api.name());
            } else {
                builder.setGrouping("请使用@Api说明接口");
            }
            //获取方法上注解以及注解值
            Operation methodAnnotation = method.getMethodAnnotation(Operation.class);
            if (!Objects.isNull(methodAnnotation)) {
                builder.setDescription(methodAnnotation.description());
            } else {
                builder.setDescription("请使用@ApiOperation说明接口");
            }
            //获取url的Set集合，一个方法可能对应多个url
            try {
                Set<String> patterns;
                PatternsRequestCondition patternsCondition = info.getPatternsCondition();
                if(Objects.isNull(patternsCondition)){
                    continue ;
                }else {
                    patterns = patternsCondition.getPatterns();
                }
                // 这里可获取请求方式 Get,Post等等
                Set<RequestMethod> requestMethods = info.getMethodsCondition().getMethods();
                patterns.forEach(url -> {
                    if (noRecordUrl(url, interceptUrl.getExcludePathPatterns())) {
                        return;
                    }
                    builder.setUrl(url);
                });
                requestMethods.forEach(requestMethod -> builder.setRequestMethod(requestMethod.name()));
                if (StringUtil.isNotBlank(builder.getUrl())) {
                    if (!methodUrls.isEmpty()) {
                        for (MethodUrl methodUrl : methodUrls) {
                            if (builder.getUrl().equalsIgnoreCase(methodUrl.getUri())
                                    && builder.getRequestMethod().equalsIgnoreCase(methodUrl.getMethod())) {
                                LOG.warn("该接口已存在不用重复添加{},{}",builder.getDescription(),builder.getUrl());
                                continue loop;
                            }
                        }
                    }
                }else {
                    continue ;
                }

                urls.add(builder);
            } catch (Exception e) {
                LOG.error("getUrls失败", e);
            }
        }
        return urls;
    }


}
