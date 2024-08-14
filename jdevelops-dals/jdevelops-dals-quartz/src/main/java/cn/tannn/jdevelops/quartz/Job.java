package cn.tannn.jdevelops.quartz;

import java.lang.annotation.*;

/**
 * 自定新增任务注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Job {

    /**
     * 任务名
     */
    String jobName();

    /**
     * cron表达式
     * @see <a href="https://www.yuque.com/tanning/yg9ipo/opbdsfqmhr2myz7v#GQzu2">...</a>
     * @return cron
     */
    String cron();

    /**
     * 是否立即执行（默认 false)
     * @return true 立即执行
     */
    boolean isStartNow() default false;

}
