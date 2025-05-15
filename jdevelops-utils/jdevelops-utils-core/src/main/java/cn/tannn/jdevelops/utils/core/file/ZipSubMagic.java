package cn.tannn.jdevelops.utils.core.file;

/**
 * zip 魔数
 */
public enum ZipSubMagic {
    UNKNOWN, // 无法识别的特定 ZIP 类型（可能是普通 ZIP 或其他类型）
    ZIP,     // 通用 ZIP 压缩文件
    JAR,     // Java 压缩文件
    WAR,     // Web 应用程序压缩文件
    DOCX,    // Word 文档（OOXML）
    XLSX,    // Excel 电子表格（OOXML）
    PPTX;    // PowerPoint 演示文稿（OOXML）

    // 辅助方法，用于检查找到的条目名称是否指示此子类型
    public static ZipSubMagic determineFromEntry(String entryName) {
        String lowerEntryName = entryName.toLowerCase(); // 使用小写进行一致的检查

        // OOXML 格式具有特定的根目录
        if (lowerEntryName.startsWith("word/")) return DOCX;
        if (lowerEntryName.startsWith("xl/")) return XLSX;
        if (lowerEntryName.startsWith("ppt/")) return PPTX;

        // 检查 Java 压缩文件标记
        if (lowerEntryName.equals("meta-inf/manifest.mf")) return JAR; // 清单文件
        if (lowerEntryName.startsWith("web-inf/")) return WAR; // WEB-INF 目录表明是 WAR

        return UNKNOWN; // 不匹配特定子类型标记
    }

    // 辅助方法，根据标志确定最终特定类型
    public static ZipSubMagic finalizeSubType(boolean foundManifest, boolean foundWebInf, boolean foundWordDir, boolean foundXlDir, boolean foundPptDir) {
        if (foundWordDir) return DOCX;
        if (foundXlDir) return XLSX;
        if (foundPptDir) return PPTX;
        if (foundManifest && foundWebInf) return WAR; // WAR 比 JAR 更具体
        if (foundManifest) return JAR;             // 如果找到清单但没有 WEB-INF，则为 JAR

        // 如果没有找到特定的指示器，则为通用 ZIP（或为空/格式错误）
        return ZIP;
    }
}
