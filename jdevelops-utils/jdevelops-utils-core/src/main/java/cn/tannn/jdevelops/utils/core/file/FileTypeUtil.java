package cn.tannn.jdevelops.utils.core.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * 文件类型
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/15 11:59
 */
public class FileTypeUtil {


    /**
     * Reads the initial bytes of a file to determine if its magic number matches any of the allowed types.
     * This is a quick check based on file headers and does not guarantee a well-formed file.
     *
     * @param file The MultipartFile to validate.
     * @param allowedTypes A set of FileType enums representing the allowed file types.
     * @return true if the file's magic number matches any of the allowed types, false otherwise.
     */
    public static boolean isValidFileType(MultipartFile file, Set<FileMagic> allowedTypes) {
        if (file == null || file.isEmpty() || allowedTypes == null || allowedTypes.isEmpty()) {
            // Handle null or empty inputs gracefully
            // LOG.warn("Validation called with null file, empty file, or empty allowed types set.");
            return false;
        }

        // Determine the maximum number of bytes we need to read to cover all allowed magic numbers
        int maxBytesToRead = 0;
        for (FileMagic type : allowedTypes) {
            if (type != null) {
                maxBytesToRead = Math.max(maxBytesToRead, type.getMaxMagicLength());
            }
        }

        // If no valid types were provided, return false
        if (maxBytesToRead == 0) {
            // LOG.warn("Validation called with allowed types set containing only nulls or no valid types.");
            return false;
        }

        // Read the initial bytes from the file
        byte[] fileMagic = new byte[maxBytesToRead];
        try (InputStream fis = file.getInputStream()) {
            int bytesRead = fis.read(fileMagic);

            if (bytesRead == -1) {
                // File is empty
                // LOG.warn("File is empty, no bytes read.");
                return false;
            }

            // Truncate the buffer if less than maxBytesToRead were read
            if (bytesRead < maxBytesToRead) {
                fileMagic = Arrays.copyOf(fileMagic, bytesRead);
            }

        } catch (IOException e) {
            // LOG.error("Error reading file input stream for magic number check", e);
            return false;
        }

        // Check if the read magic number matches any of the allowed types
        for (FileMagic allowedType : allowedTypes) {
            if (allowedType == null) continue; // Skip null entries in the set

            for (byte[] magicPattern : allowedType.getMagicNumbers()) {
                // Ensure we have enough bytes read to match the pattern
                if (fileMagic.length >= magicPattern.length) {
                    boolean matches = true;
                    for (int i = 0; i < magicPattern.length; i++) {
                        if (fileMagic[i] != magicPattern[i]) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        return true; // Found a match
                    }
                }
            }
        }

        // No match found among allowed types
        return false;
    }

    /**
     * Validates if the file is a PDF.
     */
    public static boolean isValidPdf(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF));
    }

    /**
     * Validates if the file is a traditional DOC (Word 97-2003).
     */
    public static boolean isValidDoc(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.DOC));
    }

    /**
     * Validates if the file is a ZIP archive (includes DOCX, XLSX, PPTX, etc.).
     */
    public static boolean isValidZip(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.ZIP));
    }

    /**
     * Validates if the file is a RAR archive (covers common older and newer versions).
     */
    public static boolean isValidRAR(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.RAR, FileMagic.RAR_NEW));
    }

    /**
     * Validates if the file is a 7z archive.
     */
    public static boolean isValid7z(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.SEVEN_Z));
    }

    /**
     * Validates if the file is a TGZ archive (checks for GZIP header).
     */
    public static boolean isValidTgz(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.GZIP));
    }


    /**
     * Validates if the file is a PDF, DOC, or any supported ZIP-based type (ZIP, DOCX, XLSX, PPTX).
     */
    public static boolean isValidPdfDocZip(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.ZIP));
    }

    /**
     * Validates if the file is a PDF, DOC, or any supported archive type (ZIP, RAR, 7z, TGZ).
     * This mirrors the original method's intent more closely, though using GZIP enum for TGZ.
     */
    public static boolean isValidPdfDocArchive(MultipartFile file) {
        return isValidFileType(file, EnumSet.of(FileMagic.PDF, FileMagic.DOC, FileMagic.ZIP, FileMagic.RAR, FileMagic.RAR_NEW, FileMagic.SEVEN_Z, FileMagic.GZIP));
    }

}
