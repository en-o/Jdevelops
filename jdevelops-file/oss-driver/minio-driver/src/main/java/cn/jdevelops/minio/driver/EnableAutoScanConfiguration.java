package cn.jdevelops.minio.driver;

import cn.jdevelops.file.config.OSSConfig;
import io.minio.MinioClient;
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



    @ConditionalOnMissingBean(name = {"ossConfig"})
    @Bean
    public OSSConfig ossConfig(){
        return new OSSConfig();
    }


    @Bean
    public MinioClient getMinioClient(OSSConfig ossConfig) {
        MinioClient minioClient;
        if (ossConfig.getPort() == null) {
            minioClient = MinioClient.builder().endpoint(ossConfig.getUploadUrl())
                    .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey()).build();
        } else {
            minioClient = MinioClient.builder().endpoint(ossConfig.getUploadUrl(), ossConfig.getPort(),
                            ossConfig.getHttps())
                    .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey()).build();
        }
        return minioClient;
    }


    @ConditionalOnMissingBean(name = {"operateFileUtil"})
    @Bean
    public OperateFileUtil operateFileUtil(OSSConfig ossConfig, MinioClient minioClient){
        return new OperateFileUtil(minioClient, ossConfig);
    }
}
