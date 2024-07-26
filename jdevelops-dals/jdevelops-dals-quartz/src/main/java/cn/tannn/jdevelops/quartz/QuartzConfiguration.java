package cn.tannn.jdevelops.quartz;

import cn.tannn.jdevelops.quartz.config.QuartzConfig;
import cn.tannn.jdevelops.quartz.dao.*;
import cn.tannn.jdevelops.quartz.entity.*;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import cn.tannn.jdevelops.quartz.service.ScheduleServiceImpl;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * spring boot 注入
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@ConditionalOnWebApplication
public class QuartzConfiguration {

    @Bean
    public QuartzConfig quartzConfig() {
        return new QuartzConfig();
    }

    @Bean
    public AutoRegisterJob autoRegisterJob() {
        return new AutoRegisterJob();
    }

}
