package cn.tannn.jdevelops.utils.core.file;

/**
 * zip 魔数
 */
public enum ZipSubMagic {
        UNKNOWN, // Not a recognized specific ZIP type (could be a generic zip or something else)
        ZIP,     // Generic ZIP archive
        JAR,     // Java Archive
        WAR,     // Web Application Archive
        DOCX,    // Word Document (OOXML)
        XLSX,    // Excel Spreadsheet (OOXML)
        PPTX;    // PowerPoint Presentation (OOXML)

        // Helper to check if a found entry name indicates this subtype
        public static ZipSubMagic determineFromEntry(String entryName) {
            String lowerEntryName = entryName.toLowerCase(); // Use lower case for consistent checks

            // OOXML formats have specific root directories
            if (lowerEntryName.startsWith("word/")) return DOCX;
            if (lowerEntryName.startsWith("xl/")) return XLSX;
            if (lowerEntryName.startsWith("ppt/")) return PPTX;

            // Check for Java archive markers
            if (lowerEntryName.equals("meta-inf/manifest.mf")) return JAR; // Manifest file
            if (lowerEntryName.startsWith("web-inf/")) return WAR; // WEB-INF directory indicates WAR

            return UNKNOWN; // Doesn't match a specific subtype marker
        }

        // Helper to determine the final specific type based on flags
        public static ZipSubMagic finalizeSubType(boolean foundManifest, boolean foundWebInf, boolean foundWordDir, boolean foundXlDir, boolean foundPptDir) {
            if (foundWordDir) return DOCX;
            if (foundXlDir) return XLSX;
            if (foundPptDir) return PPTX;
            if (foundManifest && foundWebInf) return WAR; // WAR is more specific than JAR
            if (foundManifest) return JAR;             // JAR if manifest found but not WEB-INF

            // If none of the specific indicators are found, it's a generic ZIP (or empty/malformed)
            return ZIP;
        }
    }
