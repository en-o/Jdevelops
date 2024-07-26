package cn.tannn.jdevelops.quartz;

import cn.tannn.jdevelops.quartz.config.QuartzConfig;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自动注册job
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/26 上午10:20
 */
public class AutoRegisterJob implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AutoRegisterJob.class);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private QuartzConfig quartzConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Reflections reflections = new Reflections(quartzConfig.getBasePackage());
        Set<Class<?>> interfaces = reflections.getTypesAnnotatedWith(Job.class);
        if (interfaces == null || interfaces.isEmpty()) {
            log.info(" no auto register job ");
        }
        for (Class<?> beanClass : interfaces) {
            try {
                Job annotation = beanClass.getAnnotation(Job.class);
                scheduleService.addScheduleJob((Class<? extends org.quartz.Job>) beanClass, annotation.jobName(), annotation.cron(), annotation.isStartNow());
                log.info(" auto register job success, className {}", beanClass.getName());
            } catch (Exception e) {
                log.error("auto register job failed, className {}", beanClass.getName(), e);
            }
        }

    }
}
