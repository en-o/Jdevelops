package cn.jdevelops.file.util;

import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.Direction;
import com.freewayso.image.combiner.enums.OutputFormat;
import javafx.scene.transform.Rotate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 水印
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-06-07 17:26
 */
public class ImageWatermarkUtil {

    private static Color color = Color.red;
    //透明度（0.0~1.0）
    private static float alpha = .4f;
    // 旋转
    private static int rotate = 45;


    /**
     * 单个文字水印 - 居中
     * @param watermarkStr 水印文字
     * @param image url 或者文件路径  （http://server-port/imag.png or e://image.png）
     * @return ImageCombiner
     * @throws Exception Exception
     */
    public static ImageCombiner singeWordWatermark(String watermarkStr, String image) throws Exception {
        ImageCombiner combiner = getImageCombiner(image);
        int fontSize = getFontSize(combiner.getCanvasWidth(),0.05);
        Font font = new Font("宋体", Font.PLAIN, fontSize);
        combiner.addTextElement(watermarkStr, font,0,
                        combiner.getCanvasHeight()/2-fontSize)
                //居中绘制（会忽略x坐标，改为自动计算
                .setCenter(true)
                .setColor(color)
                .setRotate(rotate)
                .setAlpha(alpha);
        //执行图片合并
        combiner.combine();
        // 可以获取流（并上传oss等）
        // InputStream is = combiner.getCombinedImageStream();
        //也可以保存到本地
        // combiner.save("E://测试图片//image2.png");
        return combiner;
    }



    /**
     * 满屏文字水印
     * @param watermarkStr 水印文字
     * @param image url 或者文件路径  （http://server-port/imag.png or e://image.png）
     * @return ImageCombiner
     * @throws Exception Exception
     */
    public static ImageCombiner fullWordWatermark(String watermarkStr, String image) throws Exception {
        ImageCombiner combiner = getImageCombiner(image);
        // 原图宽度
        int srcImgWidth = combiner.getCanvasWidth();
        int srcImgHeight = combiner.getCanvasWidth();
        int fontSize = getFontSize(srcImgWidth,0.01);
        Font font = new Font("宋体", Font.PLAIN, fontSize);
        for (int w = fontSize; w < srcImgWidth; w += w) {
            for (int h = fontSize; h < srcImgHeight; h += h) {
                combiner.addTextElement(watermarkStr,font,w+fontSize, h+fontSize)
                        .setColor(color)
                        .setRotate(rotate)
                        .setAlpha(alpha);
            }
        }
        //执行图片合并
        combiner.combine();
        // 可以获取流（并上传oss等）
        // InputStream is = combiner.getCombinedImageStream();
        //也可以保存到本地
        // combiner.save("E://测试图片//image2.png");
        return combiner;
    }


    /**
     * @param image url 或者文件路径  （http://server-port/imag.png or e://image.png）
     * @return
     */
    private static ImageCombiner getImageCombiner(String image) throws Exception {
        ImageCombiner combiner;
        if(UrlUtil.isHttpUrl(image)){
            combiner = new ImageCombiner(image, OutputFormat.PNG);
        }else {
            BufferedImage imageBuf = ImageIO.read(Files.newInputStream(Paths.get(image)));
            combiner = new ImageCombiner(imageBuf, OutputFormat.PNG);
        }
        return combiner;
    }

    public static int getFontSize(int srcImgWidth,double ratio){
        int fontSize;
        if (srcImgWidth <= 3000) {
            fontSize = 25;
        } else {
            fontSize = (int) (srcImgWidth * ratio);
        }
        return fontSize;
    }

}
