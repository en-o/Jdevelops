package cn.jdevelops.file.oss.driver.aws3.core;

import cn.jdevelops.file.oss.api.OssOperateAPI;
import cn.jdevelops.file.oss.api.bean.*;
import cn.jdevelops.file.oss.api.config.OSSConfig;
import cn.jdevelops.file.oss.api.constants.OSSConstants;
import cn.jdevelops.file.oss.api.util.StrUtil;
import cn.jdevelops.file.oss.driver.aws3.config.S3ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * aws s3 文件操作
 * <a href="https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java">...</a>
 * @author tan
 */
public class Aws3Operate  implements OssOperateAPI {
    private static final Logger LOG = LoggerFactory.getLogger(Aws3Operate.class);

    @Autowired
    private OSSConfig ossConfig;
    private S3ClientFactory s3ClientFactory;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        String originalName = uploaded.getFile().getOriginalFilename();
        String freshName;
        if(StrUtil.notBlank(uploaded.getFileName())){
            freshName = uploaded.getFileName().trim() + originalName.substring(originalName.lastIndexOf("."));
        }else {
            freshName = originalName;
        }
        String childFolder = Objects.isNull(uploaded.getChildFolder()) ? "" : uploaded.getChildFolder();
        String downPath =  childFolder + freshName;
        String relativePath =  ossConfig.getBrowseUrl()+"/"+downPath;


        S3Client s3Client = s3ClientFactory.s3Client(ossConfig);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(uploaded.getBucket())
                // 密钥名称
                .key(ossConfig.getAws3().getAccessKey())
                .build();

        s3Client.putObject(objectRequest,
                RequestBody.fromFile(uploaded.getFile().getResource().getFile()));

        FilePathResult filePathResult = new FilePathResult();
        filePathResult.setAbsolutePath(ossConfig.getBrowseUrl() + OSSConstants.PATH_SEPARATOR + relativePath);
        filePathResult.setRelativePath(relativePath);
        filePathResult.setFreshName(freshName);
        filePathResult.setDownPath(downPath);
        filePathResult.setOriginalName(originalName);
        return filePathResult;
    }

    @Override
    public List<FilePathResult> uploadFile(UploadsDTO uploaded) throws Exception {
        return null;
    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(download.getBucket()+"/"+download.getDownPath())
                // 密钥名称
                .key(ossConfig.getAws3().getAccessKey())
                .build();
        s3ClientFactory.s3Client(ossConfig).getObject(getObjectRequest);
    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) throws Exception {
        return null;
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) throws Exception {

        try {
            List<String> childFolder_freshName = remove.getDownPath();
            for (String file : childFolder_freshName) {
                try {
                    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                            .bucket(remove.getBucket()+"/"+file)
                            // 密钥名称
                            .key(ossConfig.getAws3().getAccessKey())
                            .build();
                    s3ClientFactory.s3Client(ossConfig).deleteObject(deleteObjectRequest);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
