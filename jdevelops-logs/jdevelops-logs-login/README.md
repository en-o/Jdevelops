# quick
1. 引入当前依赖
> 注意相关依赖 `jdevelops-utils-aop`

```xml
<dependency>
    <groupId>cn.tannn.jdevelops</groupId>
    <artifactId>jdevelops-logs-login</artifactId>
    <version>1.0.2</version>
</dependency>
```
2. 设置ip2region
```java
package cn.tannn.test.logslogin.config;

import cn.tannn.ip2region.sdk.config.SearcherConfig;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;

/**
 * ip region 数据初始化加载到内存里
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/3 14:19
 */
@Component
public class Ip2regionInit implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ip2regionInit.class);


    /**
     * 在服务启动时，将 ip2region 加载到内存中
     * @see <a href="https://gitee.com/lionsoul/ip2region/tree/master/binding/java#%E7%BC%93%E5%AD%98%E6%95%B4%E4%B8%AA-xdb-%E6%95%B0%E6%8D%AE">缓存整个-xdb-数据</a>
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("加载ip2region文件 starting");
        try {
            InputStream inputStream = new ClassPathResource("/ipdb/ip2region.xdb").getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            SearcherConfig.searcher = Searcher.newWithBuffer(bytes);
        } catch (Exception exception) {
            LOGGER.error("加载ip2region文件失败 ===> 请仔细阅读 resources/ipdb/readme.txt ", exception);
        }
    }
}

```

3. 设置存储
```java
import cn.tannn.jdevelops.logs.model.LoginLogRecord;
import cn.tannn.jdevelops.logs.service.LoginLogSave;
import cn.tannn.test.logslogin.storage.LoginLog;
import cn.tannn.test.logslogin.storage.LoginLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * 保存，需要时重写里面的保存方法即可实现数据自定义存入
 * 保存
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/20 下午3:01
 */
@Service
public class StorageLoginLogSave implements LoginLogSave {

    @Autowired
    private LoginLogDao loginLogDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageLoginLogSave.class);

    @Override
    @Async
    public void saveLog(LoginLogRecord audit) {
//        try {
//            SignEntity<Object> tokenBySignEntity = JwtWebUtil.getTokenBySignEntity(audit.getRequest());
//            System.out.println("=======================");
//            System.out.println(tokenBySignEntity);
//            System.out.println("=======================");
//        }catch (Exception e){
//
//        }

//        LOGGER.info("{}-日志自定义输出:{}", System.currentTimeMillis(), audit.toStyle());
        loginLogDao.save(new LoginLog().value(audit));
    }
}
```

4. 接口上使用
```java
  @Operation(summary = "上下文设置")
    @ApiOperationSupport(order = 7)
    @ApiMapping(value = "/login6",checkToken = false, method = RequestMethod.POST)
    @LoginLog(type = LoginType.ADMIN_ACCOUNT_PASSWORD)
    public TokenSign login7(@RequestBody LoginDto1 name) throws LoginException {
        SignEntity<String> signEntity = SignEntity.init(name.loginName);
        LoginContext loginContext = LoginContextHolder.getContext()
                .setLoginName(name.loginName)
                .setUserId(name.getId())
                .setName(name.name);
        if("user42".equals(name.getLoginName())){
            loginContext.setPlatform(PlatformConstant.APPLET);
        } else if ("demo55".equals(name.getLoginName())) {
            throw new LoginException("登录失败");
        }
        signEntity.setMap("hi");
        return loginService.login(signEntity);
    }
```
