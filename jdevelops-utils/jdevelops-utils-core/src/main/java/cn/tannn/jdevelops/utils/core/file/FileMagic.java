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

    // XLS（旧版 Excel - 使用与 DOC 相同的魔数，但也可能有特定的Excel标识）
    XLS(new byte[][]{
            {(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}, // 标准OLE复合文档
            {(byte) 0x09, (byte) 0x08, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00}, // Excel 5.0/95
            {(byte) 0x09, (byte) 0x08, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00}  // Excel 4.0
    }, new int[]{0, 0, 0}),

    // 压缩文件（以及 OOXML 格式，如 .docx、.xlsx、.pptx、.jar）- 检查通用 ZIP 魔数
    // 特定 ZIP 子类型（JAR、WAR、DOCX、XLSX、PPTX）需要在确认为 ZIP 文件后检查 ZIP 内容。
    ZIP(new byte[][]{
            {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04}, // PK\003\004 - 标准ZIP
            {(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06}, // PK\005\006 - 空ZIP文件
            {(byte) 0x50, (byte) 0x4B, (byte) 0x07, (byte) 0x08}  // PK\007\008 - 跨卷ZIP文件
    }, new int[]{0, 0, 0}),

    // TAR (Tape 压缩文件 - checks for "ustar" signatures at offset 257)
    TAR(new byte[][]{
            {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x00}, // ustar\0 (POSIX)
            {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x20, (byte) 0x20, (byte) 0x00}, // ustar  \0 (GNU TAR)
            {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72}  // ustar (简化检查，某些变体)
    }, new int[]{257, 257, 257}),

    // RAR（Roshal 压缩文件）- 覆盖常见的旧版本和新版本
    RAR(new byte[][]{
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x00}, // Rar!\x1a\x07\x00 (RAR v1.5-v4.x)
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x01, (byte) 0x00} // Rar!\x1a\x07\x01\x00 (RAR v5+)
    }, new int[]{0, 0}),

    // 7Z（7z 压缩文件）
    SEVEN_Z(new byte[][]{{(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C}}, new int[]{0}), // 7z\xBC\xAF\x27\x1C

    // GZIP（GZIP 压缩文件，包括 TGZ）
    GZIP(new byte[][]{
            {(byte) 0x1F, (byte) 0x8B, (byte) 0x08}, // 标准GZIP，deflate压缩
            {(byte) 0x1F, (byte) 0x8B}               // 通用GZIP头（更宽松的检查）
    }, new int[]{0, 0}),

    // 音频和视频
    // MP4（ISO 基础媒体文件格式）- 多种可能的ftyp变体
    MP4(new byte[][]{
            {(byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70}, // ftyp 在偏移量 4
            {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x18, (byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70}, // 完整的ftyp box头
            {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70}  // 另一种常见的ftyp box头
    }, new int[]{4, 0, 0}),

    // MP3（MPEG 音频层 3）- 包含更多变体
    MP3(new byte[][]{
            {(byte) 0x49, (byte) 0x44, (byte) 0x33}, // ID3v2 标签
            {(byte) 0xFF, (byte) 0xFB},             // MPEG Layer 3, 44.1kHz
            {(byte) 0xFF, (byte) 0xF3},             // MPEG Layer 3, 44.1kHz (另一种变体)
            {(byte) 0xFF, (byte) 0xF2},             // MPEG Layer 3, 22.05kHz
            {(byte) 0xFF, (byte) 0xFA},             // MPEG Layer 3 (变体)
            {(byte) 0x54, (byte) 0x41, (byte) 0x47}  // TAG (ID3v1标签在文件末尾，但某些文件可能从此开始)
    }, new int[]{0, 0, 0, 0, 0, 0}),

    // 图像格式 - 添加常用的图像格式支持
    JPEG(new byte[][]{
            {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0}, // JFIF
            {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1}, // EXIF
            {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE8}  // SPIFF
    }, new int[]{0, 0, 0}),

    PNG(new byte[][]{{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A}}, new int[]{0}),

    GIF(new byte[][]{
            {(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38, (byte) 0x37, (byte) 0x61}, // GIF87a
            {(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38, (byte) 0x39, (byte) 0x61}  // GIF89a
    }, new int[]{0, 0}),

    // 可执行文件格式
    EXE(new byte[][]{
            {(byte) 0x4D, (byte) 0x5A}, // MZ - DOS/Windows可执行文件
            {(byte) 0x5A, (byte) 0x4D}  // ZM - DOS可执行文件的变体
    }, new int[]{0, 0}),

    // 其他有用的格式
    BMP(new byte[][]{{(byte) 0x42, (byte) 0x4D}}, new int[]{0}), // BM - Windows位图

    ICO(new byte[][]{{(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00}}, new int[]{0}), // Windows图标

    // 数据库文件
    SQLITE(new byte[][]{{(byte) 0x53, (byte) 0x51, (byte) 0x4C, (byte) 0x69, (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x6D, (byte) 0x61, (byte) 0x74, (byte) 0x20, (byte) 0x33, (byte) 0x00}}, new int[]{0}); // SQLite format 3

    // --- 无法通过魔数可靠验证的文件类型 ---
    // TEXT（.txt、.csv、.log 等）- 没有标准魔数，可以以任何文本开头。
    // SQL（.sql）- 纯文本脚本，没有标准魔数。
    // MD（.md）- Markdown，纯文本，没有标准魔数。
    // JSON（.json）- 纯文本，没有标准魔数。
    // XML（.xml）- 纯文本，可能以<?xml开头但不是严格要求。

    private final byte[][] magicPatterns;
    private final int[] offsets; // 每个魔数模式的偏移量

    FileMagic(byte[][] magicPatterns, int[] offsets) {
        if (magicPatterns == null || offsets == null) {
            throw new IllegalArgumentException("魔数模式和偏移量不能为null");
        }
        if (magicPatterns.length != offsets.length) {
            throw new IllegalArgumentException("魔数模式的数量必须与偏移量的数量匹配");
        }

        // 验证没有null的魔数模式
        for (int i = 0; i < magicPatterns.length; i++) {
            if (magicPatterns[i] == null) {
                throw new IllegalArgumentException("魔数模式不能为null，索引: " + i);
            }
            if (magicPatterns[i].length == 0) {
                throw new IllegalArgumentException("魔数模式不能为空，索引: " + i);
            }
            if (offsets[i] < 0) {
                throw new IllegalArgumentException("偏移量不能为负数，索引: " + i + ", 值: " + offsets[i]);
            }
        }

        // 创建防御性副本
        this.magicPatterns = new byte[magicPatterns.length][];
        for (int i = 0; i < magicPatterns.length; i++) {
            this.magicPatterns[i] = magicPatterns[i].clone();
        }
        this.offsets = offsets.clone();
    }

    /**
     * 获取魔数模式的防御性副本
     */
    public byte[][] getMagicPatterns() {
        byte[][] copy = new byte[magicPatterns.length][];
        for (int i = 0; i < magicPatterns.length; i++) {
            copy[i] = magicPatterns[i].clone();
        }
        return copy;
    }

    /**
     * 获取偏移量数组的防御性副本
     */
    public int[] getOffsets() {
        return offsets.clone();
    }

    /**
     * 计算检查此文件类型的所有魔数模式所需的最大字节数，考虑偏移量。
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
     * 计算所有定义的 FileTypes 中执行魔数检查所需的最大字节数。
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

    /**
     * 获取魔数模式的数量
     */
    public int getPatternCount() {
        return magicPatterns.length;
    }

    /**
     * 检查是否包含指定索引的模式
     */
    public boolean hasPattern(int index) {
        return index >= 0 && index < magicPatterns.length;
    }

    /**
     * 获取指定索引的魔数模式（防御性副本）
     */
    public byte[] getPattern(int index) {
        if (!hasPattern(index)) {
            throw new IndexOutOfBoundsException("模式索引超出范围: " + index);
        }
        return magicPatterns[index].clone();
    }

    /**
     * 获取指定索引的偏移量
     */
    public int getOffset(int index) {
        if (!hasPattern(index)) {
            throw new IndexOutOfBoundsException("偏移量索引超出范围: " + index);
        }
        return offsets[index];
    }

    /**
     * 获取该文件类型的描述信息
     */
    public String getDescription() {
        switch (this) {
            case PDF: return "Adobe Portable Document Format";
            case DOC: return "Microsoft Word 97-2003 Document";
            case XLS: return "Microsoft Excel 97-2003 Spreadsheet";
            case ZIP: return "ZIP Archive";
            case TAR: return "TAR Archive";
            case RAR: return "RAR Archive";
            case SEVEN_Z: return "7-Zip Archive";
            case GZIP: return "GZIP Compressed File";
            case MP4: return "MPEG-4 Video";
            case MP3: return "MPEG Audio Layer 3";
            case JPEG: return "JPEG Image";
            case PNG: return "Portable Network Graphics";
            case GIF: return "Graphics Interchange Format";
            case EXE: return "Windows Executable";
            case BMP: return "Windows Bitmap";
            case ICO: return "Windows Icon";
            case SQLITE: return "SQLite Database";
            default: return "Unknown File Type";
        }
    }

    /**
     * 获取该文件类型的常见文件扩展名
     */
    public String[] getCommonExtensions() {
        return switch (this) {
            case PDF -> new String[]{".pdf"};
            case DOC -> new String[]{".doc"};
            case XLS -> new String[]{".xls"};
            case ZIP -> new String[]{".zip", ".jar", ".war", ".docx", ".xlsx", ".pptx"};
            case TAR -> new String[]{".tar"};
            case RAR -> new String[]{".rar"};
            case SEVEN_Z -> new String[]{".7z"};
            case GZIP -> new String[]{".gz", ".tgz"};
            case MP4 -> new String[]{".mp4", ".m4v"};
            case MP3 -> new String[]{".mp3"};
            case JPEG -> new String[]{".jpg", ".jpeg"};
            case PNG -> new String[]{".png"};
            case GIF -> new String[]{".gif"};
            case EXE -> new String[]{".exe", ".com"};
            case BMP -> new String[]{".bmp"};
            case ICO -> new String[]{".ico"};
            case SQLITE -> new String[]{".db", ".sqlite", ".sqlite3"};
        };
    }

}
