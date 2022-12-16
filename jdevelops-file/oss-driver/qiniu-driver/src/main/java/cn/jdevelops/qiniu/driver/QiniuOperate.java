package cn.jdevelops.qiniu.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;
import cn.jdevelops.file.config.OSSConfig;
import cn.jdevelops.file.util.StrUtil;
import cn.jdevelops.file.util.UrlUtil;
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
        String originalName = uploaded.getFile().getOriginalFilename();
        String freshName;
        if(StrUtil.notBlank(uploaded.getFileName())){
            freshName = uploaded.getFileName().trim() + originalName.substring(originalName.lastIndexOf("."));
        }else {
            freshName = originalName;
        }
        String updateFile = uploaded.getChildFolder() + freshName;
        Response response = this.uploadManager.put(uploaded.getFile().getInputStream(),
                updateFile,
                getUploadToken(uploaded.getBucket()),
                null,
                null);
        Gson gson = new Gson();
        DefaultPutRet defaultPutRet = gson.fromJson(response.bodyString(), DefaultPutRet.class);
        return FilePathResult.builder().freshName(freshName)
                .absolutePath(updateFile)
                .originalName(originalName)
                .relativePath(ossConfig.getBrowseUrl()+"/"+updateFile).build();

    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        //构造私有空间的需要生成的下载的链接
        String  encodeName = URLEncoder.encode(download.getChildFolder_FreshName(),"UTF-8").replaceAll("\\+", "%20");
        String urlString = ossConfig.getBrowseUrl()+"/"+encodeName;
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(urlString, 3600);
        String childFolder_freshName = download.getChildFolder_FreshName();
        String fileName = childFolder_freshName.substring(childFolder_freshName.lastIndexOf('/') + 1);
        try {
            URL url = new URL(downloadRUL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            response.reset();//避免空行
            // 设置response的Header
            response.setContentType(UrlUtil.getContentType(downloadRUL) + ";charset=utf-8");
            //setContentType 设置发送到客户机的响应的内容类型
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
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
        String browseUrl = ossConfig.getBrowseUrl();
        DownloadUrl url = new DownloadUrl(browseUrl.substring(browseUrl.indexOf("//")+1), ossConfig.getQiniu()
                .getHttps(), expireDate.getChildFolder_FreshName());
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
