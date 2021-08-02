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

package com.detabes.spring.schema;

import com.detabes.spring.properties.DataBaseProperties;
import com.google.common.base.Splitter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * for execute schema sql file.
 */
@Slf4j
@Component
public class LocalDataSourceLoader implements InstantiationAwareBeanPostProcessor {

    private static final String PRE_FIX = "file:";
    private static final String AUTO_INITSCRIPT_MYSQL = "CREATE DATABASE  IF NOT EXISTS  `%s`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;";

    @Resource
    private DataBaseProperties dataBaseProperties;

    @Override
    public Object postProcessAfterInitialization(@NonNull final Object bean, final String beanName) throws BeansException {
        if ((bean instanceof DataSourceProperties) && dataBaseProperties.getInitEnable()) {
            this.init((DataSourceProperties) bean);
        }
        return bean;
    }

    @SneakyThrows
    protected void init(final DataSourceProperties properties) {
        // If jdbcUrl in the configuration file specifies the shenyu database, it is removed,
        // because the shenyu database does not need to be specified when executing the SQL file,
        // otherwise the shenyu database will be disconnected when the shenyu database does not exist
//        String jdbcUrl = StringUtils.replace(properties.getUrl(), "/shenyu?", "?");
        StringBuilder sb = new StringBuilder(properties.getUrl());
        int i = sb.lastIndexOf("/");
        int j = sb.lastIndexOf("?")<0?sb.length():sb.lastIndexOf("?");
        String jdbcUrl = sb.replace(i , j,"").toString();
        Connection connection = DriverManager.getConnection(jdbcUrl, properties.getUsername(), properties.getPassword());
        this.execute(connection,properties.getUrl().substring(i+1,j));

    }

    private void execute(final Connection conn,final String schemaName) throws Exception {
        ScriptRunner runner = new ScriptRunner(conn);
        // doesn't print logger
        runner.setLogWriter(null);
        Resources.setCharset(StandardCharsets.UTF_8);
        String initScript = dataBaseProperties.getInitScript();
        if("auto".equalsIgnoreCase(initScript)|| StringUtils.isBlank(initScript)){
            if("mysql".equalsIgnoreCase(dataBaseProperties.getDialect())){
                initScript = String.format(AUTO_INITSCRIPT_MYSQL,schemaName);
            }
            Reader fileReader = getResourceAsReaderStr(initScript);
            log.info("execute auto schema sql: {}", initScript);
            initScript.getBytes();
            runner.runScript(fileReader);
        }else {
            List<String> initScripts = Splitter.on(";").splitToList(initScript);
            for (String sqlScript : initScripts) {
                if (sqlScript.startsWith(PRE_FIX)) {
                    String sqlFile = sqlScript.substring(PRE_FIX.length());
                    Reader fileReader = getResourceAsReader(sqlFile);
                    log.info("execute auto schema sql: {}", sqlFile);
                    runner.runScript(fileReader);
                } else {
                    Reader fileReader = getResourceAsReader(sqlScript);
                    log.info("execute auto schema sql: {}", sqlScript);
                    runner.runScript(fileReader);
                }
            }
        }
        runner.closeConnection();
        conn.close();
    }

    private static Reader getResourceAsReader(final String resource) throws IOException {
        ClassPathResource classPathResource =new ClassPathResource(resource);
        return new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
    }

    private static Reader getResourceAsReaderStr(final String resource) throws IOException {
        InputStream  inputStream = new ByteArrayInputStream(resource.getBytes(StandardCharsets.UTF_8));
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
