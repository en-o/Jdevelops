package cn.tannn.jdevelops.files.sdk.init;

import cn.tannn.cat.file.sdk.core.OssCache;
import cn.tannn.cat.file.sdk.core.minio.MinioUtils;
import cn.tannn.cat.file.sdk.core.qiniu.QiNiuUtils;
import cn.tannn.jdevelops.files.sdk.config.OssConfig;
import cn.tannn.jdevelops.files.sdk.config.properties.FtpProperties;
import cn.tannn.jdevelops.files.sdk.config.properties.MinioProperties;
import cn.tannn.jdevelops.files.sdk.config.properties.QiNiuProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * 初始化
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 上午11:56
 */
public class InitOss  implements ApplicationRunner {

    private final OssConfig ossConfig;

    public InitOss(OssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FtpProperties ftp = ossConfig.getFtp();
        if (Objects.nonNull(ftp)) {
            OssCache.ftpConnectInfoCache.put(ftp.getId(), ftp.toStorage());
        }

        MinioProperties minio = ossConfig.getMinio();
        if (Objects.nonNull(minio)) {
            MinioUtils.createMinioClient(minio.toStorage());
        }

        QiNiuProperties qiniu = ossConfig.getQiniu();
        if (Objects.nonNull(qiniu)) {
            QiNiuUtils.createQiNiuClient(qiniu.toStorage());
        }
    }
}
