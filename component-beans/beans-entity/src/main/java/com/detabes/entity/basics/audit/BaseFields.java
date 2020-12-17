package com.detabes.entity.basics.audit;

import com.detabes.entity.basics.vo.SerializableVO;
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
public class BaseFields<T> extends SerializableVO<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "创建时间",hidden=true)
    private LocalDateTime createTime;


    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    @ApiModelProperty(value = "修改时间",hidden=true)
    private LocalDateTime updateTime;


}
