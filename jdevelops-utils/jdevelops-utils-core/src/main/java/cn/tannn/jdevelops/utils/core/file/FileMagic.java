package cn.tannn.jdevelops.utils.core.file;

/**
 * 文件魔数
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/15 11:55
 */
public enum FileMagic {
    // Documents and Spreadsheets
    PDF(new byte[][]{{(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46}}, new int[]{0}), // %PDF
    // OLE Compound File Binary Format (used by old .doc, .xls, .ppt)
    DOC(new byte[][]{{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}}, new int[]{0}), // D0 CF 11 E0 A1 B1 1A E1
    // XLS (Old Excel - uses the same magic number as DOC)
    XLS(new byte[][]{{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}}, new int[]{0}), // Alias for DOC magic

    // Archives (and OOXML formats like .docx, .xlsx, .pptx, .jar) - Checked for general ZIP magic
    // Specific ZIP subtypes (JAR, WAR, DOCX, XLSX, PPTX) require inspecting ZIP contents *after*
    // this magic number check confirms it's a ZIP file.
    ZIP(new byte[][]{{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04}}, new int[]{0}), // PK\003\004 - Base magic for all types below + generic ZIP

    // RAR (Roshal Archive) - Covers common older and newer versions
    RAR(new byte[][]{
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x00}, // Rar!\x1a\x07\x00 (RAR v1.5-v2.0)
            {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x01, (byte) 0x00} // Rar!\x1a\x07\x01\x00 (RAR v5+)
    }, new int[]{0, 0}),
    // 7Z (7z Archive)
    SEVEN_Z(new byte[][]{{(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C}}, new int[]{0}), // 7z\xBC\xAF\x27\x1C
    // TGZ (GZIP compressed TAR archive - the magic number is for GZIP)
    GZIP(new byte[][]{{(byte) 0x1F, (byte) 0x8B}}, new int[]{0}), // 1F 8B

    // Audio and Video
    // MP4 (ISO Base Media File Format - checks for 'ftyp' box header usually at offset 4)
    MP4(new byte[][]{{(byte) 0x66, (byte) 0x74, (byte) 0x79, (byte) 0x70}}, new int[]{4}), // ftyp at offset 4
    // MP3 (MPEG Audio Layer 3 - checks for ID3 tag or common frame sync word at the start)
    MP3(new byte[][]{
            {(byte) 0x49, (byte) 0x44, (byte) 0x33}, // ID3 tag
            {(byte) 0xFF, (byte) 0xFB}             // Common MPEG Layer 3 frame sync word
    }, new int[]{0, 0}); // Both ID3 and sync word are checked from offset 0

    // --- File types that cannot be reliably validated by magic number ---
    // TEXT (.txt, .csv, .log, etc.) - No standard magic number, can start with any text.
    // SQL (.sql) - Plain text scripts, no standard magic number.
    // MD (.md) - Markdown, plain text, no standard magic number.

    private final byte[][] magicPatterns;
    private final int[] offsets; // Offset for each magic pattern

    FileMagic(byte[][] magicPatterns, int[] offsets) {
        if (magicPatterns.length != offsets.length) {
            throw new IllegalArgumentException("Number of magic patterns must match number of offsets");
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
     * Calculates the maximum number of bytes required to read
     * to check all magic patterns for this file type, considering offsets.
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
     * Calculates the maximum number of bytes required to read
     * across *all* defined FileTypes to perform their magic number checks.
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
