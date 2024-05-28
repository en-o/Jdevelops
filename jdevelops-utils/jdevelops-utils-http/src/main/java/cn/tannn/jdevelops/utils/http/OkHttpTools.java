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

package cn.tannn.jdevelops.utils.http;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpTools.
 *
 * @author xiaoyu
 */
public final class OkHttpTools {
    private static final Logger LOG = LoggerFactory.getLogger(HttpContextUtils.class);
    /**
     * The constant JSON.
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpTools OK_HTTP_TOOLS = new OkHttpTools();

    private final OkHttpClient client;

    private OkHttpTools() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }

    private OkHttpTools(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        client = builder.build();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static OkHttpTools Default() {
        return OK_HTTP_TOOLS;
    }

    /**
     * GET
     *
     * @param url  the url
     * @return the string
     * @throws IOException the io exception
     */
    public String get(final String url) throws IOException {
        LOG.debug(" ===> post  url = {}", url );
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute().body().string();
    }


    /**
     * Post jsonStr.
     *
     * @param url  the url
     * @param json the json
     * @return the string
     * @throws IOException the io exception
     */
    public String post(final String url, final String json) throws IOException {
        LOG.debug(" ===> post  url = {}, json = {}", url, json );
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request).execute().body().string();
    }


    public void sse(String url, EventSourceListener eventSourceListener) {
        LOG.debug(" ===> get sse url  = {} ", url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        EventSource.Factory factory = EventSources.createFactory(client);
        //创建事件
        factory.newEventSource(request, eventSourceListener);
    }
}
