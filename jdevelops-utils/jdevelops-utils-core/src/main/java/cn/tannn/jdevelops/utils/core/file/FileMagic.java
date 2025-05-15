package cn.tannn.jdevelops.utils.core.file;

/**
 * 文件魔数
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/15 11:55
 */
public enum FileMagic {
    // PDF
    PDF(new byte[][]{{(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46}}), // %PDF
    // DOC (Old Microsoft Compound File Binary Format - used by .doc, .xls, .ppt)
    DOC(new byte[][]{{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}}), // D0 CF 11 E0 A1 B1 1A E1
    // ZIP (PKWARE Zip - used by .zip, .docx, .xlsx, .pptx, .jar, .war, etc.)
    ZIP(new byte[][]{{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04}}), // PK\003\004
    // RAR (Roshal Archive)
    RAR(new byte[][]{{(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x00}}), // Rar! followed by 0x1A 0x07 0x00 or 0x1A 0x07 0x01 0x00 for newer
    // 注意：为了简单起见，使用旧的RAR魔法，新版本的头文件略有不同
    // 对于更健壮的检查，您可能需要读取更多字节或使用库。
    RAR_NEW(new byte[][]{{(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x01, (byte) 0x00}}),
    // 7Z (7z Archive)
    SEVEN_Z(new byte[][]{{(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C}}), // 7z\xBC\xAF\x27\x1C
    // TGZ (GZIP compressed TAR archive - the magic number is for GZIP)
    GZIP(new byte[][]{{(byte) 0x1F, (byte) 0x8B}}); // 1F 8B

    private final byte[][] magicNumbers;

    FileMagic(byte[][] magicNumbers) {
        this.magicNumbers = magicNumbers;
    }

    public byte[][] getMagicNumbers() {
        return magicNumbers;
    }

    // 获取此类型魔数的最大长度
    public int getMaxMagicLength() {
        int maxLen = 0;
        for (byte[] magic : magicNumbers) {
            if (magic.length > maxLen) {
                maxLen = magic.length;
            }
        }
        return maxLen;
    }

    // 获取所有类型魔数的最大长度
    public static int getMaxOverallMagicLength() {
        int maxLen = 0;
        for (FileMagic type : values()) {
            int typeMaxLen = type.getMaxMagicLength();
            if (typeMaxLen > maxLen) {
                maxLen = typeMaxLen;
            }
        }
        return maxLen;
    }
}
