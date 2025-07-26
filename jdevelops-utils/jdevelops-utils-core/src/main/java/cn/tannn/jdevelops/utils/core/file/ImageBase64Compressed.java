package cn.tannn.jdevelops.utils.core.file;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * ImageBase64压缩工具类
 */
public class ImageBase64Compressed {

    // Base64字符验证正则表达式
    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/]*={0,2}$");

    // 支持的图片格式前缀
    private static final Pattern DATA_URL_PATTERN = Pattern.compile("^data:image/(jpeg|jpg|png|gif|bmp|webp);base64,");

    /**
     * 数据库类型枚举（内置）
     */
    public enum DatabaseType {
        /**
         * MySQL TEXT字段 - 65,535字节限制
         */
        MYSQL_TEXT(48000, "MySQL TEXT字段"),

        /**
         * MySQL MEDIUMTEXT字段 - 16MB限制
         */
        MYSQL_MEDIUMTEXT(12000000, "MySQL MEDIUMTEXT字段"),

        /**
         * MySQL LONGTEXT字段 - 4GB限制
         */
        MYSQL_LONGTEXT(50000000, "MySQL LONGTEXT字段"),

        /**
         * PostgreSQL TEXT字段 - 理论无限制，实际约1GB
         */
        POSTGRESQL_TEXT(12000000, "PostgreSQL TEXT字段");

        private final int maxSize;
        private final String description;

        DatabaseType(int maxSize, String description) {
            this.maxSize = maxSize;
            this.description = description;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 验证并清理Base64字符串
     */
    private static String validateAndCleanBase64(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            throw new IllegalArgumentException("Base64字符串不能为空");
        }

        // 移除data URL前缀
        String cleanBase64 = base64String.trim();
        if (DATA_URL_PATTERN.matcher(cleanBase64).find()) {
            cleanBase64 = cleanBase64.replaceFirst("^data:image/[^;]+;base64,", "");
        }

        // 移除可能的空白字符
        cleanBase64 = cleanBase64.replaceAll("\\s+", "");

        // 验证Base64格式
        if (!BASE64_PATTERN.matcher(cleanBase64).matches()) {
            throw new IllegalArgumentException("无效的Base64格式");
        }

        // 检查长度是否为4的倍数，如果不是则添加padding
        int paddingLength = (4 - cleanBase64.length() % 4) % 4;
        if (paddingLength > 0) {
            cleanBase64 += "=".repeat(paddingLength);
        }

        return cleanBase64;
    }

    /**
     * 安全解码Base64
     */
    private static byte[] safeDecodeBase64(String base64String) {
        try {
            String cleanBase64 = validateAndCleanBase64(base64String);
            return Base64.getDecoder().decode(cleanBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64解码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 压缩图片并转换为base64，兼容指定数据库类型
     */
    public static String compressImageToBase64(String originalBase64, DatabaseType dbType) {
        return compressImageToBase64(originalBase64, dbType.getMaxSize());
    }

    /**
     * 压缩图片并转换为base64，指定最大大小
     */
    public static String compressImageToBase64(String originalBase64, int maxSize) {
        try {
            // 验证输入
            if (originalBase64 == null || originalBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("输入的Base64字符串不能为空");
            }

            if (maxSize <= 0) {
                throw new IllegalArgumentException("最大大小必须大于0");
            }

            // 安全解码base64
            byte[] imageBytes = safeDecodeBase64(originalBase64);

            // 验证是否为有效图片数据
            BufferedImage originalImage;
            try {
                originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            } catch (IOException e) {
                throw new IllegalArgumentException("无法读取图片数据，可能不是有效的图片格式", e);
            }

            if (originalImage == null) {
                throw new IllegalArgumentException("无效的图片数据或不支持的图片格式");
            }

            // 如果原图已经满足大小要求，直接返回
            if (originalBase64.length() <= maxSize) {
                return originalBase64;
            }

            // 逐步压缩直到满足大小要求
            BufferedImage compressedImage = originalImage;
            float quality = 0.9f;
            int targetWidth = originalImage.getWidth();
            int targetHeight = originalImage.getHeight();

            // 设置最小尺寸限制
            final int MIN_WIDTH = 50;
            final int MIN_HEIGHT = 50;
            final float MIN_QUALITY = 0.1f;
            final float QUALITY_STEP = 0.1f;
            final double SIZE_REDUCTION_FACTOR = 0.8;

            int iterations = 0;
            final int MAX_ITERATIONS = 50; // 防止无限循环

            while (iterations < MAX_ITERATIONS) {
                iterations++;

                // 尝试调整尺寸
                if (quality <= MIN_QUALITY) {
                    int newWidth = (int) (targetWidth * SIZE_REDUCTION_FACTOR);
                    int newHeight = (int) (targetHeight * SIZE_REDUCTION_FACTOR);

                    if (newWidth < MIN_WIDTH || newHeight < MIN_HEIGHT) {
                        break; // 已达到最小尺寸限制
                    }

                    targetWidth = newWidth;
                    targetHeight = newHeight;
                    quality = 0.9f;

                    // 重新调整图片尺寸
                    compressedImage = resizeImage(originalImage, targetWidth, targetHeight);
                }

                // 压缩图片
                String compressedBase64 = compressImage(compressedImage, quality);

                if (compressedBase64.length() <= maxSize) {
                    return compressedBase64;
                }

                quality -= QUALITY_STEP;
            }

            // 如果还是太大，返回最小版本
            BufferedImage minImage = resizeImage(originalImage, MIN_WIDTH, MIN_HEIGHT);
            return compressImage(minImage, MIN_QUALITY);

        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出参数异常
        } catch (Exception e) {
            throw new RuntimeException("图片压缩失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调整图片尺寸
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        if (targetWidth <= 0 || targetHeight <= 0) {
            throw new IllegalArgumentException("目标宽度和高度必须大于0");
        }

        // 处理透明图片
        int imageType = originalImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM || hasTransparency(originalImage)) {
            imageType = BufferedImage.TYPE_INT_ARGB;
        } else {
            imageType = BufferedImage.TYPE_INT_RGB;
        }

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, imageType);

        Graphics2D g2d = resizedImage.createGraphics();
        try {
            // 设置高质量渲染提示
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                    java.awt.RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(scaledImage, 0, 0, null);
        } finally {
            g2d.dispose();
        }

        return resizedImage;
    }

    /**
     * 检查图片是否包含透明通道
     */
    private static boolean hasTransparency(BufferedImage image) {
        return image.getColorModel().hasAlpha();
    }

    /**
     * 压缩图片为指定质量
     */
    private static String compressImage(BufferedImage image, float quality) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("图片对象不能为null");
        }

        quality = Math.max(0.1f, Math.min(1.0f, quality));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // 如果图片有透明通道，先转换为RGB
            BufferedImage processedImage = image;
            if (hasTransparency(image)) {
                processedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = processedImage.createGraphics();
                try {
                    g2d.setColor(java.awt.Color.WHITE); // 设置白色背景
                    g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
                    g2d.drawImage(image, 0, 0, null);
                } finally {
                    g2d.dispose();
                }
            }

            // 使用JPEG格式压缩
            javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            if (writer == null) {
                throw new IOException("找不到JPEG编码器");
            }

            try {
                javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality);
                }

                javax.imageio.stream.ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
                if (ios == null) {
                    throw new IOException("无法创建图片输出流");
                }

                try {
                    writer.setOutput(ios);
                    writer.write(null, new javax.imageio.IIOImage(processedImage, null, null), param);
                } finally {
                    ios.close();
                }
            } finally {
                writer.dispose();
            }

            byte[] compressedBytes = baos.toByteArray();
            if (compressedBytes.length == 0) {
                throw new IOException("压缩后的图片数据为空");
            }

            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(compressedBytes);

        } finally {
            baos.close();
        }
    }

    /**
     * 获取压缩后的图片信息
     */
    public static CompressionResult getCompressionInfo(String originalBase64, DatabaseType dbType) {
        String compressed = compressImageToBase64(originalBase64, dbType);

        return new CompressionResult(
                originalBase64.length(),
                compressed.length(),
                dbType,
                compressed
        );
    }

    /**
     * 验证Base64图片字符串
     */
    public static boolean isValidBase64Image(String base64String) {
        try {
            byte[] imageBytes = safeDecodeBase64(base64String);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return image != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 压缩结果信息类
     */
    public static class CompressionResult {
        private final int originalSize;
        private final int compressedSize;
        private final DatabaseType dbType;
        private final String compressedData;

        public CompressionResult(int originalSize, int compressedSize,
                                 DatabaseType dbType, String compressedData) {
            this.originalSize = originalSize;
            this.compressedSize = compressedSize;
            this.dbType = dbType;
            this.compressedData = compressedData;
        }

        public int getOriginalSize() { return originalSize; }
        public int getCompressedSize() { return compressedSize; }
        public DatabaseType getDbType() { return dbType; }
        public String getCompressedData() { return compressedData; }

        public double getCompressionRatio() {
            return originalSize > 0 ? (double) compressedSize / originalSize : 0;
        }

        public int getSavedSize() {
            return originalSize - compressedSize;
        }

        public double getCompressionPercentage() {
            return originalSize > 0 ? (1.0 - getCompressionRatio()) * 100 : 0;
        }

        public boolean isWithinLimit() {
            return compressedSize <= dbType.getMaxSize();
        }

        @Override
        public String toString() {
            return String.format(
                    "CompressionResult{原始大小=%d, 压缩后大小=%d, 压缩比=%.2f%%, 节省空间=%.2f%%, 数据库类型=%s, 符合限制=%s}",
                    originalSize, compressedSize, getCompressionRatio() * 100,
                    getCompressionPercentage(), dbType.getDescription(), isWithinLimit()
            );
        }
    }
}
