package cn.tannn.jdevelops.quick.quratz;

import cn.tannn.jdevelops.quartz.dao.*;
import cn.tannn.jdevelops.quartz.entity.*;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import cn.tannn.jdevelops.quartz.service.ScheduleServiceImpl;
import cn.tannn.jdevelops.quick.quratz.controller.QzController;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * spring boot 注入
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/21 下午3:07
 */
@ConditionalOnWebApplication
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
        return new ScheduleServiceImpl(scheduler,jobDetailsDao);
    }


    @Bean
    public QrtzBlobTriggersEntity qrtzBlobTriggersEntity() {
        return new QrtzBlobTriggersEntity();
    }

    @Bean
    public QrtzCalendarsEntity qrtzCalendarsEntity() {
        return new QrtzCalendarsEntity();
    }

    @Bean
    public QrtzCronTriggersEntity qrtzCronTriggersEntity() {
        return new QrtzCronTriggersEntity();
    }

    @Bean
    public QrtzFiredTriggersEntity qrtzFiredTriggersEntity() {
        return new QrtzFiredTriggersEntity();
    }

    @Bean
    public QrtzJobDetailsEntity qrtzJobDetailsEntity() {
        return new QrtzJobDetailsEntity();
    }

    @Bean
    public QrtzLocksEntity qrtzLocksEntity() {
        return new QrtzLocksEntity();
    }

    @Bean
    public QrtzPausedTriggerGrpsEntity qrtzPausedTriggerGrpsEntity() {
        return new QrtzPausedTriggerGrpsEntity();
    }

    @Bean
    public QrtzSchedulerStateEntity qrtzSchedulerStateEntity() {
        return new QrtzSchedulerStateEntity();
    }

    @Bean
    public QrtzSimpleTriggersEntity qrtzSimpleTriggersEntity() {
        return new QrtzSimpleTriggersEntity();
    }

    @Bean
    public QrtzSimpropTriggersEntity qrtzSimpropTriggersEntity() {
        return new QrtzSimpropTriggersEntity();
    }

    @Bean
    public QrtzTriggersEntity qrtzTriggersEntity() {
        return new QrtzTriggersEntity();
    }


    @Bean
    public QzController qzController(ScheduleService scheduleService) {
        return new QzController(scheduleService);
    }
}
