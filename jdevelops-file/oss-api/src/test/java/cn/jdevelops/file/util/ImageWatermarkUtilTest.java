package cn.jdevelops.file.util;

import junit.framework.TestCase;

public class ImageWatermarkUtilTest extends TestCase {

    public void testSingeWordWatermark() throws Exception {
        ImageWatermarkUtil.singeWordWatermark("测试URL-singe","https://oss.databstech.com/detabes/image/5BF7AE51-7717-4852-A9BF-4A41338F5245.png")
                .save("E://测试图片//测试URL-singe.png");
        ImageWatermarkUtil.singeWordWatermark("测试文件地址-singe","E:\\测试图片\\微信图片_20201114155024.png")
                .save("E://测试图片//测试文件地址-singe.png");
    }


    public void testFullWordWatermark() throws Exception {
        ImageWatermarkUtil.fullWordWatermark("测试URL-full","https://oss.databstech.com/detabes/image/5BF7AE51-7717-4852-A9BF-4A41338F5245.png")
                .save("E://测试图片//测试URL-full.png");
        ImageWatermarkUtil.fullWordWatermark("测试文件地址-full","E:\\测试图片\\微信图片_20201114155024.png")
                .save("E://测试图片//测试文件地址--full.png");
    }
}
