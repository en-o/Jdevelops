package cn.jdevelops.config.standalone.controller;

import cn.jdevelops.api.result.response.PageResult;
import cn.jdevelops.api.result.response.ResultPageVO;
import cn.jdevelops.api.result.response.ResultVO;
import cn.jdevelops.config.standalone.controller.dto.ConfigsPage;
import cn.jdevelops.config.standalone.model.Configs;
import cn.jdevelops.config.standalone.service.ConfigsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户端注册上来的配置信息接口
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/28 13:02
 */
@RestController
@RequestMapping("configs")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigsService configsService;


    /**
     * 查询配置
     *
     * @return Configs of List
     */
    @GetMapping("/findAll")
    public ResultVO<List<Configs>> findAll() {
        return ResultVO.success(configsService.findAll());
    }

    /**
     * 查询配置
     *
     * @param app 应用
     * @param env 环境
     * @param ns  namespace
     * @return Configs of List
     */
    @GetMapping("/list")
    public ResultVO<List<Configs>> list(String app, String env, String ns) {
        return ResultVO.success(configsService.list(app, env, ns));
    }

    @RequestMapping("/update")
    public ResultVO<String> update(@RequestParam("app") String app,
                                   @RequestParam("env") String env,
                                   @RequestParam("ns") String ns,
                                   @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> configsService.update(new Configs().create(app, env, ns, k, v)));
        return ResultVO.successMessage("更新成功");
    }


    @RequestMapping("/add")
    public ResultVO<String> add(@RequestParam("app") String app,
                                @RequestParam("env") String env,
                                @RequestParam("ns") String ns,
                                @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> {
                if(!configsService.select(app, env, ns, k).isPresent()){
                    configsService.insert(new Configs().create(app, env, ns, k, v));
                }
        });
        return ResultVO.successMessage("添加成功");
    }


    @PostMapping("/page")
    public ResultPageVO<PageResult<Configs>> page(@RequestBody ConfigsPage page) {
        Page<Configs> rows = configsService.page(page);
        PageResult<Configs> pageResult = new PageResult<>(rows.getNumber() + 1
                , rows.getSize()
                , rows.getTotalPages()
                , rows.getTotalElements()
                , rows.getContent()
        );
        return ResultPageVO.success(pageResult);
    }

}
