package cn.jdevelops.qiniu.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;
import cn.jdevelops.file.config.OSSConfig;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


/**
 * 七牛云上传下载文件
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:57
 */
@SuppressWarnings("all")
public class QiniuOperate implements OssOperateAPI {

    private static final Logger LOG = LoggerFactory.getLogger(QiniuOperate.class);


    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;


    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {
        String originalFilename = uploaded.getFile().getOriginalFilename();
        String updateFile = uploaded.getChildFolder() + originalFilename;
        Response response = this.uploadManager.put(uploaded.getFile().getInputStream(),
                updateFile,
                getUploadToken(uploaded.getBucket()),
                null,
                null);
        Gson gson = new Gson();
        DefaultPutRet defaultPutRet = gson.fromJson(response.bodyString(), DefaultPutRet.class);
        return FilePathResult.builder().freshName(defaultPutRet.key)
                .absolutePath(updateFile)
                .originalName(originalFilename)
                .relativePath(ossConfig.getBrowseUrl()+"/"+updateFile).build();

    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        //构造私有空间的需要生成的下载的链接
        String urlString = ossConfig.getBrowseUrl()+"/"+download.getChildFolder_FreshName();
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(urlString, 3600);
        String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
        try {
            URL url = new URL(downloadRUL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            response.reset();//避免空行
            // 设置response的Header
            String suffixName = urlString.substring(downloadRUL.lastIndexOf("."));
            String imgType = "image/" + suffixName;
            response.setContentType(imgType + ";charset=utf-8");
            //setContentType 设置发送到客户机的响应的内容类型
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //设置文件大小
            response.setHeader("Content-Length", String.valueOf(url.openConnection().getContentLength()));

            IOUtils.copy(conn.getInputStream(), response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) throws Exception {
        // domain   下载 domain, eg: qiniu.com【必须】
        // useHttps 是否使用 https【必须】
        // key      下载资源在七牛云存储的 key【必须】
        DownloadUrl url = new DownloadUrl(ossConfig.getBrowseUrl(), ossConfig.getHttps(), expireDate.getChildFolder_FreshName());
        // 带有效期
        long expireInSeconds = expireDate.getExpires();
        return url.buildURL(auth, expireInSeconds);
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) throws Exception {
        try {
            List<String> childFolder_freshName = remove.getChildFolder_FreshName();
            for (String file : childFolder_freshName) {
                try {
                    bucketManager.delete(remove.getBucket(), file);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取上传凭证
     *
     * @return String
     */
    private String getUploadToken(String bucket) {
        //上传策略
        StringMap policy = new StringMap();
        return this.auth.uploadToken(bucket, null, 3600, policy);
    }

}
