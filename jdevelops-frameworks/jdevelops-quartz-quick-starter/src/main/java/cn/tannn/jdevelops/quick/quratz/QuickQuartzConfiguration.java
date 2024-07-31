package cn.tannn.jdevelops.quick.quratz;

import cn.tannn.jdevelops.quartz.AutoRegisterJob;
import cn.tannn.jdevelops.quartz.config.QuartzConfig;
import cn.tannn.jdevelops.quartz.dao.*;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import cn.tannn.jdevelops.quartz.service.ScheduleServiceImpl;
import cn.tannn.jdevelops.quick.quratz.controller.QzController;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * spring boot 注入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@ConditionalOnProperty(name = "jdevelops.quartz.enabled", havingValue = "true", matchIfMissing = true)
public class QuickQuartzConfiguration {


    @Bean
    public QrtzCronTriggersDao qrtzCronTriggersDao(@Autowired EntityManager entityManager) {
        return new QrtzCronTriggersDaoImpl(entityManager);
    }

    @Bean
    public QrtzJobDetailsDao qrtzJobDetailsDao(@Autowired EntityManager entityManager) {
        return new QrtzJobDetailsDaoImpl(entityManager);
    }



    @Bean
    public QrtzTriggersDao qrtzTriggersDao(@Autowired EntityManager entityManager) {
        return new QrtzTriggersDaoImpl(entityManager);
    }

    @Bean
    public ScheduleService scheduleService(Scheduler scheduler, QrtzJobDetailsDao jobDetailsDao) {
        return new ScheduleServiceImpl(scheduler, jobDetailsDao);
    }


    @Bean
    public QuartzConfig quartzConfig() {
        return new QuartzConfig();
    }

    @Bean
    public AutoRegisterJob autoRegisterJob(ScheduleService scheduleService
            , QuartzConfig quartzConfig) {
        return new AutoRegisterJob(scheduleService, quartzConfig);
    }




    @Bean
    public QzController qzController(ScheduleService scheduleService) {
        return new QzController(scheduleService);
    }
}
