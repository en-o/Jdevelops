package cn.jdevelops.qiniu.driver;

import cn.jdevelops.file.*;
import cn.jdevelops.file.bean.*;
import cn.jdevelops.file.config.OSSConfig;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 七牛云上传下载文件
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 14:57
 */
public class QiniuOperate implements OssOperateAPI {


    @Autowired
    private OSSConfig ossConfig;

    @Override
    public FilePathResult uploadFile(UploadDTO uploaded) throws Exception {

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = ossConfig.getAccessKey();
        String secretKey = ossConfig.getSecretKey();
        String bucket = uploaded.getBucket();
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploaded.getFile().getInputStream(), key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
        return null;
    }

    @Override
    public void downloadFile(HttpServletResponse response, DownloadDTO download) throws Exception {
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = "Access_Key";
        String SECRET_KEY = "Secret_Key";
        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //构造私有空间的需要生成的下载的链接
        String URL = "http://bucketdomain/key";
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(URL, 3600);
        System.out.println(downloadRUL);
    }

    @Override
    public String expireDateUrl(ExpireDateDTO expireDate) throws Exception {

        // domain   下载 domain, eg: qiniu.com【必须】
        // useHttps 是否使用 https【必须】
        // key      下载资源在七牛云存储的 key【必须】
        DownloadUrl url = new DownloadUrl(ossConfig.getBrowseUrl(), ossConfig.getHttps(), expireDate.getChildFolder_FreshName());
        // 带有效期
        long expireInSeconds = expireDate.getExpires();
        String accessKey = ossConfig.getAccessKey();
        String secretKey = ossConfig.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        return url.buildURL(auth, expireInSeconds);
    }

    @Override
    public void removeFiles(RemoveFileDTO remove) throws Exception {
        //设置需要操作的账号的AK和SK
        String accessKey = ossConfig.getAccessKey();
        String secretKey = ossConfig.getSecretKey();
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration c = new Configuration(Region.huanan());
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = remove.getBucket();
        List<String> childFolder_freshName = remove.getChildFolder_FreshName();
        for (String file : childFolder_freshName) {
            try {
                bucketManager.delete(bucket, file);
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
    }
}
