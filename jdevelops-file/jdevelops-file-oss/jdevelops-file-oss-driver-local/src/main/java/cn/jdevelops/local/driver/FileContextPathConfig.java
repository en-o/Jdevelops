package cn.jdevelops.local.driver;

import cn.jdevelops.file.config.LocalConfig;
import cn.jdevelops.file.config.OSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

import static cn.jdevelops.file.util.StrUtil.notBlank;

/**
 * 映射目录访问路径
 * @author tn
 * @version 1
 * @date 2020/6/18 16:04
 */
@Configuration
public class FileContextPathConfig implements WebMvcConfigurer {

    private static final String PATH_STR_1 = "/";
    private static final String PATH_STR_2 = "//";

    @Autowired
    private OSSConfig ossConfig;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        LocalConfig local = ossConfig.getLocal();
        if(Objects.nonNull(local)
                && notBlank(local.getUploadDir())){

            String uploadDir = local.getUploadDir();
            if(!uploadDir.endsWith(PATH_STR_1) && !uploadDir.endsWith(PATH_STR_2)){
                uploadDir  = uploadDir+PATH_STR_1;
            }

            registry.addResourceHandler(LocalDirverUtil.contextPath(local.getContextPath()))
                    .addResourceLocations("file:"+uploadDir);
        }

    }


}
