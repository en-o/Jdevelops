package cn.tannn.jdevelops.files.sdk;

import cn.tannn.cat.file.sdk.core.ftp.FtpOperate;
import cn.tannn.cat.file.sdk.core.local.LocalOperate;
import cn.tannn.cat.file.sdk.core.minio.MinioOperate;
import cn.tannn.cat.file.sdk.core.qiniu.QiNiuOperate;
import cn.tannn.jdevelops.files.sdk.config.OssConfig;
import org.springframework.context.annotation.Bean;

/**
 * 导入
 * <p>  启用oss jdevelops.oss.enable= ftp/local/minio/qiniu
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/1 下午4:29
 */
public class ImportsConfiguration {

    @Bean
    public OssConfig ossConfig() {
        return new OssConfig();
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
