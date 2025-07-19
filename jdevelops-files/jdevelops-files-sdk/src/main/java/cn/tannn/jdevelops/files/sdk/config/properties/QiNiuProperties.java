package cn.tannn.jdevelops.files.sdk.config.properties;

import cn.tannn.cat.file.sdk.bean.FileStorage;
import cn.tannn.cat.file.sdk.config.QiNiuConfig;
import cn.tannn.cat.file.sdk.enums.StorageDict;
import cn.tannn.jdevelops.files.sdk.constants.PropertiesId;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * oss配置信息
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/2 上午9:44
 */
public class QiNiuProperties {

    /**
     * 存储器设置
     */
    @NestedConfigurationProperty
    private QiNiuConfig config;


    public Long getId() {
        return PropertiesId.QINIU_ID;
    }

    public String getName() {
        return StorageDict.QINIU_STORAGE.getType();
    }

    public String getStorage() {
        return StorageDict.QINIU_STORAGE.getValue();
    }


    public QiNiuConfig getConfig() {
        return config;
    }

    public void setConfig(QiNiuConfig config) {
        this.config = config;
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
