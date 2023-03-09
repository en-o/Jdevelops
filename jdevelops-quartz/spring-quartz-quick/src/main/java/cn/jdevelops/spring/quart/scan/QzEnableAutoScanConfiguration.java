package cn.jdevelops.spring.quart.scan;

import cn.jdevelops.spring.quart.ScheduleService;
import cn.jdevelops.spring.quart.ScheduleServiceImpl;
import cn.jdevelops.spring.quart.dao.QrtzJobDetailsDao;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@Configuration
@EnableJpaRepositories(basePackages = "cn.jdevelops.spring.quart.dao")
@EntityScan("cn.jdevelops.spring.quart.entity")
public class QzEnableAutoScanConfiguration {


    @Bean
    @ConditionalOnMissingBean(name = "scheduleService")
    public ScheduleService scheduleService(Scheduler scheduler, QrtzJobDetailsDao jobDetailsDao) {
        return new ScheduleServiceImpl(scheduler, jobDetailsDao);
    }


}
