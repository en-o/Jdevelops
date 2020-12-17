package com.detabes.entity.basics.audit;

import com.detabes.entity.basics.vo.SerializableVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author tn
 * @version 1
 * @ClassName BaseDate
 * @description 数据表中自动创建时间 公共类
 * @date 2020/5/26 22:12
 */
@Getter
@Setter
@ToString
public class BaseAuditFields<T> extends SerializableVo<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "创建时间",hidden=true)
    private LocalDateTime createTime;

    /**
     * 表示该字段为创建人，在这个实体被insert的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "创建人",hidden=true)
    private String createUserName;

    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "修改时间",hidden=true)
    private LocalDateTime updateTime;

    /**
     * 表示该字段为修改人，在这个实体被update的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "修改人",hidden=true)
    private String updateUserName;

}
