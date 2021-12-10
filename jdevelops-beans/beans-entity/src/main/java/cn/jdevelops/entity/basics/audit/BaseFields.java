package cn.jdevelops.entity.basics.audit;

import cn.jdevelops.entity.basics.vo.SerializableVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 数据表中自动创建时间 公共类
 * @author tn
 * @version 1
 * @date 2020/5/26 22:12
 */
@Getter
@Setter
@ToString
public class BaseFields<T> extends SerializableVO<T> {

    /**
     * 表示该字段为创建时间字段，在这个实体被insert的时候，会自动为其赋值
     */
    private LocalDateTime createTime;


    /**
     * 表示该字段为修改时间字段，在这个实体被update的时候，会自动为其赋值
     */
    private LocalDateTime updateTime;


}
