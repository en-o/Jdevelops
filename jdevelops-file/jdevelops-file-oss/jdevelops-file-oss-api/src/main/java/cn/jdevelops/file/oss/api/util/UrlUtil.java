package cn.jdevelops.file.oss.api.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 判断字符串是否为URL
     * @param urls 需要判断的String类型url
     * @return true:是URL；false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


}
