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

package cn.jdevelops.schema;
import cn.jdevelops.schema.constant.SchemaConstant;
import cn.jdevelops.schema.properties.DataBaseProperties;
import cn.jdevelops.schema.util.StringUtil;
import com.google.common.base.Splitter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * for execute schema sql file.
 * @author tnnn
 */
@Slf4j
@Component
public class LocalDataSourceLoader implements InstantiationAwareBeanPostProcessor {

    private static final String PRE_FIX = "file:";
    private static final String AUTO_INITSCRIPT_MYSQL = "CREATE DATABASE  IF NOT EXISTS  `%s`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci ;";
    private static final String AUTO_INITSCRIPT_PGSQL = " CREATE DATABASE  %s ;";
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
        String url = properties.getUrl();
        StringBuilder sb = new StringBuilder(url);
        String sub1 = url.substring(0, !url.contains("?") ? url.length() : url.indexOf("?") - 1);
        int i = sub1.lastIndexOf("/");
        int j = sb.indexOf("?") < 0 ? sb.length() : sb.indexOf("?");
        String jdbcUrl = sb.replace(i, j, "").toString();
        Connection connection;
        AtomicInteger jdbcType = new AtomicInteger(0);
        String jdbcUrlPrefix = jdbcUrl.substring(0,jdbcUrl.lastIndexOf(":") - 1);
        if (properties.getDriverClassName().contains(SchemaConstant.POSTGRESQL)
                ||jdbcUrlPrefix.contains(SchemaConstant.POSTGRESQL)) {
            connection = DriverManager.getConnection(jdbcUrl + "/", properties.getUsername(), properties.getPassword());
            jdbcType.set(1);
        } else if (properties.getDriverClassName().contains(SchemaConstant.MYSQL)
                ||jdbcUrlPrefix.contains(SchemaConstant.MYSQL)) {
            connection = DriverManager.getConnection(jdbcUrl, properties.getUsername(), properties.getPassword());
            jdbcType.set(2);
        } else {
            log.info("暂不支持此类型数据库自动创建数据库:" + properties.getDriverClassName());
            return;
        }
        this.execute(connection, properties.getUrl().substring(i + 1, j), jdbcType);
    }

    private void execute(final Connection conn, final String schemaName, AtomicInteger jdbcType) throws Exception {
        ScriptRunner runner = new ScriptRunner(conn);
        // doesn't print logger
        runner.setLogWriter(null);
        Resources.setCharset(StandardCharsets.UTF_8);
        String initScript = dataBaseProperties.getInitScript();
        if (SchemaConstant.AUTO.equalsIgnoreCase(initScript) || StringUtil.isBlank(initScript)) {
            switch (jdbcType.get()) {
                case 1:
                    // 请查看示例 https://gist.github.com/retanoj/5fd369524a18ab68a4fe7ac5e0d121e8
                    String checkSchema = new StringBuilder().append("select count(*) as isok from pg_catalog.pg_database where datname = '").append(schemaName).append("' ;").toString();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(checkSchema);
                    /*
                     * 0不存在
                     */
                    int isok = 0;
                    while (resultSet.next()) {
                        //取出列值
                        isok = resultSet.getInt(1);
                    }
                    statement.close();
                    if (isok == 0) {
                        initScript = String.format(AUTO_INITSCRIPT_PGSQL, schemaName);
                        runner.setAutoCommit(true);
                        break;
                    }else {
                        log.info("当前库("+schemaName+")已存在，不用在自动创建");
                        runner.closeConnection();
                        conn.close();
                        return;
                    }
                case 2:
                    initScript = String.format(AUTO_INITSCRIPT_MYSQL, schemaName);
                    break;
                default:
                    runner.closeConnection();
                    conn.close();
                    return;
            }
            Reader fileReader = getResourceAsReaderStr(initScript);
            log.info("execute auto schema sql: {}", initScript);
            runner.runScript(fileReader);
        } else {
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
        ClassPathResource classPathResource = new ClassPathResource(resource);
        return new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
    }

    private static Reader getResourceAsReaderStr(final String resource)  {
        InputStream inputStream = new ByteArrayInputStream(resource.getBytes(StandardCharsets.UTF_8));
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
