//snippet-sourcedescription:[S3ClientFactory.java demonstrates how to create instances of the Amazon Simple Storage Service (Amazon S3) TransferManager.]
//snippet-keyword:[AWS SDK for Java v2]
//snippet-service:[Amazon S3]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/
package cn.jdevelops.file.oss.driver.aws3.config;

import cn.jdevelops.file.oss.api.config.OSSConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 * <p>
 * For more information, see the following documentation topic:
 * <p>
 * <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html">...</a>
 *
 * @author tan
 */
@AutoConfiguration
public class S3ClientFactory {


    @ConditionalOnMissingBean(OSSConfig.class)
    @Bean
    public OSSConfig ossConfig() {
        return new OSSConfig();
    }


    @Bean
    public S3Presigner getS3Presigner(OSSConfig ossConfig) {
        S3Presigner.Builder builder = S3Presigner.builder();
        builder.region(Region.of(ossConfig.getAws3().getRegionId()));
        if(StringUtils.isNotBlank(ossConfig.getAws3().getAccessKey())
                && StringUtils.isNotBlank(ossConfig.getAws3().getSecretKey())){

            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(ossConfig.getAws3().getAccessKey(),
                            ossConfig.getAws3().getSecretKey())));
        }else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        return builder.build();
    }

    /**
     * S3客户端
     *  好像还有个异步，后面写吧
     * @return S3Client
     */
    @Bean
    public S3Client s3Client(OSSConfig ossConfig) {
        /*
         * 默认凭证提供者链由该DefaultCredentialsProvider类实现。
         * 它会按顺序检查您可以设置默认配置以提供临时证书的每个位置，然后选择您设置的第一个位置。
         * https://docs.aws.amazon.com/zh_cn/sdk-for-java/latest/developer-guide/credentials-chain.html
         */
        S3ClientBuilder s3 = S3Client.builder();
        if(StringUtils.isNotBlank(ossConfig.getAws3().getAccessKey())
                && StringUtils.isNotBlank(ossConfig.getAws3().getSecretKey())){

            s3.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(ossConfig.getAws3().getAccessKey(),
                            ossConfig.getAws3().getSecretKey())));
        }else {
            s3.credentialsProvider(DefaultCredentialsProvider.create());
        }

        s3.region(Region.of(ossConfig.getAws3().getRegionId()));


        return s3.build();
    }
}
