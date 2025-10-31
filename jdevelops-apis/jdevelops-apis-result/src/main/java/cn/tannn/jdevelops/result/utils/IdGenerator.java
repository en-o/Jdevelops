package cn.tannn.jdevelops.result.utils;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.CRC32;

/**
 * UUID生成器
 * 优势：
 * <p>
 *   1. 使用标准UUID格式（128位），数据库兼容性好
 *   2. 内嵌父级哈希，可验证父子关系
 *   3. 包含类型信息（文章/页面/段落）
 *   4. 支持Base64URL短链接格式
 *   5. 线程安全的雪花算法
 * </p>
 */
public final class IdGenerator {

    // 默认epoch：2024-01-01 00:00:00 UTC (可用到2093年)
    public static final long DEFAULT_EPOCH = 1704067200000L;

    // --- Snowflake params ---
    private final long epoch;
    private final long datacenterId;
    private final long workerId;

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private final Object lock = new Object();

    public enum Type {
        ARTICLE(0), PAGE(1), PARAGRAPH(2), OTHER(3);
        private final int code;
        Type(int c) { this.code = c; }
        public int code() { return code; }
        public static Type fromCode(int c) {
            for (Type t : values()) if (t.code == c) return t;
            return OTHER;
        }
    }

    /**
     * 构造函数（使用默认epoch）
     * @param workerId 工作机器ID (0-31)
     * @param datacenterId 数据中心ID (0-31)
     */
    public IdGenerator(long workerId, long datacenterId) {
        this(workerId, datacenterId, DEFAULT_EPOCH);
    }

