package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.AccountTypeEnum;

/**
 * 账号类型转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class AccountTypeConverter extends GenericExcelConverter {
    public AccountTypeConverter() {
        super(AccountTypeEnum.class);
    }
}
