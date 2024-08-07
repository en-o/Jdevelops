package cn.tannn.jdevelops.files.sdk;

import cn.tannn.cat.file.sdk.core.ftp.FtpOperate;
import cn.tannn.cat.file.sdk.core.local.LocalOperate;
import cn.tannn.cat.file.sdk.core.minio.MinioOperate;
import cn.tannn.cat.file.sdk.core.qiniu.QiNiuOperate;
import cn.tannn.jdevelops.files.sdk.config.OssConfig;
import cn.tannn.jdevelops.files.sdk.init.InitOss;
import cn.tannn.jdevelops.files.sdk.init.LocalInit;
import org.springframework.context.annotation.Bean;

/**
 * 导入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/1 下午4:29
 */
public class ImportsConfiguration {

    @Bean
    public OssConfig ossConfig() {
        return new OssConfig();
    }


    /**
     * local特殊处理
     */
    @Bean
    public LocalInit localInit(OssConfig ossConfig) {
        return new LocalInit(ossConfig);
    }

    @Bean
    public InitOss initOss(OssConfig ossConfig) {
        return new InitOss(ossConfig);
    }


    @Bean
    public FileOperateService fileOperateService(
            LocalOperate localOperate,
            MinioOperate minioOperate,
            QiNiuOperate qiNiuOperate,
            FtpOperate ftpOperate,
            OssConfig ossConfig
    ) {
        return new DefFileOperateService(localOperate
                , minioOperate
                , qiNiuOperate
                , ftpOperate
                , ossConfig);
    }

}
