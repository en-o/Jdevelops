package cn.tannn.jdevelops.utils.core.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件类型工具类
 *
 * @author tan
 * @version V1.0
 * @date 2025/5/15 11:59
 */
public class FileTypeUtil {

    /**
     * 辅助方法，用于检查文件的魔数是否在指定偏移量处匹配某个模式。
     */
    private static boolean matchesMagicAtOffset(byte[] fileMagic, byte[] pattern, int offset) {
        // 文件魔数的长度必须足够覆盖指定偏移量处的模式
        if (fileMagic.length < offset + pattern.length) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (fileMagic[offset + i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取文件的初始字节并检查其魔数是否与允许的类型之一匹配。
     * 此方法支持在不同偏移量处检查魔数。
     * 这是基于文件头的快速检查，并不能保证文件是格式良好的。
     * 此方法**不**区分特定的 ZIP 子类型（如 docx、xlsx、jar 等）。
     * 如果需要区分 ZIP 子类型，请在确认文件为通用 ZIP 格式后使用 determineSpecificZipType 方法。
     *
     * @param file         要验证的 MultipartFile 对象。
     * @param allowedTypes 一个包含允许文件类型的 FileMagic 枚举集合。
     * @return 如果文件的魔数在正确偏移量处与允许的类型之一匹配，则返回 true，否则返回 false。
     */
    public static boolean isValidFileMagic(MultipartFile file, Set<FileMagic> allowedTypes) {
        if (file == null || file.isEmpty() || allowedTypes == null || allowedTypes.isEmpty()) {
            // 优雅地处理空或空输入
            // LOG.warn("Validation called with null file, empty file, or empty allowed types set.");
            return false;
        }

        // 确定我们需要读取的最大字节数，以覆盖所有允许的魔数检查
        int maxBytesToRead = 0;
        for (FileMagic type : allowedTypes) {
            if (type != null) {
                maxBytesToRead = Math.max(maxBytesToRead, type.getMaxRequiredLength());
            }
        }

        // 如果没有提供有效的类型（例如，集合为空或仅包含空值）
        if (maxBytesToRead == 0) {
            // LOG.warn("Validation called with allowed types set containing only nulls or no valid types requiring bytes.");
            return false;
        }

        // 从文件中读取初始字节
        byte[] fileMagic = new byte[maxBytesToRead];
        int bytesRead;
        try (InputStream fis = file.getInputStream()) {
            bytesRead = fis.read(fileMagic);
            if (bytesRead == -1) {
                // 文件为空
                // LOG.warn("File is empty, no bytes read.");
                return false;
            }
            // 不需要在这里截断，matchesMagicAtOffset 方法会隐式处理缓冲区长度

        } catch (IOException e) {
            // LOG.error("Error reading file input stream for magic number check", e);
            return false;
        }

        // 检查读取的魔数是否在正确偏移量处与允许的类型之一匹配
        for (FileMagic allowedType : allowedTypes) {
            if (allowedType == null) continue; // 跳过集合中的空条目

            byte[][] patterns = allowedType.getMagicPatterns();
            int[] offsets = allowedType.getOffsets();

            for (int i = 0; i < patterns.length; i++) {
                byte[] pattern = patterns[i];
                int offset = offsets[i];

                if (matchesMagicAtOffset(fileMagic, pattern, offset)) {
                    return true; // 找到匹配项
                }
            }
        }

        // 在允许的类型中未找到匹配项
        return false;
    }

    /**
     * 检查文件的初始字节以确认其为通用 ZIP 格式。
     * 这是在确定特定 ZIP 子类型之前的第一个步骤。
     */
    public static boolean isGeneralZipFormat(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.ZIP));
    }

    /**
     * 尝试通过检查文件的内部结构来确定基于 ZIP 的文件的特定子类型
     * （.zip、.jar、.war、.docx、.xlsx、.pptx）。
     * 此方法假设文件已经通过 isGeneralZipFormat 或 isValidFileMagic（包含 FileMagic.ZIP）确认为通用 ZIP 格式。
     *
     * @param file 要分析的 MultipartFile 对象。
     * @return 确定的 ZipSubMagic 类型，如果分析失败或未找到已知子类型，则返回 UNKNOWN。
     */
    public static ZipSubMagic determineSpecificZipType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            // LOG.warn("determineSpecificZipType called with null or empty file.");
            return ZipSubMagic.UNKNOWN;
        }

