//package cn.tannn.jdevelops.utils.core.file;
//
//import com.sunway.conference.util.FileUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.web.MockMultipartFile;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class FileTypeValidatorTest {
//
//    @Test
//    public void testPDFFile() throws Exception {
//        byte[] pdfMagicNumber = {(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46};
//        MockMultipartFile pdfFile = new MockMultipartFile("file", "test.pdf", "application/pdf", pdfMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(pdfFile));
//    }
//
//    @Test
//    public void testDOCFile() throws Exception {
//        byte[] docMagicNumber = {(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0};
//        MockMultipartFile docFile = new MockMultipartFile("file", "test.doc", "application/msword", docMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(docFile));
//    }
//
//    @Test
//    public void testDOCXFile() throws Exception {
//        byte[] docxMagicNumber = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
//        MockMultipartFile docxFile = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", docxMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(docxFile));
//    }
//
//    @Test
//    public void testZipFile() throws Exception {
//        byte[] zipMagicNumber = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
//        MockMultipartFile zipFile = new MockMultipartFile("file", "test.zip", "application/zip", zipMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(zipFile));
//    }
//
//    @Test
//    public void testRarFile() throws Exception {
//        byte[] rarMagicNumber = {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21};
//        MockMultipartFile rarFile = new MockMultipartFile("file", "test.rar", "application/x-rar-compressed", rarMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(rarFile));
//    }
//
//    @Test
//    public void test7zFile() throws Exception {
//        byte[] sevenZMagicNumber = {(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF};
//        MockMultipartFile sevenZFile = new MockMultipartFile("file", "test.7z", "application/x-7z-compressed", sevenZMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(sevenZFile));
//    }
//
//    @Test
//    public void testTgzFile() throws Exception {
//        byte[] tgzMagicNumber = {(byte) 0x1F, (byte) 0x8B, (byte) 0x08, (byte) 0x00};
//        MockMultipartFile tgzFile = new MockMultipartFile("file", "test.tgz", "application/gzip", tgzMagicNumber);
//        assertTrue(FileUtil.isValidFileTypeByPDFDOC(tgzFile));
//    }
//
//    @Test
//    public void testInvalidFile() throws Exception {
//        byte[] invalidMagicNumber = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
//        MockMultipartFile invalidFile = new MockMultipartFile("file", "test.invalid", "application/octet-stream", invalidMagicNumber);
//        assertFalse(FileUtil.isValidFileTypeByPDFDOC(invalidFile));
//    }
//}
