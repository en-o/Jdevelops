package cn.tannn.jdevelops.files.sdk.config;

import cn.tannn.cat.file.sdk.bean.FileStorage;
import cn.tannn.cat.file.sdk.enums.StorageDict;
import cn.tannn.cat.file.sdk.enums.StorageMaster;
import cn.tannn.cat.file.sdk.exception.FileException;
import cn.tannn.jdevelops.files.sdk.config.properties.FtpProperties;
import cn.tannn.jdevelops.files.sdk.config.properties.LocalProperties;
import cn.tannn.jdevelops.files.sdk.config.properties.MinioProperties;
import cn.tannn.jdevelops.files.sdk.config.properties.QiNiuProperties;
import cn.tannn.jdevelops.result.exception.ExceptionCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * spirng  properties
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/2 上午11:04
 */
@ConfigurationProperties(prefix = "jdevelops.oss")
public class OssConfig {

    /**
     * 设置主配置
     * <p> 目前只支持[local,minio,七牛,ftp]
     *
     * @see StorageDict
     */
    private StorageDict master;

    /**
     * 配置
     */
    @NestedConfigurationProperty
    private FtpProperties ftp;


    /**
     * 配置
     */
    @NestedConfigurationProperty
    private LocalProperties local;


    /**
     * 配置
     */
    @NestedConfigurationProperty
    private MinioProperties minio;


    /**
     * 配置
     */
    @NestedConfigurationProperty
    private QiNiuProperties qiniu;


    public StorageDict getMaster() {
        return master;
    }

    public void setMaster(StorageDict master) {
        this.master = master;
    }

    public FtpProperties getFtp() {
        return ftp;
    }

    public void setFtp(FtpProperties ftp) {
        this.ftp = ftp;
    }

    public LocalProperties getLocal() {
        return local;
    }

    public void setLocal(LocalProperties local) {
        this.local = local;
    }

    public MinioProperties getMinio() {
        return minio;
    }

    public void setMinio(MinioProperties minio) {
        this.minio = minio;
    }

    public QiNiuProperties getQiniu() {
        return qiniu;
    }

    public void setQiniu(QiNiuProperties qiniu) {
        this.qiniu = qiniu;
    }


    /**
     * 获取 master 的  FileStorage
     *
     * @return FileStorage
     */
    public FileStorage genMasterStorage() {
        if ("60".equalsIgnoreCase(master.getValue())) {
            FileStorage storage = ftp.toStorage();
            storage.setMaster(StorageMaster.MASTER);
            return storage;
        } else if ("10".equalsIgnoreCase(master.getValue())) {
            FileStorage storage = local.toStorage();
            storage.setMaster(StorageMaster.MASTER);
            return storage;
        } else if ("20".equalsIgnoreCase(master.getValue())) {
            FileStorage storage = minio.toStorage();
            storage.setMaster(StorageMaster.MASTER);
            return storage;
        } else if ("40".equalsIgnoreCase(master.getValue())) {
            FileStorage storage = qiniu.toStorage();
            storage.setMaster(StorageMaster.MASTER);
            return storage;
        } else {
            throw new FileException(new ExceptionCode(11002, "暂不支持[" + master.getType() + "]存储"));
        }
    }


    /**
     * 根据ID获取  FileStorage
     *
     * @param id [ftp:1, local:2 , minio:3 , qiniu:4 ]
     * @return FileStorage
     */
    public FileStorage genMasterStorage(Long id) {
        if( null != ftp && id.equals(ftp.getId())){
            return ftp.toStorage();
        }else if (null != local && id.equals(local.getId())) {
            return local.toStorage();
        } else if (null != minio && id.equals(minio.getId())) {
            return minio.toStorage();
        } else if (null != qiniu && id.equals(qiniu.getId())) {
            return qiniu.toStorage();
        } else {
            throw new FileException(
                    new ExceptionCode(11002
                            , "非法的ID/未在配置文件中为相应的ID配置元数据，ID:" + id)
            );
        }

    }

}
