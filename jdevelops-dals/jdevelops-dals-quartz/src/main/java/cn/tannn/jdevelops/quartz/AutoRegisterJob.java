package cn.tannn.jdevelops.quartz;

import cn.tannn.jdevelops.quartz.config.QuartzConfig;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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

    private final ScheduleService scheduleService;

    private final QuartzConfig quartzConfig;
    private final ApplicationContext context;

    public AutoRegisterJob(ScheduleService scheduleService
            , QuartzConfig quartzConfig
            , ApplicationContext context) {
        this.scheduleService = scheduleService;
        this.quartzConfig = quartzConfig;
        this.context = context;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String mainClassPath = context.getBeansWithAnnotation(SpringBootApplication.class)
                .values().iterator().next().getClass().getName();
        String packagePath = mainClassPath.substring(0, mainClassPath.lastIndexOf('.'));
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> interfaces = reflections.getTypesAnnotatedWith(Job.class);
        if (interfaces == null || interfaces.isEmpty()) {
            log.info(" no auto register job ");
        }
        for (Class<?> beanClass : interfaces) {
            try {
                Job annotation = beanClass.getAnnotation(Job.class);
                scheduleService.recurringJob((Class<? extends org.quartz.Job>) beanClass, annotation.jobName(), annotation.cron(), annotation.isStartNow());
                log.info(" auto register job success, className {}", beanClass.getName());
            } catch (Exception e) {
                log.error("auto register job failed, className {}, error message : {}", beanClass.getName(), e.getMessage());
            }
        }

    }
}
