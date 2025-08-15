package cn.tannn.jdevelops.renewpwd;

import java.util.List;

/**
 * 密码续命处理
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
public interface RenewPwdRefresh {
    /**
     * 重置数据库密码并生效 - 上下文取密码
     */
    void fixPassword();

    /**
     * 重置数据库密码并生效
     * @param newPassword 新密码 (不允许加密-如果是密文自己先处理)
     */
    void fixPassword(String newPassword);

    /**
     * 重置数据库密码并生效
     * @param newPassword 新密码 (不允许加密-如果是密文自己先处理)
     * @param beanNames 需要刷新的Bean名称列表 - 默认 dataSource
     */
    void fixPassword(String newPassword, List<String> beanNames);


}
