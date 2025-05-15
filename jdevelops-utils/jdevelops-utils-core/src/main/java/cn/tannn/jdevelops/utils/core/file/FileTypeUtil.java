package cn.tannn.jdevelops.utils.core.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * 文件类型工具类
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/15 11:59
 */
public class FileTypeUtil {

    /**
     * 通过读取文件的初始字节来确定其魔数是否与允许的类型之一匹配。
     * 这是基于文件头的快速检查，并不能保证文件是格式良好的。
     *
     * @param file 要验证的 MultipartFile 对象。
     * @param allowedTypes 一个包含允许文件类型的 FileType 枚举集合。
     * @return 如果文件的魔数与允许的类型之一匹配，则返回 true，否则返回 false。
     */
    public static boolean isValidFileType(MultipartFile file, Set<FileMagic> allowedTypes) {
        if (file == null || file.isEmpty() || allowedTypes == null || allowedTypes.isEmpty()) {
            // 优雅地处理空或空输入
            // LOG.warn("Validation called with null file, empty file, or empty allowed types set.");
            return false;
        }

        // 确定我们需要读取的最大字节数，以覆盖所有允许的魔数
        int maxBytesToRead = 0;
        for (FileMagic type : allowedTypes) {
            if (type != null) {
                maxBytesToRead = Math.max(maxBytesToRead, type.getMaxMagicLength());
            }
        }

        // 如果没有提供有效的类型，则返回 false
        if (maxBytesToRead == 0) {
            // LOG.warn("Validation called with allowed types set containing only nulls or no valid types.");
            return false;
        }

        // 从文件中读取初始字节
        byte[] fileMagic = new byte[maxBytesToRead];
        try (InputStream fis = file.getInputStream()) {
            int bytesRead = fis.read(fileMagic);

            if (bytesRead == -1) {
                // 文件为空
                // LOG.warn("File is empty, no bytes read.");
                return false;
            }

            // 如果读取的字节数少于 maxBytesToRead，则截断缓冲区
            if (bytesRead < maxBytesToRead) {
                fileMagic = Arrays.copyOf(fileMagic, bytesRead);
            }

        } catch (IOException e) {
            // LOG.error("Error reading file input stream for magic number check", e);
            return false;
        }

        // 检查读取的魔数是否与允许的类型之一匹配
        for (FileMagic allowedType : allowedTypes) {
            if (allowedType == null) continue; // 跳过集合中的空条目

            for (byte[] magicPattern : allowedType.getMagicNumbers()) {
                // 确保我们读取的字节数足以匹配该模式
                if (fileMagic.length >= magicPattern.length) {
                    boolean matches = true;
                    for (int i = 0; i < magicPattern.length; i++) {
                        if (fileMagic[i] != magicPattern[i]) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        return true; // 找到匹配项
                    }
                }
            }
        }

        // 在允许的类型中未找到匹配项
        return false;
    }

    /**
     * 验证文件是否为 PDF。
     */
    public static boolean isValidPdf(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF));
    }

    /**
     * 验证文件是否为传统 DOC（Word 97-2003）。
     */
    public static boolean isValidDoc(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.DOC));
    }

    /**
     * 验证文件是否为 ZIP 归档文件（包括 DOCX、XLSX、PPTX 等）。
     */
    public static boolean isValidZip(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.ZIP));
    }

    /**
     * 验证文件是否为 RAR 归档文件（涵盖常见的旧版本和新版本）。
     */
    public static boolean isValidRAR(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.RAR, FileMagic.RAR_NEW));
    }

    /**
     * 验证文件是否为 7z 归档文件。
     */
    public static boolean isValid7z(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.SEVEN_Z));
    }

    /**
     * 验证文件是否为 TGZ 归档文件（检查 GZIP 头）。
     */
    public static boolean isValidTgz(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.GZIP));
    }

    /**
     * 验证文件是否为 PDF、DOC 或任何支持的基于 ZIP 的类型（ZIP、DOCX、XLSX、PPTX）。
     */
    public static boolean isValidPdfDocZip(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.ZIP));
    }

    /**
     * 验证文件是否为 PDF、DOC 或任何支持的归档类型（ZIP、RAR、7z、TGZ）。
     * 这更接近原始方法的意图，尽管使用 GZIP 枚举来表示 TGZ。
     */
    public static boolean isValidPdfDocArchive(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.ZIP, FileMagic.RAR, FileMagic.RAR_NEW, FileMagic.SEVEN_Z, FileMagic.GZIP));
    }
}
