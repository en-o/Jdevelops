package cn.tannn.jdevelops.util.excel.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.tannn.jdevelops.util.excel.converter.enums.ExcelConvertEnum;

import java.lang.reflect.Method;

/**
 * 通用Excel转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class GenericExcelConverter implements Converter<Integer> {

    private final Class<? extends ExcelConvertEnum> enumClass;
    private final Method getByDbValueMethod;
    private final Method getByExcelDisplayMethod;

    public GenericExcelConverter(Class<? extends ExcelConvertEnum> enumClass) {
        this.enumClass = enumClass;
        try {
            this.getByDbValueMethod = enumClass.getMethod("getByDbValue", Integer.class);
            this.getByExcelDisplayMethod = enumClass.getMethod("getByExcelDisplay", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("枚举类必须实现 getByDbValue 和 getByExcelDisplay 方法", e);
        }
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        try {
            String value = cellData.getStringValue();
            ExcelConvertEnum enumValue = (ExcelConvertEnum) getByExcelDisplayMethod.invoke(null, value);
            return enumValue.getDbValue();
        } catch (Exception e) {
            throw new RuntimeException("Excel数据转换失败", e);
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        try {
            ExcelConvertEnum enumValue = (ExcelConvertEnum) getByDbValueMethod.invoke(null, value);
            return new WriteCellData<>(enumValue.getExcelDisplay());
        } catch (Exception e) {
            throw new RuntimeException("数据库数据转换失败", e);
        }
    }
}
