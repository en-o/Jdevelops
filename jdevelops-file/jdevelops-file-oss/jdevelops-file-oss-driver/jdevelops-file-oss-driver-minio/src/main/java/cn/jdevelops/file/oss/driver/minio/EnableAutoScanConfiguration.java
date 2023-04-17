package cn.jdevelops.file.oss.driver.minio;

import cn.jdevelops.file.oss.api.config.OSSConfig;
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



    @ConditionalOnMissingBean(OSSConfig.class)
    @Bean
    public OSSConfig ossConfig(){
        return new OSSConfig();
    }


    @Bean
    public MinioClient getMinioClient(OSSConfig ossConfig) {
        MinioClient minioClient;
        if (ossConfig.getMinio().getPort() == null) {
            minioClient = MinioClient.builder().endpoint(ossConfig.getMinio().getUploadUrl())
                    .credentials(ossConfig.getMinio().getAccessKey(),
                            ossConfig.getMinio().getSecretKey()).build();
        } else {
            minioClient = MinioClient.builder().endpoint(ossConfig.getMinio()
                                    .getUploadUrl(),
                            ossConfig.getMinio()
                                    .getPort(),
                            ossConfig.getMinio()
                                    .getHttps())
                    .credentials(ossConfig.getMinio()
                            .getAccessKey(), ossConfig.getMinio()
                            .getSecretKey()).build();
        }
        return minioClient;
    }


    @ConditionalOnMissingBean(name = {"operateFileUtil"})
    @Bean
    public OperateFileUtil operateFileUtil(OSSConfig ossConfig, MinioClient minioClient){
        return new OperateFileUtil(minioClient, ossConfig);
    }
}
