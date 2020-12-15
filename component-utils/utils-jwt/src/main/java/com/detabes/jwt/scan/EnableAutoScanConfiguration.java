package com.detabes.jwt.scan;

import com.detabes.jwt.util.ContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScan
 * @description 自动扫描
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ ContextUtil.class})
@ComponentScan("com.databstech.utils.jwt.**")
public class EnableAutoScanConfiguration {
}
