package cn.jdevelops.springs.service.url.impl;


import cn.jdevelops.springs.service.url.UrlService;
import cn.jdevelops.springs.service.config.InterceptUrl;
import cn.jdevelops.springs.service.url.entity.MethodUrl;
import cn.jdevelops.springs.service.url.entity.Urls;
import cn.jdevelops.springs.service.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;


import java.util.*;

import static cn.jdevelops.springs.service.util.UrlUtil.noRecordUrl;


/**
 * 接口服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 11:35
 */
@Service
@Slf4j
public class UrlServiceImpl implements UrlService {

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
            Urls.UrlsBuilder builder = Urls.builder();
            RequestMappingInfo info = handlerMethod.getKey();
            HandlerMethod method = handlerMethod.getValue();
            //获取当前方法所在类名
            Class<?> bean = method.getBeanType();
            //使用反射获取当前类注解内容 获取自定义注解
            Api api = bean.getAnnotation(Api.class);
            if (!Objects.isNull(api)) {
                builder.grouping(api.tags()[0]);
            } else {
                builder.grouping("请使用@Api说明接口");
            }
            //获取方法上注解以及注解值
            ApiOperation methodAnnotation = method.getMethodAnnotation(ApiOperation.class);
            if (!Objects.isNull(methodAnnotation)) {
                builder.description(methodAnnotation.value());
            } else {
                builder.description("请使用@ApiOperation说明接口");
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
                    builder.url(url);
                });
                requestMethods.forEach(requestMethod -> builder.requestMethod(requestMethod.name()));
                Urls build = builder.build();
                if (StringUtil.isNotBlank(build.getUrl())) {
                    if (!methodUrls.isEmpty()) {
                        for (MethodUrl methodUrl : methodUrls) {
                            if (build.getUrl().equalsIgnoreCase(methodUrl.getUri())
                                    && build.getRequestMethod().equalsIgnoreCase(methodUrl.getMethod())) {
                                log.warn("该接口已存在不用重复添加{},{}",build.getDescription(),build.getUrl());
                                continue loop;
                            }
                        }
                    }
                }else {
                    continue ;
                }

                urls.add(build);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urls;
    }


}
