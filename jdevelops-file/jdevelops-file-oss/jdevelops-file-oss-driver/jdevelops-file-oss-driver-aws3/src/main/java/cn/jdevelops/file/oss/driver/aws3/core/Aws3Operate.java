package cn.jdevelops.file.oss.driver.aws3.core;

import cn.jdevelops.file.oss.api.OssOperateAPI;
import cn.jdevelops.file.oss.api.bean.*;
import cn.jdevelops.file.oss.api.config.OSSConfig;
import cn.jdevelops.file.oss.api.util.AboutFileUtil;
import cn.jdevelops.file.oss.api.util.StrUtil;
import cn.jdevelops.file.oss.api.util.UrlUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * aws s3 文件操作
 * <a href="https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java">...</a>
 *
 * @author tan
 */
public class Aws3Operate implements OssOperateAPI {
    private static final Logger LOG = LoggerFactory.getLogger(Aws3Operate.class);

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner s3Presigner;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        String originalName = uploaded.getFile().getOriginalFilename();
        String freshName;
        // 文件类型后缀 如 jpg png
        String suffix = AboutFileUtil.getFileSuffix(originalName);
        if (StrUtil.notBlank(uploaded.getFileName())) {
            freshName = uploaded.getFileName().trim() + suffix;
        } else {
            freshName = originalName;
        }
        String childFolder = Objects.isNull(uploaded.getChildFolder()) ? "" : uploaded.getChildFolder();
        String downPath = childFolder + freshName;
        String absolutePath = ossConfig.getBrowseUrl() + "/" + downPath;


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(uploaded.getBucket())
                // 文件名称
                .key(downPath)
                .build();

        s3Client.putObject(objectRequest,
                RequestBody.fromBytes(uploaded.getFile().getBytes()));

        return new FilePathResult("/" + downPath,
                absolutePath,
                originalName,
                freshName,
                downPath,
                uploaded.getBucket(),
                suffix,
                uploaded.getFile().getContentType()
        );
    }

    @Override
    public List<FilePathResult> uploadFile(UploadsDTO uploaded) {
        ArrayList<FilePathResult> results = new ArrayList<>();
        uploaded.getFiles().forEach(file -> {
            try {
                UploadDTO uploadDTO = new UploadDTO();
                uploadDTO.setFile(file.getFile());
                uploadDTO.setFileName(file.getFileName());
                uploadDTO.setBucket(uploaded.getBucket());
                uploadDTO.setChildFolder(uploaded.getChildFolder());
                results.add(uploadFile(uploadDTO));
            } catch (Exception e) {
                LOG.error("批量上传有数据报错，可忽略", e);
            }
        });
        return results;
    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download){
        ExpireDateDTO expireDateDTO = new ExpireDateDTO();
        expireDateDTO.setBucket(download.getBucket());
        expireDateDTO.setExpires(100);
        expireDateDTO.setDownPath(download.getDownPath());
        String downUrl = expireDateUrl(expireDateDTO);
        String childFolder_freshName = download.getDownPath();
        String fileName = childFolder_freshName.substring(childFolder_freshName.lastIndexOf('/') + 1);
        try {
            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            response.reset();//避免空行
            // 设置response的Header
            response.setContentType(UrlUtil.getContentType(downUrl) + ";charset=utf-8");
            //setContentType 设置发送到客户机的响应的内容类型
            //设置响应头
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            //设置文件大小
            response.setHeader("Content-Length", String.valueOf(url.openConnection().getContentLength()));

            IOUtils.copy(conn.getInputStream(), response.getOutputStream());

        } catch (Exception e) {
            LOG.error("文件下载失败", e);
        }
    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(expireDate.getBucket())
                        .key(expireDate.getDownPath())
                        .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expireDate.getExpires()))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest =
                s3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) {
        List<String> childFolder_freshName = remove.getDownPath();
        for (String file : childFolder_freshName) {
            try {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(remove.getBucket())
                        // 文件名
                        .key(file)
                        .build();
                s3Client.deleteObject(deleteObjectRequest);

            } catch (Exception e) {
                LOG.error(file + "删除失败", e);
            }
        }
    }

}
