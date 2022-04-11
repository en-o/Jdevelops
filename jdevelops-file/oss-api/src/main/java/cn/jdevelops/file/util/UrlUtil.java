package cn.jdevelops.file.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * url
 *
 * @author tn
 * @version 1
 * @date 2022-04-11 13:14
 */
public class UrlUtil {

    /**
     * getContentType
     * 该方式支持本地文件，也支持http/https远程文件
     * @param fileUrl fileUrl
     * @return ContentType
     */
    public static String getContentType(String fileUrl) {
        String contentType = null;
        try {
            contentType = new MimetypesFileTypeMap().getContentType(new File(fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }
}
