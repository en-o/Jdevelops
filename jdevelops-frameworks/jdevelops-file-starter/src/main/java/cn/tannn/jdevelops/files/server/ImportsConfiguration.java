package cn.tannn.jdevelops.files.server;

import cn.tannn.jdevelops.files.sdk.FileOperateService;
import cn.tannn.jdevelops.files.server.controller.FileController;
import cn.tannn.jdevelops.files.server.controller.FileIndexMetaController;
import cn.tannn.jdevelops.files.server.controller.FtpController;
import cn.tannn.jdevelops.files.server.dao.FileIndexMetaDao;
import cn.tannn.jdevelops.files.server.dao.FileIndexMetaDaoImpl;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.files.server.service.FileIndexMetaService;
import cn.tannn.jdevelops.files.server.service.FileIndexMetaServiceImpl;
import cn.tannn.jdevelops.files.server.service.StartFileOperateService;
import cn.tannn.jdevelops.files.server.service.StartFileOperateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * spring
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 下午1:56
 */
@ConditionalOnProperty(name = "jdevelops.oss.starter.enabled", havingValue = "true", matchIfMissing = true)
public class ImportsConfiguration {


    @Bean
    public FileIndexMeta fileIndexMeta() {
        return new FileIndexMeta();
    }

    @Bean
    public FileIndexMetaDao fileIndexMetaDao(@Autowired EntityManager entityManager) {
        return new FileIndexMetaDaoImpl(entityManager);
    }


    @Bean
    @ConditionalOnMissingBean(StartFileOperateService.class)
    public StartFileOperateService startFileOperateService(FileOperateService fileOperateService,
                                                           FileIndexMetaDao fileIndexMetaDao) {
        return new StartFileOperateServiceImpl(fileOperateService, fileIndexMetaDao);
    }

    @Bean
    @ConditionalOnMissingBean(FileIndexMetaService.class)
    public FileIndexMetaService fileIndexMetaService() {
        return new FileIndexMetaServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean(FileController.class)
    public FileController fileController(StartFileOperateService startFileOperateService
            , FileIndexMetaService fileIndexMetaService
    ) {
        return new FileController(startFileOperateService, fileIndexMetaService);
    }

    @Bean
    @ConditionalOnMissingBean(FileController.class)
    public FileIndexMetaController fileIndexMetaController(StartFileOperateService startFileOperateService
            , FileIndexMetaService fileIndexMetaService
    ) {
        return new FileIndexMetaController(fileIndexMetaService, startFileOperateService);
    }

    @Bean
    @ConditionalOnMissingBean(FileController.class)
    public FtpController ftpController(FileIndexMetaService fileIndexMetaService) {
        return new FtpController(fileIndexMetaService);
    }
}
