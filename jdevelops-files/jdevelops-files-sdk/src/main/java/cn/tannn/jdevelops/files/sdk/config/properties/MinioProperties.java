package cn.tannn.jdevelops.files.sdk.config.properties;

import cn.tannn.cat.file.sdk.bean.FileStorage;
import cn.tannn.cat.file.sdk.config.MinioConfig;
import cn.tannn.cat.file.sdk.enums.StorageDict;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * oss配置信息
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/2 上午9:44
 */
public class MinioProperties {

    /**
     * 存储器设置
     */
    @NestedConfigurationProperty
    private MinioConfig config;


    public Long getId() {
        return 3L;
    }

    public String getName() {
        return StorageDict.MINIO_STORAGE.getType();
    }

    public String getStorage() {
        return StorageDict.MINIO_STORAGE.getValue();
    }

    public FileStorage toStorage(){
        FileStorage fileStorage = new FileStorage();
        fileStorage.setId(getId());
        fileStorage.setName(getName());
        fileStorage.setStorage(getStorage());
        fileStorage.setConfig(JSONObject.from(config));
        fileStorage.setRemark("ftp");
        return fileStorage;
    }
}
