package cn.jdevelops.data.jap.config;

import cn.jdevelops.data.jap.repository.JdevelopsSimpleJpaRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = JdevelopsSimpleJpaRepository.class)
class JpaConfiguration { }
