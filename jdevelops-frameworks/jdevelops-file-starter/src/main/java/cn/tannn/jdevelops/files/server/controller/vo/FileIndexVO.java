package cn.tannn.jdevelops.files.server.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.StringJoiner;

/**
 * 文件索引，快速查询各个存储器的文件数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-09 20:22
 */
@Schema(description = "文件索引查询视图" )
public class FileIndexVO  {

    /**
     * id
     */
    @Schema(description = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 存储器配置id[FileStorage.id]
     * 对真实文件进行操作是需要拿到其存储的配置
     */
    @Schema(description =" 存储器配置id[FileStorage.id]")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storageId;


    /**
     * 字典值[Dict.value] configId的冗余字段
     */
    @Schema(description ="字典值[Dict.value] configId的冗余字段")
    private String storage;

    /**
     * 原文件名
     */
    @Schema(description ="原文件名")
    private String originalName;

    /**
     * 新文件名
     */
    @Schema(description ="新文件名")
    private String freshName;

    /**
     * 文件路径(存储在设备里的真实路径)
     */
    @Schema(description ="文件路径(存储在设备里的真实路径)")
    private String path;

    /**
     * 完整的文件URL
     */
    @Schema(description ="文件URL")
    private String url;

    /**
     * 文件URL后缀[去除了url中的域名或者ip,那个在一些场合可以由前端自定义]
     */
    @Schema(description ="文件URL后缀[去除了url中的域名或者ip,那个在一些场合可以由前端自定义]")
    private String urlSuffix;


    /**
     * 存储的桶
     */
    @Schema(description ="存储的桶")
    private String bucket;

    /**
     * 文件在桶中的路径[桶加子路径和本身]
     */
    @Schema(description ="文件在桶中的路径[桶加子路径和本身]")
    private String bucketPath;

    /**
     * 文件类型[contentType]
     */
    @Schema(description ="文件类型[contentType]")
    private String type;

    /**
     * 文件大小
     */
    @Schema(description ="文件大小")
    private String size;

    /**
     * 文件大小
     */
    @Schema(description ="文件大小")
    private String sizeUnit;


    /**
     * 表示该字段为创建时间字段，
     */
    @Schema(description = "创建时间")
    private String createTime;

    /**
     * 表示该字段为创建人，
     */
    @Schema(description = "创建者")
    private String createUserName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFreshName() {
        return freshName;
    }

    public void setFreshName(String freshName) {
        this.freshName = freshName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }


    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    @Override
    public String toString() {
        return "FileIndexVO{" +
                "id=" + id +
                ", storageId=" + storageId +
                ", storage='" + storage + '\'' +
                ", originalName='" + originalName + '\'' +
                ", freshName='" + freshName + '\'' +
                ", path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", urlSuffix='" + urlSuffix + '\'' +
                ", bucket='" + bucket + '\'' +
                ", bucketPath='" + bucketPath + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", sizeUnit='" + sizeUnit + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUserName='" + createUserName + '\'' +
                '}';
    }
}
