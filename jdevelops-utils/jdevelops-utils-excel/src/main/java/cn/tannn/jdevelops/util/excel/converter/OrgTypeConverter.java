package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.OrgTypeEnum;

/**
 * 组织类型转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class OrgTypeConverter extends GenericExcelConverter {
    public OrgTypeConverter() {
        super(OrgTypeEnum.class);
    }
}
