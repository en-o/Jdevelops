package cn.tannn.jdevelops.files.server.controller;

import cn.tannn.cat.file.sdk.api.UploadFile;
import cn.tannn.cat.file.sdk.api.UploadFiles;
import cn.tannn.jdevelops.files.server.controller.vo.FileIndexVO;
import cn.tannn.jdevelops.files.server.entity.FileIndexMeta;
import cn.tannn.jdevelops.files.server.service.StartFileOperateService;
import cn.tannn.jdevelops.result.response.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/2 下午1:37
 */
@Tag(name = "文件操作", description = "文件操作")
@RequestMapping("files/operation")
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final StartFileOperateService startFileOperateService;

    public FileController(StartFileOperateService startFileOperateService) {
        this.startFileOperateService = startFileOperateService;
    }

    /**
     * master上传
     *
     * @param upload UploadFile
     * @return FileStorageVO
     * @throws IOException Exception
     */
    @PostMapping(value = "upload")
    @Operation(summary = "上传文件", description = "master上传")
    public ResultVO<FileIndexVO> upload(@Valid UploadFile upload) throws IOException {
        FileIndexMeta fileIndex = startFileOperateService.upload(upload);
        return ResultVO.success(fileIndex.toFileStorageVO());
    }


    /**
     * 选择存储器上传
     *
     * @param upload    UploadFile
     * @param storageId 存储器ID [ftp:1, local:2 , minio:3 , qiniu:4 ] ,必须是配置了
     * @return FileStorageVO
     * @throws IOException Exception
     */
    @PostMapping(value = "upload/{storageId}")
    @Operation(summary = "上传文件", description = "选择存储器上传")
    public ResultVO<FileIndexVO> upload(@PathVariable("storageId") Long storageId, @Valid UploadFile upload) throws IOException {
        return ResultVO.success(startFileOperateService.upload(storageId, upload).toFileStorageVO());
    }


    /**
     * master 批量上传文件
     *
     * @param uploads UploadFiles
     * @return String
     * @throws IOException Exception
     */

    @PostMapping(value = "uploads")
    @Operation(summary = "批量上传文件", description = "master上传")
    public ResultVO<String> uploads(@Valid UploadFiles uploads) throws IOException {
        startFileOperateService.uploads(uploads);
        return ResultVO.success("批量上传成功");
    }


    /**
     * 下载文件
     *
     * @param fileIndexId 文件索引的ID
     * @param response    HttpServletResponse
     */
    @PostMapping(value = "download")
    @Operation(summary = "下载文件")
    @Parameter(name = "fileIndexId", description = "文件索引的ID", required = true)
    public void download(@RequestParam("fileIndexId") Long fileIndexId, HttpServletResponse response) {
        startFileOperateService.download(fileIndexId, response);
    }


    /**
     * 删除文件
     *
     * @param path FileIndexMeta.path
     * @return String
     */
    @DeleteMapping("/remove")
    @Operation(summary = "删除文件")
    @Parameter(name = "path", description = "文件路径[FileIndexMeta.path]", required = true)
    public ResultVO<String> remove(@RequestParam("path") String path) {
        startFileOperateService.removeByPath(path);
        return ResultVO.successMessage("删除成功");
    }
}
