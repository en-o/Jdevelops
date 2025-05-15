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
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/15 11:59
 */
public class FileTypeUtil {

    /**
     * Helper method to check if a portion of the file's magic bytes matches a pattern at a specific offset.
     */
    private static boolean matchesMagicAtOffset(byte[] fileMagic, byte[] pattern, int offset) {
        // Need at least enough bytes in fileMagic to cover the pattern at the given offset
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
     * Reads the initial bytes of a file and checks if its magic number(s) match any of the allowed types.
     * This method supports checking magic numbers at different offsets.
     * This is a quick check based on file headers and does not guarantee a well-formed file.
     * This method *does not* differentiate between specific ZIP subtypes (like docx, xlsx, jar, etc.).
     * Use determineSpecificZipType for that after confirming it's a general ZIP.
     *
     * @param file         The MultipartFile to validate.
     * @param allowedTypes A set of FileMagic enums representing the allowed file types.
     * @return true if the file's magic number matches any of the allowed types at the correct offset, false otherwise.
     */
    public static boolean isValidFileMagic(MultipartFile file, Set<FileMagic> allowedTypes) {
        if (file == null || file.isEmpty() || allowedTypes == null || allowedTypes.isEmpty()) {
            // Handle null or empty inputs gracefully
            // LOG.warn("Validation called with null file, empty file, or empty allowed types set.");
            return false;
        }

        // Determine the maximum number of bytes we need to read to cover all allowed magic number checks
        int maxBytesToRead = 0;
        for (FileMagic type : allowedTypes) {
            if (type != null) {
                maxBytesToRead = Math.max(maxBytesToRead, type.getMaxRequiredLength());
            }
        }

        // If no valid types were provided (e.g., set was empty or contained only nulls)
        if (maxBytesToRead == 0) {
            // LOG.warn("Validation called with allowed types set containing only nulls or no valid types requiring bytes.");
            return false;
        }


        // Read the initial bytes from the file
        byte[] fileMagic = new byte[maxBytesToRead];
        int bytesRead;
        try (InputStream fis = file.getInputStream()) {
            bytesRead = fis.read(fileMagic);
            if (bytesRead == -1) {
                // File is empty
                // LOG.warn("File is empty, no bytes read.");
                return false;
            }
            // No need to truncate here, matchesMagicAtOffset handles buffer length implicitly

        } catch (IOException e) {
            // LOG.error("Error reading file input stream for magic number check", e);
            return false;
        }

        // Check if the read magic number matches any of the allowed types at the correct offset
        for (FileMagic allowedType : allowedTypes) {
            if (allowedType == null) continue; // Skip null entries in the set

            byte[][] patterns = allowedType.getMagicPatterns();
            int[] offsets = allowedType.getOffsets();

            for (int i = 0; i < patterns.length; i++) {
                byte[] pattern = patterns[i];
                int offset = offsets[i];

                if (matchesMagicAtOffset(fileMagic, pattern, offset)) {
                    return true; // Found a match
                }
            }
        }

        // No match found among allowed types
        return false;
    }


    /**
     * Checks the file's initial bytes to confirm it's a general ZIP format.
     * This is the first step before determining a specific ZIP subtype.
     */
    public static boolean isGeneralZipFormat(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.ZIP));
    }


    /**
     * Attempts to determine the specific subtype of a ZIP-based file
     * (.zip, .jar, .war, .docx, .xlsx, .pptx) by inspecting its internal structure.
     * This method assumes the file has already been confirmed to be a general ZIP format
     * using isGeneralZipFormat or isValidFileMagic including FileMagic.ZIP.
     *
     * @param file The MultipartFile to analyze.
     * @return The determined ZipSubMagic, or UNKNOWN if analysis fails or no known subtype is found.
     */
    public static ZipSubMagic determineSpecificZipType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            // LOG.warn("determineSpecificZipType called with null or empty file.");
            return ZipSubMagic.UNKNOWN;
        }

        // Optional: Add a check here if it's actually a ZIP first,
        // although the method contract implies it should be.
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
            // Iterate through entries, stop if we find indicators for a specific type
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                // Use the helper from the enum to classify entries
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
                        break; // Found META-INF/MANIFEST.MF
                    case WAR:
                        foundWebInf = true;
                        break;   // Found WEB-INF/
                    case UNKNOWN: // Continue checking other entries
                        break;
                    default:
                        // Should not happen with current ZipSubMagic.determineFromEntry
                        break;
                }

                // Optimization: If we've found indicators for the most specific types,
                // we can potentially stop scanning early.
                if (foundWordDir || foundXlDir || foundPptDir || (foundManifest && foundWebInf)) {
                    break;
                }

                zis.closeEntry(); // Close the current entry before moving to the next
            }

        } catch (IOException e) {
            // LOG.error("Error reading ZIP file entries", e);
            return ZipSubMagic.UNKNOWN; // Error reading implies unknown type
        }

        // Determine the final subtype based on what was found
        return ZipSubMagic.finalizeSubType(foundManifest, foundWebInf, foundWordDir, foundXlDir, foundPptDir);
    }


    // --- Convenience methods for common combinations or single types ---
    // Note: These use EnumSet.of for one or more types, or EnumSet.allOf for all supported types.

    /**
     * Validates if the file is a PDF.
     */
    public static boolean isValidPdf(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.PDF));
    }

    /**
     * Validates if the file is a traditional DOC (Word 97-2003).
     */
    public static boolean isValidDoc(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.DOC));
    }

    /**
     * Validates if the file is a traditional XLS (Excel 97-2003).
     */
    public static boolean isValidXls(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.XLS));
    }

    /**
     * Validates if the file is a general ZIP format (does not differentiate subtypes like docx, jar, etc.).
     */
    public static boolean isValidGeneralZip(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.ZIP));
    }

    /**
     * Validates if the file is a RAR archive (covers common older and newer versions).
     */
    public static boolean isValidRAR(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.RAR));
    }

    /**
     * Validates if the file is a 7z archive.
     */
    public static boolean isValid7z(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.SEVEN_Z));
    }

    /**
     * Validates if the file is a TGZ archive (checks for GZIP header).
     */
    public static boolean isValidTgz(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.GZIP));
    }

    /**
     * Validates if the file is an MP4 video.
     */
    public static boolean isValidMp4(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.MP4));
    }

    /**
     * Validates if the file is an MP3 audio file (checks for ID3 or frame header).
     */
    public static boolean isValidMp3(MultipartFile file) {
        return isValidFileMagic(file, EnumSet.of(FileMagic.MP3));
    }


    // --- Methods for validating specific ZIP subtypes ---

    /**
     * Validates if the file is specifically a DOCX file by checking internal ZIP structure.
     * This also confirms it's a general ZIP format.
     */
    public static boolean isValidDocx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.DOCX;
    }

    /**
     * Validates if the file is specifically an XLSX file by checking internal ZIP structure.
     * This also confirms it's a general ZIP format.
     */
    public static boolean isValidXlsx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.XLSX;
    }

    /**
     * Validates if the file is specifically a PPTX file by checking internal ZIP structure.
     * This also confirms it's a general ZIP format.
     */
    public static boolean isValidPptx(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.PPTX;
    }

    /**
     * Validates if the file is specifically a JAR file by checking internal ZIP structure (presence of MANIFEST.MF).
     * This also confirms it's a general ZIP format. Note: This check might classify WARs as JARs if the WAR check isn't prioritized internally.
     */
    public static boolean isValidJar(MultipartFile file) {
        // Check general ZIP format first, then the specific subtype
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;

        ZipSubMagic subtype = determineSpecificZipType(file);
        // Return true if it's classified as JAR, but *not* WAR (as WAR is a more specific JAR)
        return subtype == ZipSubMagic.JAR; // determineSpecificZipType handles the WAR vs JAR logic
    }

    /**
     * Validates if the file is specifically a WAR file by checking internal ZIP structure (presence of WEB-INF/).
     * This also confirms it's a general ZIP format.
     */
    public static boolean isValidWar(MultipartFile file) {
        return isGeneralZipFormat(file) && determineSpecificZipType(file) == ZipSubMagic.WAR;
    }

    /**
     * Validates if the file is specifically a *generic* ZIP file (not a recognized subtype like docx, jar, etc.).
     * This confirms it's a general ZIP format and no specific subtype indicators were found.
     */
    public static boolean isValidOnlyGenericZip(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;

        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.ZIP;
    }


    // --- Convenience methods for combining specific ZIP subtypes ---

    /**
     * Validates if the file is specifically a DOCX, XLSX, or PPTX file.
     */
    public static boolean isValidOoxmlDocument(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;
        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.DOCX || subtype == ZipSubMagic.XLSX || subtype == ZipSubMagic.PPTX;
    }

    /**
     * Validates if the file is specifically a JAR or WAR file.
     */
    public static boolean isValidJavaArchive(MultipartFile file) {
        boolean isZip = isGeneralZipFormat(file);
        if (!isZip) return false;
        ZipSubMagic subtype = determineSpecificZipType(file);
        return subtype == ZipSubMagic.JAR || subtype == ZipSubMagic.WAR;
    }

    /**
     * Validates if the file is any recognized specific ZIP subtype (JAR, WAR, DOCX, XLSX, PPTX)
     * or a generic ZIP. Essentially checks if it's a valid ZIP-based file.
     */
    public static boolean isValidAnyZipBasedFile(MultipartFile file) {
        // Simply checking the general ZIP magic is sufficient for this purpose
        return isValidGeneralZip(file);
    }


    // --- Convenience methods including a mix of types ---

    /**
     * Validates if the file is a PDF, traditional DOC/XLS, or any specific ZIP subtype (DOCX, XLSX, PPTX, JAR, WAR, Generic ZIP).
     */
    public static boolean isValidDocumentOrSpreadsheetOrArchive(MultipartFile file) {
        // Check non-ZIP types first
        Set<FileMagic> nonZipTypes = EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.XLS, FileMagic.RAR, FileMagic.SEVEN_Z, FileMagic.GZIP);
        if (isValidFileMagic(file, nonZipTypes)) {
            return true;
        }

        // If not a non-ZIP type, check if it's any valid ZIP-based type
        return isValidGeneralZip(file);
    }


    /**
     * Validates if the file is any of the supported file types based on magic number or ZIP inspection.
     */
    public static boolean isValidSupportedType(MultipartFile file) {
        // Check non-ZIP types
        Set<FileMagic> nonZipTypes = EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.XLS, FileMagic.RAR, FileMagic.SEVEN_Z, FileMagic.GZIP, FileMagic.MP4, FileMagic.MP3);
        if (isValidFileMagic(file, nonZipTypes)) {
            return true;
        }

        // Check if it's any valid ZIP-based type (generic or specific)
        return isValidGeneralZip(file);
    }
}
