package cn.tannn.jdevelops.logs.context;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class RequestIdGenerator {
    private static final AtomicLong SEQUENCE = new AtomicLong(0L);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 生成唯一请求ID
     * 格式: timestamp_userId_sequence_random
     */
    public static String generateRequestId(String userId) {
        StringBuilder requestId = new StringBuilder();

        // 添加时间戳 (毫秒级)
        requestId.append(Instant.now().toEpochMilli());

        if(userId != null){
            // 添加用户标识
            requestId.append("_").append(userId);
        }

        // 添加序列号
        requestId.append("_").append(SEQUENCE.incrementAndGet());

        // 添加随机数 (3位)
        requestId.append("_").append(String.format("%03d", (int) (Math.random() * 1000)));

        return requestId.toString();
    }
}