        // 可选：在这里添加检查，确认文件是否真的是 ZIP 格式，
        // 尽管方法约定意味着它应该是。
        // if (!isGeneralZipFormat(file)) {
        //     LOG.warn("determineSpecificZipType called on a file that is not a general ZIP format.");
        //     return ZipSubMagic.UNKNOWN;
        // }

        boolean foundManifest = false;
        boolean foundWebInf = false;
        boolean foundWordDir = false;
        boolean foundXlDir = false;
        boolean foundPptDir = false;

        try (InputStream is = file.getInputStream();
             ZipInputStream zis = new ZipInputStream(is)) {

            ZipEntry entry;
            // 遍历条目，如果找到特定类型的指示器，则停止
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                // 使用枚举中的辅助方法对条目进行分类
                ZipSubMagic entrySubType = ZipSubMagic.determineFromEntry(entryName);

                switch (entrySubType) {
                    case DOCX:
                        foundWordDir = true;
                        break;
                    case XLSX:
                        foundXlDir = true;
                        break;
                    case PPTX:
                        foundPptDir = true;
                        break;
                    case JAR:
                        foundManifest = true;
                        break; // 找到 META-INF/MANIFEST.MF
                    case WAR:
                        foundWebInf = true;
                        break;   // 找到 WEB-INF/
                    case UNKNOWN: // 继续检查其他条目
                        break;
                    default:
                        // 当前 ZipSubMagic.determineFromEntry 不应该出现这种情况
                        break;
                }

                // 优化：如果已经找到最具体类型的指示器，
                // 我们可以提前停止扫描。
                if (foundWordDir || foundXlDir || foundPptDir || (foundManifest && foundWebInf)) {
                    break;
                }

                zis.closeEntry(); // 在移动到下一个条目之前关闭当前条目
            }

        } catch (IOException e) {
            // LOG.error("Error reading ZIP file entries", e);
            return ZipSubMagic.UNKNOWN; // 读取错误意味着未知类型
        }

        // 根据找到的内容确定最终的子类型
        return ZipSubMagic.finalizeSubType(foundManifest, foundWebInf, foundWordDir, foundXlDir, foundPptDir);
    }

    // --- 常见组合或单个类型的便捷方法 ---
    // 注意：这些方法使用 EnumSet.of 表示一种或多种类型，或使用 EnumSet.allOf 表示所有支持的类型。

    /**
     * 验证文件是否为 PDF。
     */
    public static boolean isValidPdf(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.PDF));
    }

    /**
     * 验证文件是否为传统 DOC（Word 97-2003）。
     */
    public static boolean isValidDoc(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.DOC));
    }

    /**
     * 验证文件是否为传统 XLS（Excel 97-2003）。
     */
    public static boolean isValidXls(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.XLS));
    }

    /**
     * 验证文件是否为通用 ZIP 格式（不会区分子类型，如 docx、jar 等）。
     */
    public static boolean isValidGeneralZip(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.ZIP));
    }


    /**
     * 验证文件是否为 Tar 压缩文件
     */
    public static boolean isValidTar(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.TAR));
    }

    /**
     * 验证文件是否为 RAR 压缩文件（涵盖常见的旧版本和新版本）。
     */
    public static boolean isValidRAR(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.RAR));
    }

    /**
     * 验证文件是否为 7z 压缩文件。
     */
    public static boolean isValid7z(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.SEVEN_Z));
    }

    /**
     * 验证文件是否为 TGZ 压缩文件（检查 GZIP 头）。
     */
    public static boolean isValidTgz(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.GZIP));
    }

    /**
     * 验证文件是否为 MP4 视频。
     */
    public static boolean isValidMp4(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.MP4));
    }

    /**
     * 验证文件是否为 MP3 音频文件（检查 ID3 或帧头）。
     */
    public static boolean isValidMp3(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.MP3));
    }

    // --- 验证特定 ZIP 子类型的方法 ---

    /**
     * 通过检查内部 ZIP 结构验证文件是否为特定的 DOCX 文件。
     * 这也会确认文件为通用 ZIP 格式。
     */
    public static boolean isValidDocx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.DOCX;
    }

    /**
     * 通过检查内部 ZIP 结构验证文件是否为特定的 XLSX 文件。
     * 这也会确认文件为通用 ZIP 格式。
     */
    public static boolean isValidXlsx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.XLSX;
    }

    /**
     * 通过检查内部 ZIP 结构验证文件是否为特定的 PPTX 文件。
     * 这也会确认文件为通用 ZIP 格式。
     */
    public static boolean isValidPptx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.PPTX;
    }

    /**
     * 通过检查内部 ZIP 结构（是否存在 MANIFEST.MF）验证文件是否为特定的 JAR 文件。
     * 这也会确认文件为通用 ZIP 格式。注意：如果内部检查没有优先处理 WAR，则此检查可能会将 WAR 误分类为 JAR。
     */
    public static boolean isValidJar(MultipartFile file) {
        // 首先检查通用 ZIP 格式，然后检查特定子类型
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;

        ZipSubMagic subtype = determineSpecificZipType(file);
        // 如果被分类为 JAR，则返回 true，但 *不是* WAR（因为 WAR 是更具体的 JAR）
        return subtype == ZipSubMagic.JAR; // determineSpecificZipType 处理 WAR 与 JAR 的逻辑
    }

    /**
     * 通过检查内部 ZIP 结构（是否存在 WEB-INF/）验证文件是否为特定的 WAR 文件。
     * 这也会确认文件为通用 ZIP 格式。
     */
    public static boolean isValidWar(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.WAR;
    }

    /**
     * 验证文件是否为特定的 *通用* ZIP 文件（不是 docx、jar 等已识别的子类型）。
     * 这会确认文件为通用 ZIP 格式且未找到特定子类型的指示器。
     */
    public static boolean isValidOnlyGenericZip(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;

        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.ZIP;
    }

    // --- 包含特定 ZIP 子类型的便捷方法 ---

    /**
     * 验证文件是否为特定的 DOCX、XLSX 或 PPTX 文件。
     */
    public static boolean isValidOoxmlDocument(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;
        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.DOCX || subtype == ZipSubMagic.XLSX || subtype == ZipSubMagic.PPTX;
    }

    /**
     * 验证文件是否为特定的 JAR 或 WAR 文件。
     */
    public static boolean isValidJavaArchive(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;
        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.JAR || subtype == ZipSubMagic.WAR;
    }

    /**
     * 验证文件是否是压缩文件 (ZIP 子类型, TAR, RAR, 7z, TGZ).
     */
    public static boolean isValidAnyArchive(MultipartFile file) {
        // Check non-ZIP archive types first
        Set<FileMagic> nonZipArchives = EnumSet.of(FileMagic.TAR, FileMagic.RAR, FileMagic.SEVEN_Z, FileMagic.GZIP);
        if (isValidFileMagic(file, nonZipArchives)) {
            return true;
        }

        // 检查是否为任何有效的基于 ZIP 的类型（通用或特定）
        return isValidGeneralZip(file);
    }



    /**
     * 验证文件是否为任何已识别的特定 ZIP 子类型（JAR、WAR、DOCX、XLSX、PPTX）
     * 或通用 ZIP。本质上是检查文件是否为有效的基于 ZIP 的文件。
     */
    public static boolean isValidAnyZipBasedFile(MultipartFile file) {
        // 对于此目的，仅检查通用 ZIP 魔数即可
        return isValidGeneralZip(file);
    }

    // --- 包含多种类型的便捷方法 ---

    /**
     * 验证文件是否为 PDF、传统 DOC/XLS 或任何特定 ZIP 子类型（DOCX、XLSX、PPTX、JAR、WAR、通用 ZIP）。
     */
    public static boolean isValidDocumentOrSpreadsheetOrArchive(MultipartFile file) {
        // 首先检查非 ZIP 类型
        Set<FileMagic> nonZipTypes = EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.XLS, FileMagic.RAR, FileMagic.SEVEN_Z, FileMagic.GZIP);
        if (isValidFileMagic(file, nonZipTypes)) {
            return true;
        }

        // 如果不是非 ZIP 类型，则检查是否为任何有效的基于 ZIP 的类型
        return isValidGeneralZip(file);
    }


    /**
     * 验证文件是否为任何受支持的文件类型，基于魔数或 ZIP 检查。
     */
    public static boolean isValidSupportedType(MultipartFile file) {
        // Check non-ZIP types (including TAR, RAR, 7z, TGZ, MP4, MP3)
        Set<FileMagic> nonZipTypes = EnumSet.of(
                FileMagic.PDF, FileMagic.DOC, FileMagic.XLS,
                FileMagic.TAR, FileMagic.RAR, FileMagic.SEVEN_Z, FileMagic.GZIP,
                FileMagic.MP4, FileMagic.MP3
        );
        if (isValidFileMagic(file, nonZipTypes)) {
            return true;
        }

        // 检查是否为任何有效的基于 ZIP 的类型（通用或特定）
        return isValidGeneralZip(file);
    }
}
