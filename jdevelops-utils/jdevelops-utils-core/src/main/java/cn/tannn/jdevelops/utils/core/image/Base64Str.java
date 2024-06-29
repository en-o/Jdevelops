package cn.tannn.jdevelops.utils.core.image;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Base64有关的工具类
 *
 * @author tn
 * @version 1
 * @date 2021-11-10 22:37
 */
public class Base64Str {

    /**
     * 本地图片转换成base64字符串
     * @param imgFilePath    图片本地路径
     * @return  Base64
     */
    public static String imageToBase64(String imgFilePath) throws IOException {
        File file = FileUtil.file(imgFilePath);
        BufferedImage bufferedImage = ImageIO.read(FileUtil.file(imgFilePath));
        //  Image scalerRImage = ImgUtil.scale(rRead, 0.5f); // 压缩下
        return ImgUtil.toBase64(bufferedImage, FilenameUtils.getExtension(file.getName()));
    }

    /**
     *  本地图片转换成base64字符串  - 带前缀可直接访问
     * @param imgFilePath 图片本地路径
     * @return Base64
     * @throws IOException IOException
     */
    public static String imageToBase64DataUri(String imgFilePath) throws IOException {
        File file = FileUtil.file(imgFilePath);
        BufferedImage bufferedImage = ImageIO.read(FileUtil.file(imgFilePath));
        return ImgUtil.toBase64DataUri(bufferedImage, FilenameUtils.getExtension(file.getName()));
    }

    /**
     * MultipartFile 转换成base64字符串
     * @param multipartImageFile    图片本地路径
     * @throws  IOException IOException
     * @return Base64
     */
    public static String imageToBase64(MultipartFile multipartImageFile) throws IOException {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        BufferedImage rRead = ImageIO.read(multipartImageFile.getInputStream());
        return ImgUtil.toBase64(rRead, FilenameUtils.getExtension(multipartImageFile.getName()));
    }

    /**
     * MultipartFile 转换成base64字符串 - 带前缀可直接访问
     * @param multipartImageFile multipartImageFile
     * @return Base64
     * @throws IOException IOException
     */
    public static String imageToBase64DataUri(MultipartFile multipartImageFile) throws IOException {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        BufferedImage rRead = ImageIO.read(multipartImageFile.getInputStream());
        return ImgUtil.toBase64DataUri(rRead, FilenameUtils.getExtension(multipartImageFile.getName()));
    }
}
