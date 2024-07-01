package cn.tannn.jdevelops.files.sdk;

import cn.tannn.cat.file.sdk.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 导入
 * <p>  启用oss jdevelops.oss.enable= ftp/local/minio/qiniu
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/1 下午4:29
 */
public class ImportsConfiguration {

    //  oss 配置元信息
    @Bean
    @ConfigurationProperties(prefix = "jdevelops.oss.ftp")
    @ConditionalOnProperty(prefix = "jdevelops.oss", name = "enable", havingValue = "ftp")
    public FtpConfig ftpConfig() {
        return new FtpConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "jdevelops.oss.local")
    @ConditionalOnProperty(prefix = "jdevelops.oss", name = "enable", havingValue = "local")
    public LocalConfig localConfig() {
        return new LocalConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "jdevelops.oss.minio")
    @ConditionalOnProperty(prefix = "jdevelops.oss", name = "enable", havingValue = "fminiotp")
    public MinioConfig minioConfig() {
        return new MinioConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "jdevelops.oss.qiniu")
    @ConditionalOnProperty(prefix = "jdevelops.oss", name = "enable", havingValue = "qiniu")
    public QiNiuConfig qiNiuConfig() {
        return new QiNiuConfig();
    }
}
