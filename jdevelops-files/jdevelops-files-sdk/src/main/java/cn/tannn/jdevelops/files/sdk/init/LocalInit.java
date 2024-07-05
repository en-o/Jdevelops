package cn.tannn.jdevelops.files.sdk.init;

import cn.tannn.cat.file.sdk.config.LocalConfig;
import cn.tannn.cat.file.sdk.core.local.LocalDirverUtil;
import cn.tannn.jdevelops.files.sdk.config.OssConfig;
import cn.tannn.jdevelops.files.sdk.config.properties.LocalProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;


/**
 * 映射目录访问路径
 *
 * @author tn
 * @version 1
 * @date 2020/6/18 16:04
 */
@Configuration
public class LocalInit implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(LocalInit.class);
    private static final String PATH_STR_1 = "/";
    private static final String PATH_STR_2 = "//";

    private final OssConfig ossConfig;

    public LocalInit(OssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }


    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {

        LocalProperties local = ossConfig.getLocal();
        if (Objects.nonNull(local)) {
            try {
                LocalConfig config = local.getConfig();
                if (config != null) {
                    String uploadDir = config.getUploadDir();
                    if (uploadDir != null && !uploadDir.isEmpty()) {
                        if (!uploadDir.endsWith(PATH_STR_1) && !uploadDir.endsWith(PATH_STR_2)) {
                            uploadDir = uploadDir + PATH_STR_1;
                        }
                        registry.addResourceHandler(LocalDirverUtil.contextPath(config.getContextPath()))
                                .addResourceLocations("file:" + uploadDir);
                    }
                }
            } catch (Exception e) {
                logger.error("本地磁盘上传配置异常，请检查", e);
            }
        }


    }

    /**
     * 判非空
     *
     * @param str str
     * @return true false
     */
    private static boolean notBlank(String str) {
        return Objects.nonNull(str) && !str.isEmpty();
    }
}
