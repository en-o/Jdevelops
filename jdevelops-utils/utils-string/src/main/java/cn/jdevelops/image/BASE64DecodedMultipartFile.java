package cn.jdevelops.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

/**
 * 来源于网络
 * base64转Multipart
 * @author 来源于网络
 */
public class BASE64DecodedMultipartFile implements MultipartFile {
  
    private final byte[] imgContent;
    private final String header;
  
    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }
  
    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }
  
    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
    }
  
    @Override
    public String getContentType() {
        return header.split(":")[1];
    }
  
    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }
  
    @Override
    public long getSize() {
        return imgContent.length;
    }
  
    @Override
    public byte[] getBytes() {
        return imgContent;
    }
  
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }
  
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(dest)){
            fileOutputStream.write(imgContent);
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * base64转Multipart
     * @param base64 base64
     * @return MultipartFile
     */
    public static MultipartFile base64ToMultipart(String base64) {
        String[] baseStrs = base64.split(",");

        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] b = decoder.decode(baseStrs[1]);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return new BASE64DecodedMultipartFile(b, baseStrs[0]);
    }
  
}