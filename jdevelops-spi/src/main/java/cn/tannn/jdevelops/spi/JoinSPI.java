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

package cn.tannn.jdevelops.spi;

import java.lang.annotation.*;

/**
 * SPI注解的配套注解
 *  被此注解标记的类会被添加到SPI中
 *  加载顺序：从最外层开始加载
 * @author web
 * @date 2022-04-01 10:18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JoinSPI {

    /**
     * true 表示如果存在重复则丢弃，不加入spi集合中去
     * PS: 此参数一般用在不同的jar下面对统一接口进行spi实现
     * @return boolean
     */
    boolean cover() default false;
}
