package cn.tannn.jdevelops.files.sdk.util;

import cn.tannn.cat.file.sdk.exception.FileException;
import cn.tannn.cat.file.sdk.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/9/30 上午9:33
 */
public class FileFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FileFilter.class);
    private static final int READ_SIZE = 8192;
    private static final Map<String, String> MAGIC_NUMBERS = new LinkedHashMap<>();


    static {
        // 图像文件
        MAGIC_NUMBERS.put("FFD8FF", "jpeg");
        MAGIC_NUMBERS.put("89504E470D0A1A0A", "png");
        MAGIC_NUMBERS.put("47494638", "gif");
        MAGIC_NUMBERS.put("424D", "bmp");
        MAGIC_NUMBERS.put("00000100", "ico");

        // 文档文件
        MAGIC_NUMBERS.put("25504446", "pdf");
        MAGIC_NUMBERS.put("D0CF11E0A1B11AE1", "doc");
        MAGIC_NUMBERS.put("504B0304140006000800", "docx");
        MAGIC_NUMBERS.put("504B0304140008000800", "xlsx");
        MAGIC_NUMBERS.put("504B030414000600", "pptx");

        // 压缩文件
        MAGIC_NUMBERS.put("504B0304", "zip");
        MAGIC_NUMBERS.put("526172211A07", "rar");
        MAGIC_NUMBERS.put("1F8B08", "gz");

        // 音频文件
        MAGIC_NUMBERS.put("494433", "mp3");
        MAGIC_NUMBERS.put("52494646", "wav");
        MAGIC_NUMBERS.put("4F676753", "ogg");

        // 视频文件
        MAGIC_NUMBERS.put("000000146674797069736F6D", "mp4");
        MAGIC_NUMBERS.put("1A45DFA3", "mkv");

        // 其他文件类型
        MAGIC_NUMBERS.put("7B5C727466", "rtf");
        MAGIC_NUMBERS.put("7F454C46", "elf");
        MAGIC_NUMBERS.put("CAFEBABE", "class");
    }

    /**
     * 增加文件类型映射<br>
     * 如果已经存在将覆盖之前的映射
     *
     * @param fileStreamHexHead 文件流头部Hex信息
     * @param extName           文件扩展名
     * @return 之前已经存在的文件扩展名
     */
    public static String putFileType(String fileStreamHexHead, String extName) {
        return MAGIC_NUMBERS.put(fileStreamHexHead, extName);
    }

    /**
     * 验证文件格式是否在白名单里
     *
     * @param file        MultipartFile
     * @param whiteSuffix 文件后缀白名单(没有. )
     * @throws IOException FileException
     */
    public static void isValidFileTypeThrow(MultipartFile file, List<String> whiteSuffix) throws IOException {
        if (!isValidFileType(file, whiteSuffix)) {
            throw FileException.specialMessage("文件格式不合法");
        }
    }

    /**
     * 验证文件格式是否在白名单里
     *
     * @param file        MultipartFile
     * @param whiteSuffix 文件后缀白名单(没有. )
     * @return true 在
     * @throws IOException IOException
     */
    public static boolean isValidFileType(MultipartFile file, List<String> whiteSuffix) throws IOException {
        String fileType = getType(file.getInputStream());
        if (whiteSuffix == null || whiteSuffix.isEmpty()) {
            return true;
        }
        for (String type : whiteSuffix) {
            if (type.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证文件格式是否是指定格式
     *
     * @param file       MultipartFile
     * @param fileSuffix 文件后缀(没有. )
     * @return  true 在
     * @throws IOException IOException
     */
    public static boolean isValidFileType(MultipartFile file, String fileSuffix) throws IOException {
        String fileType = getType(file.getInputStream(), file.getName());
        LOG.info("isValidFileType:{}", fileType);
        if (fileSuffix.equalsIgnoreCase(fileType)) {
            return true;
        }
        return false;
    }


    /**
     * 根据文件流的头部信息获得文件类型<br>
     * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * @param in       {@link InputStream}
     * @param fileName 文件名
     * @return 类型，文件的扩展名，提供的in为{@code null}或未找到为{@code null}
     * @throws IOException 读取流引起的异常
     */
    public static String getType(InputStream in, String fileName) throws IOException {
        if (null == in) {
            return null;
        }
        String typeName = getType(in);
        if (null == typeName) {
            // 未成功识别类型，扩展名辅助识别
            typeName = FileUtils.getExt(fileName).replace(".", "");
        }
        return typeName;
    }


    /**
     * 根据文件流的头部信息获得文件类型<br>
     * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * @param in       {@link InputStream}
     * @throws IOException 读取流引起的异常
     */
    public static String getType(InputStream in) throws IOException {
        try {
            byte[] magicNumber = getMagicNumber(in);
            String hexMagicNumber = bytesToHex(magicNumber);
            // 检查各种文件类型的魔数
            for (Map.Entry<String, String> entry : MAGIC_NUMBERS.entrySet()) {
                if (hexMagicNumber.startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
        } catch (Exception e) {
            LOG.error("Error parsing file magic", e);
        }
        return null;
    }

    /**
     * 获取魔数 byte
     */
    private static byte[] getMagicNumber(InputStream fis) throws IOException {
        byte[] buffer = new byte[READ_SIZE];
        int bytesRead = fis.read(buffer, 0, READ_SIZE);
        return Arrays.copyOf(buffer, bytesRead);
    }

    /**
     * 验证魔数 byte = map key
     */
    private static boolean startsWithBytes(byte[] source, byte[] match) {
        return source.length >= match.length && Arrays.equals(Arrays.copyOfRange(source, 0, match.length), match);
    }

    /**
     * byte[] 转 hex
     * @param bytes bytes
     * @return String
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}
