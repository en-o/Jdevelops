package cn.jdevelops.string;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字符串压缩
 * @author 来源于网络
 */
public class ZipStrUtil {

  /**
   * 恢复字符串
   * @param compressedStr 需要解压的字符串
   * @return string （恢复失败返回 compressedStr
   */
  public static String gunzip(String compressedStr){
    if(compressedStr==null){
      return null;
    }
    byte[] compressed;
    try {
        Base64.Decoder decoder = Base64.getDecoder();
        compressed = decoder.decode(compressedStr.getBytes(StandardCharsets.UTF_8));
      try(
              ByteArrayInputStream   in=new ByteArrayInputStream(compressed);
              GZIPInputStream ginzip=new GZIPInputStream(in);
              ByteArrayOutputStream out= new ByteArrayOutputStream();
      ) {
        byte[] buffer = new byte[1024];
        int offset = -1;
        while ((offset = ginzip.read(buffer)) != -1) {
          out.write(buffer, 0, offset);
        }
        return out.toString();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }catch (Exception e){
        e.printStackTrace();
    }
     return  compressedStr;
  }

  /**
   * 压缩字符串
   * @param primStr 需要压缩的字符串
   * @return string (压缩失败返回原字符
   */
  public static String gzip(String primStr) {
    if (primStr == null || primStr.length() == 0) {
      return primStr;
    }
    /*
     * ByteArrayInputStream是内存读写流，不同于指向硬盘的流，
     * 它内部是使用字节数组读内存的，这个字节数组是它的成员变量，
     * 当这个数组不再使用变成垃圾的时候，Java的垃圾回收机制会将它回收。所以不需要关流
     */
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try(
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            ) {
      gzip.write(primStr.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
      return primStr;
    }
      Base64.Encoder encoder = Base64.getEncoder();
      return encoder.encodeToString(out.toByteArray());
  }
}
