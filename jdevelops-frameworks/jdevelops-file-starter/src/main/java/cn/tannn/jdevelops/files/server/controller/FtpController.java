package cn.tannn.jdevelops.files.server.controller;

import cn.tannn.cat.file.sdk.constants.OSSConstants;
import cn.tannn.cat.file.sdk.core.ftp.FtpUtils;
import cn.tannn.cat.file.sdk.enums.StorageDict;
import cn.tannn.cat.file.sdk.utils.ResponseFile;
import cn.tannn.jdevelops.files.server.service.FileIndexMetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * ftp特殊处理
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/7/4 下午12:26
 */
@Tag(name = "ftp特殊处理", description = "ftp特殊处理")
@RestController
public class FtpController {

    private static final Logger logger = LoggerFactory.getLogger(FtpController.class);
    private final FileIndexMetaService fileIndexMetaService;

    public FtpController(FileIndexMetaService fileIndexMetaService) {
        this.fileIndexMetaService = fileIndexMetaService;
    }

    @Operation(summary = "ftp文件预览")
    @Parameter(name = "filePath", description = "文件索引的path", required = true)
    @Parameter(name = "storage", description = "文件存储器类型字典", required = true)
    @GetMapping(value ="/operation/"+ OSSConstants.FTP_VIEWS_API_NAME + "/{storage}")
    public void views(@PathVariable("storage") String storage, @RequestParam("filePath") String filePath, HttpServletResponse response) {
        fileIndexMetaService.findPathAndStorage(filePath, StorageDict.fromValue(storage)).ifPresent(index -> {
            FTPClient ftpClient = FtpUtils.createFtpClient(index.getStorageId());
            try {
                // remote:文件存储在FTP的位置
                String fileNameEncoding = new String(index.getPath().getBytes(StandardCharsets.UTF_8)
                        , StandardCharsets.ISO_8859_1);
                InputStream inputStream = ftpClient.retrieveFileStream(fileNameEncoding);

                HttpServletResponse downResponse = ResponseFile.customResponse(response
                        , index.getType(), index.getFreshName());
                downResponse.setContentType("application/octet-stream");
                //设置文件大小
                downResponse.setHeader("Content-Length", String.valueOf(index.getSize()));
                IOUtils.copy(inputStream, downResponse.getOutputStream());
            } catch (Exception e) {
                logger.error("ftp文件view失败：index id : {} , URL: {}", index.getId(), index.getUrl(), e);
            }finally {
                FtpUtils.disConnection(ftpClient);
            }
        });
    }
}
