package cn.jdevelops.file.oss.driver.aws3.core;

import cn.jdevelops.file.oss.api.OssOperateAPI;
import cn.jdevelops.file.oss.api.bean.*;
import cn.jdevelops.file.oss.api.config.OSSConfig;
import cn.jdevelops.file.oss.api.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    @Autowired
    private S3Client s3Client;

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
        String absolutePath =  ossConfig.getBrowseUrl()+"/"+downPath;


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(uploaded.getBucket())
                // 文件名称
                .key(downPath)
                .build();

        s3Client.putObject(objectRequest,
                RequestBody.fromBytes(uploaded.getFile().getBytes()));

        FilePathResult filePathResult = new FilePathResult();
        filePathResult.setAbsolutePath(absolutePath);
        filePathResult.setRelativePath("/"+downPath);
        filePathResult.setFreshName(freshName);
        filePathResult.setDownPath(downPath);
        filePathResult.setOriginalName(originalName);
        return filePathResult;
    }

    @Override
    public List<FilePathResult> uploadFile(UploadsDTO uploaded) throws Exception {
        ArrayList<FilePathResult> results = new ArrayList<>();
        uploaded.getFiles().forEach(file -> {
            try {
                UploadDTO uploadDTO = new UploadDTO();
                uploadDTO.setFile(file.getFile());
                uploadDTO.setFileName(file.getFileName());
                uploadDTO.setBucket(uploaded.getBucket());
                uploadDTO.setChildFolder(uploaded.getChildFolder());
                results.add(uploadFile(uploadDTO));
            }catch (Exception e){
                LOG.error("批量上传有数据报错，可忽略",e);
            }
        });
        return results;
    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(download.getBucket())
                // 密钥名称
                .key(download.getDownPath())
                .build();
        s3Client.getObject(getObjectRequest);
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
                            // 文件名
                            .key(file)
                            .build();
                    s3Client.deleteObject(deleteObjectRequest);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
