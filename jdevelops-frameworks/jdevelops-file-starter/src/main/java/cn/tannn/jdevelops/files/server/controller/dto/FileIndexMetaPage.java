package cn.tannn.jdevelops.files.server.controller.dto;

import cn.tannn.jdevelops.annotations.jpa.JpaSelectIgnoreField;
import cn.tannn.jdevelops.annotations.jpa.JpaSelectOperator;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLConnect;
import cn.tannn.jdevelops.annotations.jpa.enums.SQLOperatorWrapper;
import cn.tannn.jdevelops.jpa.request.PagingSorteds;
import cn.tannn.jdevelops.result.bean.SerializableBean;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import java.util.StringJoiner;

/**
 * 文件元数据分页
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-09 20:24
 */
@Schema(description = "分页查询文件元数据")
@Valid
public class FileIndexMetaPage extends SerializableBean<FileIndexMetaPage> {

    /**
     * 存储器ID[FileStorageConfig.id]
     * 对真实文件进行操作时需要拿到其存储的配置
     */
    @Schema(description ="存储器ID[FileStorageConfig.id]")
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.EQ, connect = SQLConnect.AND)
    private Long storageId;


    /**
     * 新文件名
     */
    @Schema(description ="新文件名")
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.LIKE, connect = SQLConnect.AND)
    private String freshName;


    /**
     * 存储的桶
     */
    @Schema(description ="存储的桶")
    @JpaSelectOperator(operatorWrapper = SQLOperatorWrapper.LIKE, connect = SQLConnect.AND)
    private String bucket;


    /**
     * 分页排序
     */
    @Schema(description = "分页排序")
    @JpaSelectIgnoreField
    @Valid
    private PagingSorteds page;

    public PagingSorteds getPage() {
        if(page == null){
            return new PagingSorteds().fixSort(1,"id");
        }else {
            return page.append(1, "id");
        }
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getFreshName() {
        return freshName;
    }

    public void setFreshName(String freshName) {
        this.freshName = freshName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setPage(@Valid PagingSorteds page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FileIndexMetaPage.class.getSimpleName() + "[", "]")
                .add("storageId=" + storageId)
                .add("freshName='" + freshName + "'")
                .add("bucket='" + bucket + "'")
                .add("page=" + page)
                .toString();
    }
}
