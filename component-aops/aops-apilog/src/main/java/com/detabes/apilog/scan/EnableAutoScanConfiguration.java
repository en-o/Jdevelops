package com.detabes.apilog.scan;

import com.detabes.apilog.aspect.ApiLogAspectSave;
import com.detabes.apilog.aspect.ApiLogAspectSee;
import com.detabes.apilog.server.impl.ApiLogSaveImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScan
 * @description 自动扫描
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ApiLogAspectSee.class,
        ApiLogAspectSave.class,
})
@ComponentScan("com.detabes.apilog.**")
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "apiLogSaveImpl")
    @Bean
    public ApiLogSaveImpl apiLogSaveImpl(){
        return new ApiLogSaveImpl();
    }
}
