package cn.jdevelops.file.oss.api.config;


/**
 * minio 【Version 2021-11-24T23:19:33Z】
 <p>
    nginx 完整配置
     upstream minio {
         server 127.0.0.1:9000;
         # server 127.0.0.1:9000;
     }

     upstream console {
         server 127.0.0.1:9100;
         # server 127.0.0.1:9100;
     }

     server {
         listen 443 ssl;
         server_name  _;
         ssl_certificate      /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.pem;
         ssl_certificate_key  /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.key;
         access_log   off;
         return       444;
     }

     server {
         listen   8081 ssl;
         server_name  oss.minio.com;
         ssl_certificate      /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.pem;
         ssl_certificate_key  /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.key;
         ssl_session_cache    shared:SSL:1m;
         ssl_session_timeout  5m;
         client_max_body_size 500M;
         ssl_ciphers  HIGH:!aNULL:!MD5;
         ssl_prefer_server_ciphers   on;


         location / {
             proxy_set_header Host $http_host;
             proxy_set_header X-Real-IP $remote_addr;
             proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
             proxy_set_header X-Forwarded-Proto $scheme;
             proxy_set_header X-NginX-Proxy true;

             # This is necessary to pass the correct IP to be hashed
             real_ip_header X-Real-IP;

             proxy_connect_timeout 300;

             # To support websocket
             proxy_http_version 1.1;
             proxy_set_header Upgrade $http_upgrade;
             proxy_set_header Connection "upgrade";

             chunked_transfer_encoding off;

             proxy_pass http://console;
         }
     }


     server {
         listen   443 ssl;
         server_name  oss.minio.com;
         ssl_certificate      /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.pem;
         ssl_certificate_key  /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.key;
         ssl_session_cache    shared:SSL:1m;
         ssl_session_timeout  5m;
         client_max_body_size 500M;
         ssl_ciphers  HIGH:!aNULL:!MD5;
         ssl_prefer_server_ciphers   on;

         location / {
             proxy_set_header Host $http_host;
             proxy_pass http://minio;
         }
     }

 server {
     listen       80;
     server_name  oss.minio.com;
     #charset koi8-r;
     #access_log  logs/host.access.log  main;
     rewrite ^(.*) https://$server_name$1 permanent;
 }


 </p>
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 19:05
 */

public class MinioConfig {

    /**
     * 文件上传地址 尽量是域名
     *  e.g <p><a href="www.file.com">www.file.com</a></p>
       <p>
        域名配置如下就不需要加端口：
         upstream minio {
             server 127.0.0.1:9000;
             # server 127.0.0.1:9000;
         }
     server {
         listen   443 ssl;
         server_name  oss.minio.com;
         ssl_certificate      /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.pem;
         ssl_certificate_key  /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.key;
         ssl_session_cache    shared:SSL:1m;
         ssl_session_timeout  5m;
         client_max_body_size 500M;
         ssl_ciphers  HIGH:!aNULL:!MD5;
         ssl_prefer_server_ciphers   on;

         location / {
             proxy_set_header Host $http_host;
             proxy_pass http://minio;
         }
     }

       </p>
     */
    private String uploadUrl;


    /**
     * 是否使用https
     */
    private Boolean https = false;

    /**
     * 可访问端口 （IP时用）
     <p>
     域名配置如下就不需要加端口：
     upstream minio {
         server 127.0.0.1:9000;
         # server 127.0.0.1:9000;
     }
     server {
         listen   443 ssl;
         server_name  oss.minio.com;
         ssl_certificate      /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.pem;
         ssl_certificate_key  /home/nginxconfig/https/oss.minio.com_nginx/oss.minio.com.key;
         ssl_session_cache    shared:SSL:1m;
         ssl_session_timeout  5m;
         client_max_body_size 500M;
         ssl_ciphers  HIGH:!aNULL:!MD5;
         ssl_prefer_server_ciphers   on;

         location / {
             proxy_set_header Host $http_host;
             proxy_pass http://minio;
         }
     }
     </p>
     */
    private Integer port;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码。
     */
    private String secretKey;


    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "MinioConfig{" +
                "uploadUrl='" + uploadUrl + '\'' +
                ", https=" + https +
                ", port=" + port +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
