/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.tannn.jdevelops.result.imports;

import cn.tannn.jdevelops.result.exception.DefaultExceptionResult;
import cn.tannn.jdevelops.result.exception.ExceptionResult;
import cn.tannn.jdevelops.result.spring.SpringContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

/**
 * 加载 默认的统一异常返回类
 * @author tannn
 */
public class DefaultExceptionResultConfiguration {

    /**
     * Application context aware application context aware.
     *
     * @return the application context aware
     */
    @Bean
    public ApplicationContextAware applicationContextAware() {
        return new JdevelopsApplicationContextAware();
    }

    /**
     * The type shenyu application context aware.
     */
    public static class JdevelopsApplicationContextAware implements ApplicationContextAware {

        @Override
        public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
            SpringContextUtils.getInstance().setCfgContext((ConfigurableApplicationContext) applicationContext);
        }
    }



    /**
     * jdevelops-apis-exception result.
     *
     * @return the jdevelops-api-exception result
     */
    @Bean
    @ConditionalOnMissingBean(value = ExceptionResult.class, search = SearchStrategy.ALL)
    public ExceptionResult<?> exceptionResult() {
        return new DefaultExceptionResult();
    }
}
