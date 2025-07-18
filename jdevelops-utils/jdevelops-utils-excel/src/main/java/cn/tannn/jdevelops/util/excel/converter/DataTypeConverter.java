package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.DataTypeEnum;

/**
 * 数据类型转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class DataTypeConverter extends GenericExcelConverter {
    public DataTypeConverter() {
        super(DataTypeEnum.class);
    }
}
