package cn.tannn.jdevelops.utils.core.file;

/**
 * 文件魔数
 *
 * @author tan
 * @version V1.0
 * @date 2025/5/15 11:55
 */
public enum FileMagic {
    // 文档和电子表格
    PDF(new byte[][]{{(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46}}, new int[]{0}), // %PDF
    // OLE 复合文件二进制格式（用于旧版 .doc、.xls、.ppt）
    DOC(new byte[][]{{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}}, new int[]{0}), // D0 CF 11 E0 A1 B1 1A E1
    // XLS（旧版 Excel - 使用与 DOC 相同的魔数）
    XLS(new byte[][]{{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}}, new int[]{0}), // DOC 魔数的别名

    // 压缩文件（以及 OOXML 格式，如 .docx、.xlsx、.pptx、.jar）- 检查通用 ZIP 魔数
    // 特定 ZIP 子类型（JAR、WAR、DOCX、XLSX、PPTX）需要在确认为 ZIP 文件后检查 ZIP 内容。
    ZIP(new byte[][]{{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04}}, new int[]{0}), // PK\003\004 - 所有以下类型的通用魔数 + 通用 ZIP

    // TAR (Tape 压缩文件 - checks for "ustar\0" at offset 257)
    TAR(new byte[][]{
            {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x00}, // ustar\0 (Ustar/POSIX)
            {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x20}  // ustar  (GNU TAR)
    }, new int[]{257, 257}), // Both patterns are checked at offset 257

    // RAR（Roshal 压缩文件）- 覆盖常见的旧版本和新版本
    RAR(new byte[][]{
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x00}, // Rar!\x1a\x07\x00 (RAR v1.5-v2.0)
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x01, (byte) 0x00} // Rar!\x1a\x07\x01\x00 (RAR v5+)
    }, new int[]{0, 0}),
    // 7Z（7z 压缩文件）
    SEVEN_Z(new byte[][]{{(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C}}, new int[]{0}), // 7z\xBC\xAF\x27\x1C
    // TGZ（GZIP 压缩的 TAR 压缩文件 - 魔数是 GZIP 的）
    GZIP(new byte[][]{{(byte) 0x1F, (byte) 0x8B}}, new int[]{0}), // 1F 8B

    // 音频和视频
    // MP4（ISO 基础媒体文件格式 - 检查通常位于偏移量 4 的 'ftyp' 箱头）
    MP4(new byte[][]{{(byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70}}, new int[]{4}), // ftyp 在偏移量 4
    // MP3（MPEG 音频层 3 - 检查开头的 ID3 标签或常见的帧同步字）
    MP3(new byte[][]{
            {(byte) 0x49, (byte) 0x44, (byte) 0x33}, // ID3 标签
            {(byte) 0xFF, (byte) 0xFB}             // 常见的 MPEG 层 3 帧同步字
    }, new int[]{0, 0}); // ID3 和同步字都从偏移量 0 开始检查

    // --- 无法通过魔数可靠验证的文件类型 ---
    // TEXT（.txt、.csv、.log 等）- 没有标准魔数，可以以任何文本开头。
    // SQL（.sql）- 纯文本脚本，没有标准魔数。
    // MD（.md）- Markdown，纯文本，没有标准魔数。

    private final byte[][] magicPatterns;
    private final int[] offsets; // 每个魔数模式的偏移量

    FileMagic(byte[][] magicPatterns, int[] offsets) {
        if (magicPatterns.length != offsets.length) {
            throw new IllegalArgumentException("魔数模式的数量必须与偏移量的数量匹配");
        }
        this.magicPatterns = magicPatterns;
        this.offsets = offsets;
    }

    public byte[][] getMagicPatterns() {
        return magicPatterns;
    }

    public int[] getOffsets() {
        return offsets;
    }

    /**
     * 计算检查此文件类型的所有魔数模式所需的最字节数，考虑偏移量。
     */
    public int getMaxRequiredLength() {
        int maxLen = 0;
        for (int i = 0; i < magicPatterns.length; i++) {
            int requiredLen = offsets[i] + magicPatterns[i].length;
            if (requiredLen > maxLen) {
                maxLen = requiredLen;
            }
        }
        return maxLen;
    }

    /**
     * 计算所有定义的 FileTypes 中执行魔数检查所需的最字节数。
     */
    public static int getMaxOverallRequiredLength() {
        int maxLen = 0;
        for (FileMagic type : values()) {
            int typeMaxLen = type.getMaxRequiredLength();
            if (typeMaxLen > maxLen) {
                maxLen = typeMaxLen;
            }
        }
        return maxLen;
    }
}
