package cn.jdevelops.file;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取扩展File的实现
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 15:07
 */
public class FileExtensionLoader {

    /**
     * 扩展接口和扩展类加载器的缓存
     */
    private static final Map<Class<?>, FileApi> LOADERS = new ConcurrentHashMap<>();


    /**
     * 装载 扩展
     *
     * @return the extension loader
     */
    public static  FileApi getExtensionLoader() {
        Class<FileApi> fileApiClass = FileApi.class;
        // 从缓存中获取扩展加载器，如果存在则直接返回，不存在就创建一个扩展类加载器并放进缓存中
        FileApi bean = LOADERS.get(fileApiClass);
        if (Objects.nonNull(bean)) {
            return bean;
        }
        ServiceLoader<FileApi> fileApis = ServiceLoader.load(FileApi.class);
        for (FileApi fileApi : fileApis) {
            LOADERS.putIfAbsent(fileApiClass,fileApi);
        }
        return LOADERS.get(fileApiClass);
    }



}
