package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.AccountStateEnum;

/**
 * 账号状态转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class AccountStateConverter extends GenericExcelConverter {
    public AccountStateConverter() {
        super(AccountStateEnum.class);
    }
}
