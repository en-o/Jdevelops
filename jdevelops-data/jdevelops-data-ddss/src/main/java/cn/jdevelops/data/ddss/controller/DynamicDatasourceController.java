package cn.jdevelops.data.ddss.controller;

import cn.jdevelops.api.result.response.ResultVO;
import cn.jdevelops.data.ddss.model.AddDynamicDatasource;
import cn.jdevelops.data.ddss.model.DynamicDatasourceEntity;
import cn.jdevelops.data.ddss.service.DynamicDatasourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @Operation(summary = "删除数据源")
    @GetMapping("delete")
    @Parameter(name = "datasourceName",description = "数据源名", required = true)
    public ResultVO<String> delete(String datasourceName){
        dynamicDatasourceService.delete(datasourceName);
        return ResultVO.successMessage("删除数据源成功");
    }


    @Operation(summary = "查询数据源")
    @GetMapping("list")
    public ResultVO<List<DynamicDatasourceEntity>> list(){
        List<DynamicDatasourceEntity> all = dynamicDatasourceService.findAll();
        return ResultVO.success(all);
    }
}
