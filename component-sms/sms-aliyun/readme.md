# 阿里云短信模块

## 开启短信

> 启动类上添加注解 @Sms
## 依赖添加
```java
    <dependency>
            <groupId>com.detabes</groupId>
            <artifactId>sms-aliyun</artifactId>
            <version>1.0.0</version>
        </dependency>
```

```java
@Sms
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
```

## 配置文件

```java
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
```