package cn.tannn.jdevelops.jwt.redis.service;

import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import cn.tannn.jdevelops.utils.jwt.exception.LoginException;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RedisLoginServiceTest {
    private static final Logger log = LoggerFactory.getLogger(RedisLoginServiceTest.class);

    @Test
    public void testPlatformSubtract() {
        try {
            List<PlatformConstant> input = Arrays.asList(PlatformConstant.APPLET, PlatformConstant.COMMON);
            List<PlatformConstant> local = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.COMMON);
            // 新增的 platform
            Collection<PlatformConstant> subtract = CollectionUtils.subtract(input, local);
            log.info("input:"+input);
            log.info("local:"+local);
            log.info("subtract:"+subtract);
            if (!subtract.isEmpty()) {
                throw new LoginException("存储的 platform 跟新登录的platform不一致");
            }
        } catch (Exception e) {
            log.warn("1 = {}", e.getMessage());
        }
        log.info("============================================");
        try {
            List<PlatformConstant> input = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.APPLET, PlatformConstant.COMMON);
            List<PlatformConstant> local = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.COMMON);
            // 新增的 platform
            Collection<PlatformConstant> subtract = CollectionUtils.subtract(input, local);
            log.info("input:"+input);
            log.info("local:"+local);
            log.info("subtract:"+subtract);
            if (!subtract.isEmpty()) {
                throw new LoginException("存储的 platform 跟新登录的platform不一致");
            }
        } catch (Exception e) {
            log.warn("2 = {}", e.getMessage());
        }
        log.info("============================================");
        try {
            List<PlatformConstant> input = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.COMMON);
            List<PlatformConstant> local = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.APPLET, PlatformConstant.COMMON);
            // 新增的 platform
            Collection<PlatformConstant> subtract = CollectionUtils.subtract(input, local);
            log.info("input:"+input);
            log.info("local:"+local);
            log.info("subtract:"+subtract);
            if (!subtract.isEmpty()) {
                throw new LoginException("存储的 platform 跟新登录的platform不一致");
            }
        } catch (Exception e) {
            log.warn("3 = {}", e.getMessage());
        }


        log.info("============================================");
        try {
            List<PlatformConstant> input = Collections.emptyList();
            List<PlatformConstant> local = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.APPLET, PlatformConstant.COMMON);
            // 新增的 platform
            Collection<PlatformConstant> subtract = CollectionUtils.subtract(input, local);
            log.info("input:"+input);
            log.info("local:"+local);
            log.info("subtract:"+subtract);
            if (!subtract.isEmpty()) {
                throw new LoginException("存储的 platform 跟新登录的platform不一致");
            }
        } catch (Exception e) {
            log.warn("4 = {}", e.getMessage());
        }

        log.info("============================================");
        try {
            List<PlatformConstant> input = Arrays.asList(PlatformConstant.WEB_H5, PlatformConstant.COMMON);
            List<PlatformConstant> local = Collections.emptyList();
            // 新增的 platform
            Collection<PlatformConstant> subtract = CollectionUtils.subtract(input, local);
            log.info("input:"+input);
            log.info("local:"+local);
            log.info("subtract:"+subtract);
            if (!subtract.isEmpty()) {
                throw new LoginException("存储的 platform 跟新登录的platform不一致");
            }
        } catch (Exception e) {
            log.warn("5 = {}", e.getMessage());
        }

    }
}
