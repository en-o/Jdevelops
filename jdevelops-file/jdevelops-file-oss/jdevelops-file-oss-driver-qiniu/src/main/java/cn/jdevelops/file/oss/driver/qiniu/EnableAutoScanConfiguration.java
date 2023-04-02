package cn.jdevelops.file.oss.driver.qiniu;

import cn.jdevelops.file.oss.api.config.OSSConfig;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@Configuration
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(OSSConfig.class)
    @Bean
    public OSSConfig ossConfig(){
        return new OSSConfig();
    }
    /**
     * 配置自己空间所在的区域
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig(OSSConfig ossConfig) {
        Region region;
        switch (ossConfig.getQiniu().getRegionId()){
            case "z0":
                region = Region.region1();
                break;
            case "z1":
                region = Region.region1();
                break;
            case "na0":
                region = Region.regionNa0();
                break;
            case "as0":
                region = Region.regionAs0();
                break;
            case "fog-cn-east-1":
                region = Region.regionFogCnEast1();
                break;
            case "cn-east-2":
                region = Region.regionCnEast2();
                break;
            case "z2":
            default:
                region = Region.region2();
                break;
        }
        return new com.qiniu.storage.Configuration(region);
    }

    /**
     * 构建一个七牛上传工具实例
     * @param ossConfig  ossConfig
     */
    @Bean
    public UploadManager uploadManager(OSSConfig ossConfig) {
        return new UploadManager(qiniuConfig(ossConfig));
    }



    /**
     * 认证信息实例
     * @param ossConfig  ossConfig
     */
    @Bean
    public Auth auth(OSSConfig ossConfig) {
        return Auth.create(ossConfig.getQiniu()
                .getAccessKey(),
                ossConfig.getQiniu()
                .getSecretKey());
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager(OSSConfig ossConfig) {
        return new BucketManager(auth(ossConfig), qiniuConfig(ossConfig));
    }


}
