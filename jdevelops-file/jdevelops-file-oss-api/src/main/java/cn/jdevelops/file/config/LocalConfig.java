package cn.jdevelops.file.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * local
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 19:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LocalConfig {

    /**
     * 使用localDriver时，文件上传的存放路径
     *  e.g E:/test/file
     */
    private String uploadDir;


    /**
     * 映射路径 即: 前端相对访问路径前缀
     * e.g /image ： image/contextPath/xx.png(数据库存的/xx.png)
     */
    private String contextPath;

}
