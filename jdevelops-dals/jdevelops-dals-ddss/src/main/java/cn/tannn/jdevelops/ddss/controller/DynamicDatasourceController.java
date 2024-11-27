package cn.tannn.jdevelops.ddss.controller;

import cn.tannn.jdevelops.ddss.model.AddDynamicDatasource;
import cn.tannn.jdevelops.ddss.model.DynamicDatasourceEntity;
import cn.tannn.jdevelops.ddss.model.FixDynamicDatasource;
import cn.tannn.jdevelops.ddss.service.DynamicDatasourceService;
import cn.tannn.jdevelops.result.response.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.util.List;

/**
 * 数据源管理
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 15:20
 */
@Tag(name = "数据源管理")
@RequestMapping("dbsource")
public class DynamicDatasourceController {

    public final DynamicDatasourceService dynamicDatasourceService;


    public DynamicDatasourceController(DynamicDatasourceService dynamicDatasourceService) {
        this.dynamicDatasourceService = dynamicDatasourceService;
    }

    @Operation(summary = "添加新的数据源")
    @PostMapping("add")
    public ResultVO<String> add(@RequestBody AddDynamicDatasource datasourceEntity) throws InvalidKeyException {
        dynamicDatasourceService.add(datasourceEntity);
        return ResultVO.successMessage("新增数据源成功");
    }

    @Operation(summary = "修改数据源")
    @PostMapping("fix")
    public ResultVO<List<DynamicDatasourceEntity>> fix(@RequestBody FixDynamicDatasource fix) {
        dynamicDatasourceService.updateDataSource(fix);
        return ResultVO.successMessage("修改数据源成功");
    }


    @Operation(summary = "删除数据源")
    @GetMapping("delete")
    @Parameter(name = "datasourceName", description = "数据源名", required = true)
    public ResultVO<String> delete(String datasourceName) {
        dynamicDatasourceService.delete(datasourceName);
        return ResultVO.successMessage("删除数据源成功");
    }


    @Operation(summary = "查询数据源")
    @GetMapping("list")
    public ResultVO<List<DynamicDatasourceEntity>> list() {
        List<DynamicDatasourceEntity> all = dynamicDatasourceService.findAll();
        return ResultVO.success(all);
    }

    @Operation(summary = "查询所有可用的数据源")
    @GetMapping("enableList")
    public ResultVO<List<DynamicDatasourceEntity>> enableList() {
        List<DynamicDatasourceEntity> all = dynamicDatasourceService.findEnable();
        return ResultVO.success(all);
    }


    @Operation(summary = "更新数据源状态")
    @PutMapping("/status/{datasourceName}/{enable}")
    @Parameter(name = "datasourceName", description = "数据源名称", required = true)
    @Parameter(name = "enable", description = "0:禁用 1:启用", required = true)
    public ResultVO<List<DynamicDatasourceEntity>> updateDataSourceStatus(@PathVariable("datasourceName") String datasourceName
            , @PathVariable("enable") int enable) {
        dynamicDatasourceService.updateDataSourceStatus(datasourceName, enable);
        return ResultVO.successMessage("状态更新成功");
    }
}
