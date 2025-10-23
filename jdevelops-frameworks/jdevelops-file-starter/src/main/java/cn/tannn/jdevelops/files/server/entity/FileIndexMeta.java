package cn.tannn.jdevelops.files.server.entity;

import cn.hutool.core.date.DateTime;
import cn.tannn.cat.file.sdk.bean.FileIndex;
import cn.tannn.jdevelops.files.server.controller.vo.FileIndexVO;
import cn.tannn.jdevelops.jpa.modle.fn.JpaAuditFnFields;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import java.time.format.DateTimeFormatter;

import static cn.tannn.jdevelops.uitls.aop.CommonConstant.DEFAULT_FORMAT_DATETIME;


/**
 * 文件索引数据
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/7 13:18
 */
@Entity
@Table(name = "file_index_meta")
@Comment("文件索引数据")
@DynamicUpdate
@DynamicInsert
@AutoConfigurationPackage
public class FileIndexMeta extends JpaAuditFnFields<FileIndexMeta> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuidCustomGenerator")
    @GenericGenerator(name = "uuidCustomGenerator", strategy = "cn.tannn.jdevelops.jpa.generator.UuidCustomGenerator")
    @Column(columnDefinition = "bigint")
    @Comment("主键，自动生成")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 存储器配置id[FileStorage.id]
     * 对真实文件进行操作是需要拿到其存储的配置
     */
    @Comment("存储器配置id[FileConfig.id]")
    @Column(columnDefinition = "bigint", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storageId;

    /**
     * 字典值[Dict.value] configId的冗余字段
     */
    @Comment("字典值[Dict.value] configId的冗余字段")
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String storage;


    /**
     * 原文件名
     */
    @Comment("原文件名")
    @Column(columnDefinition = "varchar(1024)")
    private String originalName;

    /**
     * 新文件名
     */
    @Comment("新文件名")
    @Column(columnDefinition = "varchar(512)", nullable = false)
    private String freshName;

    /**
     * 文件路径(存储在设备里的真实路径) [本地磁盘是全路径，oss是不带bucket的全路径
     */
    @Comment("文件路径(存储在设备里的真实路径)")
    @Column(columnDefinition = "text", nullable = false)
    private String path;

    /**
     * 完整的文件URL
     */
    @Comment("文件URL")
    @Column(columnDefinition = "text", nullable = false)
    private String url;

    /**
     * 文件URL后缀[去除了url中的域名或者ip,那个在一些场合可以由前端自定义]
     */
    @Comment("文件URL后缀[去除了url中的域名或者ip,那个在一些场合可以由前端自定义]")
    @Column(columnDefinition = "text", nullable = false)
    private String urlSuffix;


    /**
     * 存储的桶
     */
    @Comment("存储的桶")
    @Column(columnDefinition = "varchar(100)")
    private String bucket;


    /**
     * 存储的桶
     */
    @Comment("文件在桶中的路径[桶加子路径和本身]")
    @Column(columnDefinition = "text")
    private String bucketPath;


    /**
     * 文件类型[contentType]
     */
    @Comment("文件类型[contentType]")
    @Column(columnDefinition = "varchar(128)")
    private String type;

    /**
     * 文件大小
     */
    @Comment("文件大小")
    @Column(columnDefinition = "varchar(128)")
    private String size;


    /**
     * 文件大小[GB,MB,KB]
     */
    @Comment("文件大小[GB,MB,KB]")
    @Column(columnDefinition = "varchar(128)")
    private String sizeUnit;


    /**
     * FileStorage -> FileStorageVO
     */
    public FileIndexVO toFileStorageVO() {
        FileIndexVO vo = new FileIndexVO();
        vo.setId(id);
        vo.setStorageId(storageId);
        vo.setStorage(storage);
        vo.setOriginalName(originalName);
        vo.setFreshName(freshName);
        vo.setPath(path);
        vo.setUrl(url);
        vo.setBucket(bucket);
        vo.setBucketPath(bucketPath);
        vo.setUrlSuffix(urlSuffix);
        vo.setType(type);
        vo.setSize(size);
        vo.setSizeUnit(sizeUnit);
        if (null != getCreateTime()) {
            vo.setCreateTime(getCreateTime().format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT_DATETIME)));
        } else {
            vo.setCreateTime(DateTime.now().toString());
        }
        vo.setCreateUserName(getCreateUserName());
        return vo;
    }

    /**
     * FileIndexMeta -> FileIndex
     */
    public FileIndex toFileIndex() {
        FileIndex fileIndex = new FileIndex();
        fileIndex.setId(id);
        fileIndex.setStorageId(storageId);
        fileIndex.setStorage(storage);
        fileIndex.setOriginalName(originalName);
        fileIndex.setFreshName(freshName);
        fileIndex.setPath(path);
        fileIndex.setUrl(url);
        fileIndex.setBucket(bucket);
        fileIndex.setBucketPath(bucketPath);
        fileIndex.setUrlSuffix(urlSuffix);
        fileIndex.setType(type);
        fileIndex.setSize(Long.valueOf(null == size ? "0" : size));
        fileIndex.setSizeUnit(sizeUnit);
        return fileIndex;
    }


    /**
     * FileIndex -> FileIndexMeta
     */
    public static FileIndexMeta toFileIndexMeta(FileIndex fileIndex) {
        FileIndexMeta indexMeta = new FileIndexMeta();
        indexMeta.setStorageId(fileIndex.getStorageId());
        indexMeta.setStorage(fileIndex.getStorage());
        indexMeta.setOriginalName(fileIndex.getOriginalName());
        indexMeta.setFreshName(fileIndex.getFreshName());
        indexMeta.setPath(fileIndex.getPath());
        indexMeta.setUrl(fileIndex.getUrl());
        indexMeta.setBucket(fileIndex.getBucket());
        indexMeta.setBucketPath(fileIndex.getBucketPath());
        indexMeta.setUrlSuffix(fileIndex.getUrlSuffix());
        indexMeta.setType(fileIndex.getType());
        indexMeta.setSize(fileIndex.getSize() == null ? "0" : fileIndex.getSize() + "");
        indexMeta.setSizeUnit(fileIndex.getSizeUnit());
        return indexMeta;
    }


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

    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    @Override
    public String toString() {
        return "FileIndexMeta{" +
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
               '}';
    }
}
