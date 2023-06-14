package cn.jdevelops.data.jap.config;

import cn.jdevelops.data.jap.repository.JdevelopsSimpleJpaRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 这里设置无效，目前必须在项目里写上@EnableJpaRepositories注解相关的内容
 * @author tan
 */
@Configuration
@EnableJpaRepositories(repositoryBaseClass = JdevelopsSimpleJpaRepository.class)
public class JpaConfiguration {

}
