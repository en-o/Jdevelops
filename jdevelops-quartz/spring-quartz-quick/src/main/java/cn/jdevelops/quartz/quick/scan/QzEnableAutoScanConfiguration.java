package cn.jdevelops.quartz.quick.scan;

import cn.jdevelops.quartz.quick.ScheduleServiceImpl;
import cn.jdevelops.quartz.quick.ScheduleService;
import cn.jdevelops.quartz.quick.dao.QrtzJobDetailsDao;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableJpaRepositories(basePackages = "cn.jdevelops.quartz.quick.dao")
@EntityScan("cn.jdevelops.quartz.quick.entity")
public class QzEnableAutoScanConfiguration {


    @Bean
    @ConditionalOnMissingBean(ScheduleService.class)
    public ScheduleService scheduleService(Scheduler scheduler, QrtzJobDetailsDao jobDetailsDao) {
        return new ScheduleServiceImpl(scheduler, jobDetailsDao);
    }


}
