# 阿里云短信模块

## 开启短信


## 依赖添加
```maven
    <dependency>
       <groupId>com.detabes</groupId>
       <artifactId>sms-aliyun</artifactId>
       <version>1.0.0</version>
    </dependency>
```
> 启动类上添加注解 @Sms
```java
@Sms
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
```

## 配置文件

```yaml
#  阿里云短信配置
sms:
  aliyun:
    template-code: 短信模板id
    sign-name: 短信签名
    access-key-id: 阿里云accesskeId
    access-key-secret: 阿里云密钥
```

## 使用例子

```java
package com.detabes.user;

import com.alibaba.fastjson.JSONObject;
import com.detabes.sms.aliyun.annotaion.Sms;
import com.detabes.sms.aliyun.config.SmsConfig;
import com.detabes.sms.aliyun.service.AliYunService;
import com.detabes.sms.mail.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserApplicationTests {
    @Autowired
    private AliYunService aliYunService;
    @Autowired
    private SmsConfig smsConfig;
    @Test
    void sms() {
        //发送的电话号码
        smsConfig.setPhoneNumbers("18324185370");
        //短信验证码
        JSONObject object = new JSONObject();
        object.put("code", aliYunService.getRandCode(6));
        smsConfig.setTemplateParam(object.toString());
        boolean b = aliYunService.sendSms(smsConfig);
        System.out.println(b);
    }
}

```
# spring mail邮件发送
## 开启邮件


## 依赖添加
```maven
   <dependency>
       <groupId>com.detabes</groupId>
       <artifactId>sms-mail</artifactId>
       <version>1.0.0</version>
    </dependency>
```
> 启动类上添加注解 @Mail
```java
@Mail
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
```

## 配置文件

```yaml
#  邮箱配置
spring:
  mail:
    # 88
    host: smtp.88.com
    port: 25
    username: your mial@88.com
    password: your password(非邮箱密码，为服务器动态密码)
    protocol: smtp
    default-encoding: UTF-8
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
      mail.smtp.starttls.required: true
      mail.smtp.socketFactory.port: 465
      mail.smtp.socketFactory.class: javax.net.ssl.SSLSocketFactory
      mail.smtp.socketFactory.fallback: false
```

## 使用例子

```java
package com.detabes.user;

import com.alibaba.fastjson.JSONObject;
import com.detabes.sms.aliyun.annotaion.Sms;
import com.detabes.sms.aliyun.config.SmsConfig;
import com.detabes.sms.aliyun.service.AliYunService;
import com.detabes.sms.mail.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Sms
class UserApplicationTests {
    @Autowired
    private MailService mailService;
 
    @Test
    void mail() throws MessagingException {
        /**
         *      * @param to      　　　　　收件人地址
         *      * @param subject 　　邮件主题
         *      * @param content 　　邮件内容
         *      * @param cc      　　　　　抄送地址
         */
        String adress = "702142322@qq.com";
        String subject = "测试邮件--附件";
        String content = "这是一封来自外太空的不明测试邮件！！！！";
        String[] cc = {"460160750@qq.com"};
        mailService.sendSimpleMail(adress, subject, content, cc);
        /**
         *      * @param to       收件人地址
         *      * @param subject  邮件主题
         *      * @param content  邮件内容
         *      * @param filePath 附件地址
         *      * @param cc       抄送地址
         *      F:\机械纪元\123.jpg
         *      附件文件发送
         */
        List<String> list = Arrays.asList("F:\\机械纪元\\123.jpg", "F:\\机械纪元\\124.jpg");
        mailService.sendAttachmentsMail(adress, subject, content,list,cc);
        /**
         * description: 发送正文中有网络资源的文件
         *
         * @param to      收件人地址
         * @param subject 邮件主题
         * @param content 邮件内容
         * @param urls    网络资源集合
         * @param cc      抄送地址
         * @throws MessagingException 邮件发送异常
         * @author lmz
         * @company Peter
         * @date 2021/3/29  11:35
         */

        String url="http://192.168.0.2:9000/smart/logo/logo.png";
        String url1="https://goss.cfp.cn/creative/vcg/800/new/VCG211182167893.jpg";
        List<String> list1 = Arrays.asList(url,url1);
        mailService.sendUrlResourceMail(adress,subject,content,list1,cc);

    }


}

```