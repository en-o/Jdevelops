package cn.jdevelops.file.oss.driver.aws3.util;

import cn.jdevelops.file.oss.driver.aws3.core.Aws3Operate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

/**
 * s3工具类
 * @author tan
 */
public class Aws3Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Aws3Operate.class);

    /**
     * 创建桶
     * Create a bucket by using a S3Waiter object
     * @param s3Client s3Client
     * @param bucketName bucketName
     * @param region region
     */
    public static void createBucket(S3Client s3Client, String bucketName, Region region) {

        S3Waiter s3Waiter = s3Client.waiter();

        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(
                            CreateBucketConfiguration.builder()
                                    .locationConstraint(region.id())
                                    .build())
                    .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Wait until the bucket is created and print out the response
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
        } catch (S3Exception e) {
//            e.awsErrorDetails().errorMessage()
            LOG.error("s3桶创建错误",e);
            System.exit(1);
        }
    }
}
