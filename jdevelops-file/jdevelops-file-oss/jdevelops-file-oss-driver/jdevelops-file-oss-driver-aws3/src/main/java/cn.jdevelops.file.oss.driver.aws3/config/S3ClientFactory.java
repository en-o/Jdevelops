//snippet-sourcedescription:[S3ClientFactory.java demonstrates how to create instances of the Amazon Simple Storage Service (Amazon S3) TransferManager.]
//snippet-keyword:[AWS SDK for Java v2]
//snippet-service:[Amazon S3]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/
package cn.jdevelops.file.oss.driver.aws3.config;

import cn.jdevelops.file.oss.api.config.OSSConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 * <p>
 * For more information, see the following documentation topic:
 * <p>
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
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


    /**
     * 不知道干啥的 写上把
     * The S3 Transfer Manager offers a simple API that allows you to transfer a single object or a set of objects to and
     * from Amazon S3 with enhanced throughput and reliability. It leverages Amazon S3 multipart upload and
     * byte-range fetches to perform transfers in parallel. In addition, the S3 Transfer Manager also enables you to
     * monitor a transfer's progress in real-time, as well as pause the transfer for execution at a later time.
     *
     * @return S3TransferManager
     */
    @Bean
    public S3TransferManager createCustonTm(OSSConfig ossConfig) {
        // snippet-start:[s3.tm.java2.s3clientfactory.create_custom_tm]
        S3AsyncClient s3AsyncClient =
                S3AsyncClient.crtBuilder()
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .region(Region.of(ossConfig.getAws3().getRegionId()))
                        .targetThroughputInGbps(20.0)
                        .minimumPartSizeInBytes(8 * MB)
                        .build();

        S3TransferManager transferManager =
                S3TransferManager.builder()
                        .s3Client(s3AsyncClient)
                        .build();
        // snippet-end:[s3.tm.java2.s3clientfactory.create_custom_tm]
        return transferManager;
    }

    /**
     * 不知道干啥的 写上把
     * The S3 Transfer Manager offers a simple API that allows you to transfer a single object or a set of objects to and
     * from Amazon S3 with enhanced throughput and reliability. It leverages Amazon S3 multipart upload and
     * byte-range fetches to perform transfers in parallel. In addition, the S3 Transfer Manager also enables you to
     * monitor a transfer's progress in real-time, as well as pause the transfer for execution at a later time.
     *
     * @return S3TransferManager
     */
    @Bean
    public S3TransferManager createDefaultTm() {
        // snippet-start:[s3.tm.java2.s3clientfactory.create_default_tm]
        S3TransferManager transferManager = S3TransferManager.create();
        // snippet-end:[s3.tm.java2.s3clientfactory.create_default_tm]
        return transferManager;
    }

    /**
     * S3客户端
     *
     * @return S3Client
     */
    @Bean
    public S3Client s3Client(OSSConfig ossConfig) {
        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(ossConfig.getAws3().getRegionId()))
                .build();
    }
}
