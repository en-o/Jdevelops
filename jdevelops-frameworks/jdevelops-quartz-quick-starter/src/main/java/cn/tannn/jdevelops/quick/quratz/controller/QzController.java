package cn.tannn.jdevelops.quick.quratz.controller;


import cn.tannn.jdevelops.jpa.request.Pagings;
import cn.tannn.jdevelops.jpa.result.JpaPageResult;
import cn.tannn.jdevelops.quartz.dao.bo.JobAndTriggerBO;
import cn.tannn.jdevelops.quartz.service.ScheduleService;
import cn.tannn.jdevelops.result.response.ResultPageVO;
import cn.tannn.jdevelops.result.response.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import static cn.tannn.jdevelops.quartz.util.ClazzUtil.checkClasses;


/**
 * @author tnnn
 * @version V1.0
 * @date 2023-03-09 14:19
 */
@RestController
@RequestMapping("quartz")
@Tag(name = "定时器")
public class QzController {

    private final ScheduleService scheduleService;

    public QzController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @Operation(summary = "添加定时任务")
    @GetMapping("addRecurring")
    @Parameter(name = "jobBeanClass", description = "定时任务bean的类路径"
            ,example = "cn.tannn.demo.jdevelops.frameworksquick.job.TestQuartzJobBean")
    @Parameter(name = "jName", description = "任务名")
    @Parameter(name = "cron", description = "cron表达式(开始时间)", example = "0/2 * * * * ?")
    @Parameter(name = "isStartNow", description = "是否立即执行", deprecated = true)
    public ResultVO<String> addRecurring(String jobBeanClass,
                                         String jName,
                                         String cron,
                                         Boolean isStartNow) {

        scheduleService.recurringJob(checkClasses(jobBeanClass)
                , jName
                , cron, isStartNow == null || isStartNow);
        return ResultVO.successMessage("新增成功");
    }


    @Operation(summary = "添加延时任务")
    @GetMapping("addDelay")
    @Parameter(name = "jobBeanClass", description = "定时任务bean的类路径"
            ,example = "cn.tannn.demo.jdevelops.frameworksquick.job.TestQuartzJobBean")
    @Parameter(name = "jName", description = "任务名")
    @Parameter(name = "delaySeconds", description = "延时时间/秒", example = "10")
    public ResultVO<String> addDelay(String jobBeanClass,
                                     String jName,
                                     Integer delaySeconds) {

        scheduleService.delayJob(checkClasses(jobBeanClass)
                , jName
                , delaySeconds);
        return ResultVO.successMessage("新增成功");
    }


    @Operation(summary = "查询任务")
    @PostMapping("page")
    public ResultPageVO<JobAndTriggerBO, JpaPageResult<JobAndTriggerBO>> page(@RequestBody @Valid Pagings pagings) {
        Page<JobAndTriggerBO> details = scheduleService.getJobAndTriggerDetails(pagings.pageable());
        JpaPageResult<JobAndTriggerBO> pageResult = JpaPageResult.toPage(details);
        return ResultPageVO.success(pageResult, "查询成功");
    }


    @Operation(summary = "删除任务")
    @GetMapping("delete")
    @Parameter(name = "jName", description = "任务名")
    public ResultVO<String> delete(String jName) {
        scheduleService.deleteJob(jName, jName);
        return ResultVO.successMessage("删除成功");
    }


    @Operation(summary = "暂停任务")
    @GetMapping("pause")
    @Parameter(name = "jName", description = "任务名")
    public ResultVO<String> pause(String jName) {
        scheduleService.pauseJob(jName, jName);
        return ResultVO.successMessage("暂停成功");
    }


    @Operation(summary = "重启任务")
    @GetMapping("resume")
    @Parameter(name = "jName", description = "任务名")
    public ResultVO<String> resume(String jName) {
        scheduleService.resumeJob(jName, jName);
        return ResultVO.successMessage("重启成功");
    }


    @Operation(summary = "重置任务")
    @GetMapping("res")
    @Parameter(name = "jName", description = "任务名")
    @Parameter(name = "cron", description = "cron表达式(开始时间)", example = "0/2 * * * * ?")
    public ResultVO<String> resume(String jName, String cron) {
        scheduleService.resJob(jName, jName, cron);
        return ResultVO.successMessage("重置成功");
    }


    @Operation(summary = "手动执行一次[存在的任务]")
    @GetMapping("run")
    @Parameter(name = "jName", description = "任务名")
    public ResultVO<String> run(String jName) {
        scheduleService.runJob(jName, jName);
        return ResultVO.successMessage("执行成功");
    }


}
