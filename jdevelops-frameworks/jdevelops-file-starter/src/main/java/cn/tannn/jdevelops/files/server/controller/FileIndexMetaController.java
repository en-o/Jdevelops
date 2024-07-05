package cn.tannn.jdevelops.files.server.controller;

import cn.tannn.jdevelops.files.server.controller.dto.FileIndexMetaPage;
import cn.tannn.jdevelops.files.server.controller.vo.FileIndexVO;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.files.server.service.FileIndexMetaService;
import cn.tannn.jdevelops.files.server.service.StartFileOperateService;
import cn.tannn.jdevelops.jpa.result.JpaPageResult;
import cn.tannn.jdevelops.result.response.ResultPageVO;
import cn.tannn.jdevelops.result.response.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 文件索引数据管理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-09 19:28
 */
@Tag(name = "文件索引数据管理", description = "文件索引数据管理")
@RequestMapping("files/manager")
@RestController
public class FileIndexMetaController {
    private static final Logger logger = LoggerFactory.getLogger(FileIndexMetaController.class);
    private final FileIndexMetaService fileIndexMetaService;
    private final StartFileOperateService startFileOperateService;

    public FileIndexMetaController(FileIndexMetaService fileIndexMetaService
            , StartFileOperateService startFileOperateService) {
        this.fileIndexMetaService = fileIndexMetaService;
        this.startFileOperateService = startFileOperateService;
    }

    @Operation(summary = "查询文件元数据-分页")
    @PostMapping("page")
    public ResultPageVO<FileIndexVO,JpaPageResult<FileIndexVO>> page(@RequestBody @Valid FileIndexMetaPage page) {
        Page<FileIndexMeta> files = fileIndexMetaService.findPage(page, page.getPage());
        JpaPageResult<FileIndexVO> filesResult = JpaPageResult.toPage(files, FileIndexVO.class);
        return ResultPageVO.success(filesResult, "查询成功");
    }

    @GetMapping(value = "download")
    @Operation(summary = "下载文件")
    @Parameter(name = "fileIndexId", description = "存储索引ID", required = true)
    public void download(@RequestParam("fileIndexId") Long fileIndexId, HttpServletResponse response) {
        startFileOperateService.download(fileIndexId, response);
    }


    @Operation(summary = "批量删除文件元数据")
    @PostMapping("deletes")
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<String> deletes(@RequestBody List<Long> ids) {
        List<FileIndexMeta> fileStorages = fileIndexMetaService.findByIds(ids);
        // 1. 删除真实存储文件
        // 2. 删除文件记录、
        fileStorages.forEach(startFileOperateService::remove);
        return ResultVO.successMessage("存储信息和真实源文件删除成功");
    }

    /**
     * 删除文件
     *
     * @param fileIndexId fileIndexId
     * @return String
     */
    @DeleteMapping("/remove")
    @Operation(summary = "删除文件")
    @Parameter(name = "fileIndexId", description = "文件索引的ID", required = true)
    public ResultVO<String> remove(@RequestParam("fileIndexId") Long fileIndexId) {
        startFileOperateService.remove(fileIndexId);
        return ResultVO.successMessage("删除成功");
    }

}
