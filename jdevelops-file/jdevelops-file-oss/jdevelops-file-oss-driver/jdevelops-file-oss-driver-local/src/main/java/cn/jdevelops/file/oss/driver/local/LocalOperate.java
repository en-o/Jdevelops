package cn.jdevelops.file.oss.driver.local;

import cn.jdevelops.file.oss.api.OssOperateAPI;
import cn.jdevelops.file.oss.api.bean.*;
import cn.jdevelops.file.oss.api.config.OSSConfig;
import cn.jdevelops.file.oss.api.constants.OSSConstants;
import cn.jdevelops.file.oss.api.util.AboutFileUtil;
import cn.jdevelops.file.oss.api.util.StrUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.jdevelops.file.oss.api.util.StrUtil.notBlank;

/**
 * 本地撒上传
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:35
 */
@SuppressWarnings("all")
public class LocalOperate implements OssOperateAPI {

    public static final String SPIRIT = "/";

    private static final Logger LOG = LoggerFactory.getLogger(LocalOperate.class);

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${server.servlet.context-path:/}")
    private String serverName;


    @Autowired
    private OSSConfig ossConfig;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        MultipartFile file = uploaded.getFile();
        // getOriginalFilename方法获取文件名(带后缀)
        String originalName = file.getOriginalFilename();
        String freshName;
        // 文件类型后缀 如 jpg png
        String suffixDot = AboutFileUtil.getFileSuffixDot(originalName);
        if(StrUtil.notBlank(uploaded.getFileName())){
            freshName = uploaded.getFileName().trim() + suffixDot;
        }else {
            freshName = LocalDirverUtil.encrypt2MD5(originalName) +suffixDot;
        }
        String childFolder = Objects.isNull(uploaded.getChildFolder()) ? "" : uploaded.getChildFolder();
        String downPath =  childFolder + freshName;
        String relativePath = uploaded.getBucket() + OSSConstants.PATH_SEPARATOR + downPath;
        File dest = new File(ossConfig.getLocal()
                .getUploadDir() + OSSConstants.PATH_SEPARATOR + relativePath);
        // 判断文件所在目录是否存在，如果不存在就创建对应的目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 将文件存放到对应的目录下，如果上传重复的文件不会报错，后上传的文件会覆盖已上传的文件
        file.transferTo(dest);
        String contextPath = LocalDirverUtil.contextPath(ossConfig.getLocal().getContextPath());
        if(notBlank(contextPath)){
            relativePath = contextPath.substring(0,contextPath.lastIndexOf("/")+1)+relativePath;
        }
        String absolutePath = getBrowserUrl() + relativePath;


        return new FilePathResult(relativePath,
                absolutePath,
                originalName,
                freshName,
                childFolder+freshName,
                uploaded.getBucket(),
                AboutFileUtil.killPrefixDot(suffixDot),
                uploaded.getFile().getContentType(),
                ossConfig.getLocal().getContextPath()
        );

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
        String filePath = download.getBucket() + OSSConstants.PATH_SEPARATOR + download.getDownPath();
        String absolutePath = ossConfig.getLocal()
                .getUploadDir() + OSSConstants.PATH_SEPARATOR + filePath;
        File file = new File(absolutePath);
        try(InputStream in = Files.newInputStream(file.toPath())) {
            response.reset();//避免空行
            // 设置response的Header
            response.setContentType(Files.probeContentType(Paths.get(absolutePath)) + ";charset=utf-8");
            //setContentType 设置发送到客户机的响应的内容类型
            //设置响应头
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20"));
            //设置文件大小
            response.setHeader("Content-Length", String.valueOf(file.length()));
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            LOG.debug("下载文件失败", e);
        }
    }


    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) throws Exception {
        return getBrowserUrl() +
                OSSConstants.PATH_SEPARATOR +
                expireDate.getBucket() + OSSConstants.PATH_SEPARATOR + expireDate.getDownPath();
    }


    @Override
    @SuppressWarnings("all")
    public void removeFiles(RemoveFileDTO remove) throws Exception {
        String bucket = remove.getBucket();
        List<String> childFolder_freshName = remove.getDownPath();
        for (String it : childFolder_freshName) {
            try {
                File file = new File(ossConfig.getLocal()
                        .getUploadDir() + OSSConstants.PATH_SEPARATOR
                        + bucket + OSSConstants.PATH_SEPARATOR
                        + it);
                file.delete();
            } catch (Exception e) {
                LOG.error("文件删除失败：{}", e);
            }
        }

    }

    private String getBrowserUrl() {
        String browseUrl = ossConfig.getBrowseUrl();
        if ("".equals(ossConfig.getBrowseUrl()) ||
                ossConfig.getBrowseUrl() == null) {
            if (SPIRIT.equals(serverName)) {
                serverName = "";
            }
            browseUrl = "http://" + LocalDirverUtil.getRealIp() + ":" + serverPort + serverName;
        }
        return browseUrl;
    }
}