    /**
     * 构造函数（自定义epoch）
     * @param workerId 工作机器ID (0-31)
     * @param datacenterId 数据中心ID (0-31)
     * @param epochMillis 自定义epoch时间戳（毫秒）
     */
    public IdGenerator(long workerId, long datacenterId, long epochMillis) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("workerId out of range [0-31]: " + workerId);
        }
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException("datacenterId out of range [0-31]: " + datacenterId);
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.epoch = epochMillis;
    }

    /**
     * 快速创建实例（使用默认配置）
     * 适合单机或开发环境使用
     */
    public static IdGenerator createDefault() {
        return new IdGenerator(1, 1);
    }

    /**
     * 生成新的UUID
     * @param parent 父级UUID（文章时为null，页面时为文章UUID，段落时为页面UUID）
     * @param type 类型
     * @return 新生成的UUID
     */
    public UUID nextId(UUID parent, Type type) {
        long low = buildLowBits(parent, type);

        long high;
        synchronized (lock) {
            long ts = currentTime();
            if (ts < lastTimestamp) {
                ts = waitUntil(lastTimestamp);
            }
            if (ts == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                if (sequence == 0) {
                    ts = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = ts;

            long timestampPart = (ts - epoch) & ((1L << 41) - 1);
            high = (timestampPart << TIMESTAMP_SHIFT)
                   | (datacenterId << DATACENTER_ID_SHIFT)
                   | (workerId << WORKER_ID_SHIFT)
                   | sequence;
        }

        return new UUID(high, low);
    }

    private long buildLowBits(UUID parent, Type type) {
        int parentHash = parent == null ? 0 : parentHash32(parent);
        int typeBits = type == null ? 0 : (type.code() & 0xF);
        int random28 = ThreadLocalRandom.current().nextInt(1 << 28);

        long low = ((long) parentHash & 0xFFFFFFFFL) << 32;
        low |= ((long) (typeBits & 0xF)) << 28;
        low |= ((long) random28) & 0x0FFFFFFFL;
        return low;
    }

    private static int parentHash32(UUID parent) {
        CRC32 crc = new CRC32();
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(parent.getMostSignificantBits());
        bb.putLong(parent.getLeastSignificantBits());
        crc.update(bb.array());
        return (int) crc.getValue();
    }

    // ========== 父子关系验证 ==========

    /**
     * 验证child是否是parent的直接子节点
     * @param parent 父级UUID
     * @param child 子级UUID
     * @return true表示是父子关系
     */
    public boolean verifyParentChild(UUID parent, UUID child) {
        if (parent == null || child == null) {
            return false;
        }
        ParseResult childResult = parse(child);
        int expectedHash = parentHash32(parent);
        return childResult.parentHash32 == expectedHash;
    }

    /**
     * 验证类型层级关系是否正确
     * 例如：PAGE的父级必须是ARTICLE，PARAGRAPH的父级必须是PAGE
     */
    public boolean verifyTypeHierarchy(UUID parent, UUID child) {
        if (parent == null || child == null) {
            return false;
        }

        Type parentType = parse(parent).type;
        Type childType = parse(child).type;

        // 验证类型层级规则
        if (childType == Type.PAGE) {
            return parentType == Type.ARTICLE;
        } else if (childType == Type.PARAGRAPH) {
            return parentType == Type.PAGE;
        }

        return false;
    }

    /**
     * 提取父级的哈希指纹（用于查询）
     */
    public static int extractParentHash(UUID id) {
        long low = id.getLeastSignificantBits();
        return (int) ((low >>> 32) & 0xFFFFFFFFL);
    }

    /**
     * 提取类型信息
     */
    public static Type extractType(UUID id) {
        long low = id.getLeastSignificantBits();
        int typeBits = (int) ((low >>> 28) & 0xFL);
        return Type.fromCode(typeBits);
    }

    /**
     * 计算UUID的可用时间范围（到哪一年）
     */
    public Instant getMaxTimestamp() {
        long maxOffset = (1L << 41) - 1; // 41位最大值
        return Instant.ofEpochMilli(epoch + maxOffset);
    }

    public long getEpoch() {
        return epoch;
    }

    // ========== 解析工具 ==========

    public static class ParseResult {
        public final Instant timestamp;
        public final long datacenterId;
        public final long workerId;
        public final long sequence;
        public final int parentHash32;
        public final Type type;
        public final int random28;

        public ParseResult(Instant ts, long dc, long w, long seq, int pHash, Type type, int rand28) {
            this.timestamp = ts;
            this.datacenterId = dc;
            this.workerId = w;
            this.sequence = seq;
            this.parentHash32 = pHash;
            this.type = type;
            this.random28 = rand28;
        }

        @Override
        public String toString() {
            return String.format(
                    "ParseResult{timestamp=%s, dc=%d, worker=%d, seq=%d, parentHash=0x%08X, type=%s, random=%d}",
                    timestamp, datacenterId, workerId, sequence, parentHash32, type, random28
            );
        }
    }

    public ParseResult parse(UUID id) {
        long high = id.getMostSignificantBits();
        long low = id.getLeastSignificantBits();

        long sequence = high & ((1L << SEQUENCE_BITS) - 1);
        long worker = (high >> WORKER_ID_SHIFT) & ((1L << WORKER_ID_BITS) - 1);
        long datacenter = (high >> DATACENTER_ID_SHIFT) & ((1L << DATACENTER_ID_BITS) - 1);
        long tsPart = (high >> TIMESTAMP_SHIFT) & ((1L << 41) - 1);
        long ts = tsPart + this.epoch;

        int parentHash = (int) ((low >>> 32) & 0xFFFFFFFFL);
        int typeBits = (int) ((low >>> 28) & 0xFL);
        int random28 = (int) (low & 0x0FFFFFFFL);

        return new ParseResult(
                Instant.ofEpochMilli(ts),
                datacenter,
                worker,
                sequence,
                parentHash,
                Type.fromCode(typeBits),
                random28
        );
    }

    // ========== Base64URL编码（短链接） ==========

    public static String toBase64Url(UUID id) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bb.array());
    }

    public static UUID fromBase64Url(String s) {
        byte[] bytes = Base64.getUrlDecoder().decode(s);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long hi = bb.getLong();
        long lo = bb.getLong();
        return new UUID(hi, lo);
    }

    // ========== 时间工具方法 ==========

    private long tilNextMillis(long last) {
        long ts = currentTime();
        while (ts <= last) {
            ts = currentTime();
        }
        return ts;
    }

    private long waitUntil(long targetTs) {
        long ts = currentTime();
        while (ts < targetTs) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            ts = currentTime();
        }
        return ts;
    }

    private static long currentTime() {
        return Instant.now().toEpochMilli();
    }

}
