package cn.jdevelops.doc.core.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * springboot 2.6 swagger 修改
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-29 13:54
 */
@ConditionalOnWebApplication
public class Springfox26 {
    private Springfox26() {
    }

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {

                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    if(Objects.nonNull(field)) {
                        field.setAccessible(true);
                        return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                    }
                    throw new IllegalArgumentException("field为空");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
