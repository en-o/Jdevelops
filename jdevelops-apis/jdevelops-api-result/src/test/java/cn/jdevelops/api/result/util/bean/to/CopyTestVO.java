package cn.jdevelops.api.result.util.bean.to;

import cn.jdevelops.api.result.bean.SerializableBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyTestVO extends SerializableBean<CopyTestVO> {
    String name;
    Integer age;
}
