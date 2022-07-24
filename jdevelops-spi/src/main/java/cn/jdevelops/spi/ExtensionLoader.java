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

package cn.jdevelops.spi;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * SPI的实现方法
 *  拓展类加载器（通过加载扩展文件来完成的。）
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 10:20
 */
@SuppressWarnings("unchecked")
public class ExtensionLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionLoader.class);

    /**
     * SPI扩展文件的存放位置
     * 扩展文件命名方式：SPI接口的全路径
     */
    private static final String JDEVELOPS_DIRECTORY = "META-INF/jdevelops/";

    /**
     * 扩展接口
     */
    private final Class<T> clazz;

    /**
     * 类加载
     */
    private final ClassLoader classLoader;

    /**
     * 扩展接口和扩展类加载器的缓存
     */
    private static final Map<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    /**
     * 保存扩展的实现类
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    /**
     *  拓展名对应的类 实例化后的缓存
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();


    /**
     * 扩展实现类的实例化缓存
     */
    private final Map<Class<?>, Object> joinInstances = new ConcurrentHashMap<>();

    /**
     * 扩展点默认的名称缓存
     */
    private String cachedDefaultName;


    public ExtensionLoader(Class<T> clazz, ClassLoader classLoader) {
        this.clazz = clazz;
        this.classLoader = classLoader;
        if (!Objects.equals(clazz, ExtensionFactory.class)) {
            ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getExtensionClasses();
        }
    }


    /**
     *  装载 扩展
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param cl    the cl
     * @return the extension loader.
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz, final ClassLoader cl) {

        Objects.requireNonNull(clazz, "extension clazz is null");

        // 类必须是接口类
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") is not interface!");
        }
        // 接口类必须要又SPI注解
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") without @" + SPI.class + " Annotation");
        }
        // 从缓存中获取扩展加载器，如果存在则直接返回，不存在就创建一个扩展类加载器并放进缓存中
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
        if (Objects.nonNull(extensionLoader)) {
            return extensionLoader;
        }
        LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz, cl));
        return (ExtensionLoader<T>) LOADERS.get(clazz);
    }

    /**
     * 装载 扩展
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the extension loader
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        return getExtensionLoader(clazz, ExtensionLoader.class.getClassLoader());
    }

    /**
     * Gets default join.
     *
     * @return the default join.
     */
    public T getDefaultJoin() {
        getExtensionClasses();
        if (StringUtils.isBlank(cachedDefaultName)) {
            return null;
        }
        return getJoin(cachedDefaultName);
    }

    /**
     * Gets join.
     *
     * @param name the name
     * @return the join.
     */
    public T getJoin(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("get join name is null");
        }
        // 扩展对象存储缓存
        Holder<Object> objectHolder = cachedInstances.get(name);
        // 为空则创建并存储
        if (Objects.isNull(objectHolder)) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            objectHolder = cachedInstances.get(name);
        }
        // 从扩展对象的存储中获取扩展对象
        Object value = objectHolder.getValue();
        // 为空则创建并存储
        if (Objects.isNull(value)) {
            synchronized (cachedInstances) {
                value = objectHolder.getValue();
                if (Objects.isNull(value)) {
                    value = createExtension(name);
                    objectHolder.setValue(value);
                }
            }
        }
        return (T) value;
    }

    /**
     * get all join spi.
     *
     * @return list. joins
     */
    public List<T> getJoins() {
        Map<String, Class<?>> extensionClasses = this.getExtensionClasses();
        if (extensionClasses.isEmpty()) {
            return Collections.emptyList();
        }
        if (Objects.equals(extensionClasses.size(), cachedInstances.size())) {
            return (List<T>) this.cachedInstances.values().stream().map(Holder::getValue)
                    .collect(Collectors.toList());
        }
        List<T> joins = new ArrayList<>();
        extensionClasses.forEach((name, v) -> {
            T join = this.getJoin(name);
            joins.add(join);
        });
        return joins;
    }

    /**
     * 根据扩展名 反射创建扩展对象的实例
     * @param name name
     */
    private T createExtension(final String name) {
        Class<?> aClass = getExtensionClasses().get(name);
        if (Objects.isNull(aClass)) {
            throw new IllegalArgumentException("name is error");
        }
        Object o = joinInstances.get(aClass);
        if (Objects.isNull(o)) {
            try {
                // 创建并缓存
                joinInstances.putIfAbsent(aClass, aClass.newInstance());
                o = joinInstances.get(aClass);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Extension instance(name: " + name + ", class: "
                        + aClass + ")  could not be instantiated: " + e.getMessage(), e);

            }
        }
        return (T) o;
    }

    /**
     * Gets extension classes.
     *
     * @return the extension classes
     */
    public Map<String, Class<?>> getExtensionClasses() {
        // 扩展文件中的扩展对象是 k，v格式的。 name=com.xx.cc
        Map<String, Class<?>> classes = cachedClasses.getValue();
        if (Objects.isNull(classes)) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getValue();
                if (Objects.isNull(classes)) {
                    classes = loadExtensionClass();
                    cachedClasses.setValue(classes);
                }
            }
        }
        return classes;
    }

    /**
     * 加载扩展接口类并缓存
     * @return Map
     */
    private Map<String, Class<?>> loadExtensionClass() {
        SPI annotation = clazz.getAnnotation(SPI.class);
        if (Objects.nonNull(annotation)) {
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                cachedDefaultName = value;
            }
        }
        Map<String, Class<?>> classes = new HashMap<>(16);
        loadDirectory(classes);
        return classes;
    }

    /**
     * 加载JDEVELOPS_DIRECTORY下的扩展文件
     *
     */
    private void loadDirectory(final Map<String, Class<?>> classes) {
        String fileName = JDEVELOPS_DIRECTORY + clazz.getName();
        try {
            // 读取配置文件 （接口的是实现类集合）
            Enumeration<URL> urls = Objects.nonNull(this.classLoader) ? classLoader.getResources(fileName)
                    : ClassLoader.getSystemResources(fileName);
            if (Objects.nonNull(urls)) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResources(classes, url);
                }
            }
        } catch (IOException t) {
            LOG.error("load extension class error {}", fileName, t);
        }
    }

    private void loadResources(final Map<String, Class<?>> classes, final URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                // 解析 k,v格式，
                // 扩展实现类的名字
                String name = (String) k;
                // 扩展实现类的全路径
                String classPath = (String) v;
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
                        // 加载扩展的实现类并缓存
                        loadClass(classes, name, classPath);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("load extension resources error", e);
                    }
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("load extension resources error", e);
        }
    }

    private void loadClass(final Map<String, Class<?>> classes,
                           final String name, final String classPath) throws ClassNotFoundException {
        Class<?> subClass = Objects.nonNull(this.classLoader) ? Class.forName(classPath, true, this.classLoader) : Class.forName(classPath);
        if (!clazz.isAssignableFrom(subClass)) {
            throw new IllegalStateException("load extension resources error," + subClass + " subtype is not of " + clazz);
        }
        // 必须要有 JoinSPI 注解才能作为扩展类的实现
        if (!subClass.isAnnotationPresent(JoinSPI.class)) {
            throw new IllegalStateException("load extension resources error," + subClass + " without @" + JoinSPI.class + " annotation");
        }
        Class<?> oldClass = classes.get(name);
        if (Objects.isNull(oldClass)) {
            classes.put(name, subClass);
        } else if (!Objects.equals(oldClass, subClass)) {
            if (subClass.getAnnotation(JoinSPI.class).cover()){
                classes.put(name, subClass);
                LOG.warn(name+"被"+subClass.getName()+"覆盖");
            }else {
                throw new IllegalStateException("load extension resources error,Duplicate class " + clazz.getName() + " name " + name + " on " + oldClass.getName() + " or " + subClass.getName());
            }

        }
    }

    /**
     * The type Holder.
     *
     * @param <T> the type parameter.
     */
    @SuppressWarnings("all")
    public static class Holder<T> {

        private volatile T value;

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(final T value) {
            this.value = value;
        }
    }
}
